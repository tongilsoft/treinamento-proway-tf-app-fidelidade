package com.treinamento.app_fidelidade.feature.resgate

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class ListaResgatesUiState(
    val carregando: Boolean = true,
    val resgates: List<Resgate> = emptyList(),
    val filtro: FiltroResgate = FiltroResgate.TODOS,
    val mensagem: String? = null
) {
    val resgatesFiltrados: List<Resgate>
        get() = when (filtro) {
            FiltroResgate.TODOS -> resgates
            FiltroResgate.CONCLUIDOS -> resgates.filter { it.status == StatusResgate.CONCLUIDO }
            FiltroResgate.PENDENTES -> resgates.filter { it.status == StatusResgate.PENDENTE }
        }

    val vazio: Boolean get() = resgatesFiltrados.isEmpty()
}

class ListaResgatesViewModel : ViewModel() {

    private val filtro = MutableStateFlow(FiltroResgate.TODOS)
    private val mensagem = MutableStateFlow<String?>(null)

    val uiState: StateFlow<ListaResgatesUiState> = combine(
        ResgateRepositorio.resgates,
        filtro,
        mensagem
    ) { resgates, filtroAtual, msg ->
        ListaResgatesUiState(
            carregando = false,
            resgates = resgates,
            filtro = filtroAtual,
            mensagem = msg
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ListaResgatesUiState())

    fun alterarFiltro(novoFiltro: FiltroResgate) {
        filtro.value = novoFiltro
    }

    fun limparMensagem() {
        mensagem.value = null
    }

    /**
     * Abrir um pendente so faz sentido com internet, porque a tela de confirmacao
     * vai tentar enviar de novo. Sem conexao, apenas avisa o usuario.
     */
    fun abrirPendente(resgateId: Long, onOnline: (Long) -> Unit) {
        if (ConexaoMock.estaOnline()) {
            onOnline(resgateId)
        } else {
            mensagem.value = "Voce esta sem internet. Tente novamente mais tarde."
        }
    }
}
