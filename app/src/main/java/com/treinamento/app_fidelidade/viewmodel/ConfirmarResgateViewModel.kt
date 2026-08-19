package com.treinamento.app_fidelidade.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.treinamento.app_fidelidade.data.remote.service.ResultadoApi
import com.treinamento.app_fidelidade.di.AppContainer
import com.treinamento.app_fidelidade.model.ItemResgate
import com.treinamento.app_fidelidade.model.OrigemResgate
import com.treinamento.app_fidelidade.model.Resgate
import com.treinamento.app_fidelidade.model.StatusResgate
import com.treinamento.app_fidelidade.model.paraItensResgate
import com.treinamento.app_fidelidade.repository.CarrinhoRepository
import com.treinamento.app_fidelidade.repository.Conexao
import com.treinamento.app_fidelidade.repository.ConexaoMock
import com.treinamento.app_fidelidade.repository.ResgateRepository
import com.treinamento.app_fidelidade.repository.SaldoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ConfirmarResgateUiState(
    val carregando: Boolean = true,
    val itens: List<ItemResgate> = emptyList(),
    val saldoPontos: Long = 0,
    /** Vira true quando o usuario tenta confirmar e nao ha conexao. */
    val semConexao: Boolean = false,
    /** Enquanto o POST /api/resgate esta em andamento. */
    val enviando: Boolean = false,
    /** Erro de regra devolvido pela API (saldo insuficiente, produto inexistente...). */
    val mensagemErro: String? = null
) {
    val totalItens: Int get() = itens.sumOf { it.quantidade }
    val totalPontos: Long get() = itens.sumOf { it.totalPontos }

    // Ate o texto dos botoes sai do estado: a tela nao decide nada, so desenha.
    val titulo: String get() = if (semConexao) "Resgate Pendente" else "Confirmar Resgate"
    val textoBotao: String
        get() = when {
            enviando -> "Enviando..."
            semConexao -> "Salvar Resgate"
            else -> "Confirmar Resgate"
        }
    val textoRodape: String
        get() = if (semConexao) "Voce pode acompanhar na tela de Resgates Pendentes."
        else "Ao confirmar, seus pontos serao debitados imediatamente."
}

/**
 * VIEWMODEL da confirmacao de resgate.
 *
 * As tres dependencias entram pelo construtor. Antes este ViewModel dava
 * `ResgateService()` e falava com `ConexaoMock` direto: eram dependencias
 * escondidas, impossiveis de substituir no teste. Agora sao declaradas, e da para
 * criar o ViewModel com um repositorio falso e uma conexao que responde offline.
 */
class ConfirmarResgateViewModel(
    private val resgateRepository: ResgateRepository,
    private val carrinhoRepository: CarrinhoRepository,
    private val saldoRepository: SaldoRepository,
    private val conexao: Conexao
) : ViewModel() {

    private val _uiState = MutableStateFlow(ConfirmarResgateUiState())
    val uiState: StateFlow<ConfirmarResgateUiState> = _uiState.asStateFlow()

    private var origem: OrigemResgate? = null

    init {
        viewModelScope.launch {
            saldoRepository.saldo.collect { saldo ->
                _uiState.update { it.copy(saldoPontos = saldo ?: 0) }
            }
        }
    }

    /** Chamado uma vez pela tela, com a origem (carrinho ou pendente). */
    fun carregar(origem: OrigemResgate) {
        if (this.origem == origem) return
        this.origem = origem

        val itens = when (origem) {
            OrigemResgate.Carrinho -> carrinhoRepository.itens.value.paraItensResgate()
            is OrigemResgate.Pendente -> resgateRepository.buscarPorId(origem.resgateId)?.itens.orEmpty()
        }

        _uiState.update {
            it.copy(carregando = false, itens = itens, semConexao = false, mensagemErro = null)
        }

        // Saldo atual do servidor, para o resumo da tela nao ficar defasado.
        viewModelScope.launch { saldoRepository.atualizar() }
    }

    /**
     * Regra da tela:
     * 1) sempre valida a conexao antes;
     * 2) com internet -> envia o resgate para a API e conclui;
     * 3) sem internet -> primeiro avisa e troca o botao para "Salvar Resgate";
     *    no clique seguinte, salva como PENDENTE para reenviar depois.
     *
     * Uma falha de rede no meio do envio cai no mesmo caminho do item 3: o resgate
     * nao se perde, vira pendente.
     */
    fun confirmar(onFinalizado: (StatusResgate) -> Unit) {
        val state = _uiState.value
        if (state.itens.isEmpty() || state.enviando) return

        if (!conexao.estaOnline()) {
            tratarSemConexao(state, onFinalizado)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(enviando = true, mensagemErro = null) }

            when (val resultado = resgateRepository.criar(state.itens)) {
                is ResultadoApi.Sucesso -> {
                    saldoRepository.definir(resultado.dados.pontosSaldoAtual.toLong())
                    registrarConclusao()
                    _uiState.update { it.copy(enviando = false) }
                    onFinalizado(StatusResgate.CONCLUIDO)
                }

                ResultadoApi.SemConexao -> {
                    _uiState.update { it.copy(enviando = false) }
                    tratarSemConexao(_uiState.value, onFinalizado)
                }

                is ResultadoApi.Erro -> _uiState.update {
                    it.copy(enviando = false, mensagemErro = resultado.mensagem)
                }
            }
        }
    }

    fun limparMensagemErro() {
        _uiState.update { it.copy(mensagemErro = null) }
    }

    /**
     * O resgate concluido nao e guardado no app: ele passa a existir no extrato do
     * servidor e a lista de resgates o busca de la. Aqui so limpamos o que era local.
     */
    private fun registrarConclusao() {
        when (val origemAtual = origem) {
            is OrigemResgate.Pendente -> resgateRepository.concluir(origemAtual.resgateId)
            else -> carrinhoRepository.limpar()
        }
    }

    /** Primeiro clique sem rede so avisa; o segundo salva o pendente. */
    private fun tratarSemConexao(
        state: ConfirmarResgateUiState,
        onFinalizado: (StatusResgate) -> Unit
    ) {
        if (!state.semConexao) {
            _uiState.update { it.copy(semConexao = true, mensagemErro = null) }
            return
        }

        // Um pendente reenviado sem rede continua pendente: nao duplica na lista.
        if (origem == OrigemResgate.Carrinho) {
            resgateRepository.salvarPendente(state.itens)
            carrinhoRepository.limpar()
        }
        onFinalizado(StatusResgate.PENDENTE)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ConfirmarResgateViewModel(
                    resgateRepository = AppContainer.resgateRepository,
                    carrinhoRepository = AppContainer.carrinhoRepository,
                    saldoRepository = AppContainer.saldoRepository,
                    conexao = AppContainer.conexao
                )
            }
        }
    }
}
