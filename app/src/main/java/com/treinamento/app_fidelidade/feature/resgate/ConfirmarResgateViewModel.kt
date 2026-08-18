package com.treinamento.app_fidelidade.feature.resgate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.feature.carrinho.CarrinhoRepositorio
import com.treinamento.app_fidelidade.repository.InMemoryUsuarioRepository
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
    val semConexao: Boolean = false
) {
    val totalItens: Int get() = itens.sumOf { it.quantidade }
    val totalPontos: Long get() = itens.sumOf { it.totalPontos }

    // Textos da tela mudam conforme tem ou nao internet (imagens 7 e 8).
    val titulo: String get() = if (semConexao) "Resgate Pendente" else "Confirmar Resgate"
    val textoBotao: String get() = if (semConexao) "Salvar Resgate" else "Confirmar Resgate"
    val textoRodape: String
        get() = if (semConexao) "Voce pode acompanhar na tela de Resgates Pendentes."
        else "Ao confirmar, seus pontos serao debitados imediatamente."
}

class ConfirmarResgateViewModel : ViewModel() {

    private val usuarioRepository = InMemoryUsuarioRepository()

    private val _uiState = MutableStateFlow(ConfirmarResgateUiState())
    val uiState: StateFlow<ConfirmarResgateUiState> = _uiState.asStateFlow()

    private var origem: OrigemResgate? = null

    init {
        viewModelScope.launch {
            usuarioRepository.observarUsuario().collect { usuario ->
                _uiState.update { it.copy(saldoPontos = usuario.pontosSaldo) }
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
            it.copy(carregando = false, itens = itens, semConexao = false)
        }
    }

    /**
     * Regra da tela:
     * 1) sempre valida a conexao antes;
     * 2) com internet -> conclui o resgate;
     * 3) sem internet -> primeiro avisa e troca o botao para "Salvar Resgate";
     *    no clique seguinte, salva como PENDENTE.
     */
    fun confirmar(onFinalizado: (StatusResgate) -> Unit) {
        val state = _uiState.value
        if (state.itens.isEmpty()) return

        when {
            ConexaoMock.estaOnline() -> {
                when (val origemAtual = origem) {
                    is OrigemResgate.Pendente -> ResgateRepositorio.concluir(origemAtual.resgateId)
                    else -> {
                        ResgateRepositorio.salvar(state.itens, StatusResgate.CONCLUIDO)
                        CarrinhoRepositorio.limpar()
                    }
                }
                // TODO(integracao): debitar os pontos do usuario na API.
                onFinalizado(StatusResgate.CONCLUIDO)
            }

            // Sem internet e ainda nao avisou: mostra o aviso e troca o botao.
            !state.semConexao -> _uiState.update { it.copy(semConexao = true) }

            // Sem internet e ja avisou: salva como pendente.
            else -> {
                if (origem == OrigemResgate.Carrinho) {
                    ResgateRepositorio.salvar(state.itens, StatusResgate.PENDENTE)
                    CarrinhoRepositorio.limpar()
                }
                onFinalizado(StatusResgate.PENDENTE)
            }
        }
    }
}
