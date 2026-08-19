package com.treinamento.app_fidelidade.feature.resgate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.data.remote.service.ResgateService
import com.treinamento.app_fidelidade.data.remote.service.ResultadoApi
import com.treinamento.app_fidelidade.feature.carrinho.CarrinhoRepositorio
import com.treinamento.app_fidelidade.repository.SaldoPontosRepositorio
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

    // Textos da tela mudam conforme tem ou nao internet (imagens 7 e 8).
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

class ConfirmarResgateViewModel : ViewModel() {

    private val service = ResgateService()

    private val _uiState = MutableStateFlow(ConfirmarResgateUiState())
    val uiState: StateFlow<ConfirmarResgateUiState> = _uiState.asStateFlow()

    private var origem: OrigemResgate? = null

    init {
        viewModelScope.launch {
            SaldoPontosRepositorio.saldo.collect { saldo ->
                _uiState.update { it.copy(saldoPontos = saldo ?: 0) }
            }
        }
    }

    /** Chamado uma vez pela tela, com a origem (carrinho ou pendente). */
    fun carregar(origem: OrigemResgate) {
        if (this.origem == origem) return
        this.origem = origem

        val itens = when (origem) {
            OrigemResgate.Carrinho -> CarrinhoRepositorio.itens.value.paraItensResgate()
            is OrigemResgate.Pendente -> ResgateRepositorio.buscarPorId(origem.resgateId)?.itens.orEmpty()
        }

        _uiState.update {
            it.copy(carregando = false, itens = itens, semConexao = false, mensagemErro = null)
        }

        // Saldo atual do servidor, para o resumo da tela nao ficar defasado.
        viewModelScope.launch { SaldoPontosRepositorio.atualizar() }
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

        if (!ConexaoMock.estaOnline()) {
            tratarSemConexao(state, onFinalizado)
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(enviando = true, mensagemErro = null) }

            when (val resultado = service.criarResgate(state.itens.paraItensRequest())) {
                is ResultadoApi.Sucesso -> {
                    val resgate = resultado.dados
                    SaldoPontosRepositorio.definir(resgate.pontosSaldoAtual.toLong())
                    registrarConclusao(state, resgate.idResgate.toLong())
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

    private fun registrarConclusao(state: ConfirmarResgateUiState, idResgate: Long) {
        when (val origemAtual = origem) {
            is OrigemResgate.Pendente -> ResgateRepositorio.concluir(origemAtual.resgateId, idResgate)
            else -> {
                ResgateRepositorio.salvar(state.itens, StatusResgate.CONCLUIDO, idResgate)
                CarrinhoRepositorio.limpar()
            }
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
            ResgateRepositorio.salvar(state.itens, StatusResgate.PENDENTE)
            CarrinhoRepositorio.limpar()
        }
        onFinalizado(StatusResgate.PENDENTE)
    }
}
