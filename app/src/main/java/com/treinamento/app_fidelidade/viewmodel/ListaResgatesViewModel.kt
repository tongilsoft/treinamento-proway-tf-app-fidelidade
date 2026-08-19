package com.treinamento.app_fidelidade.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.treinamento.app_fidelidade.data.remote.service.ResultadoApi
import com.treinamento.app_fidelidade.di.AppContainer
import com.treinamento.app_fidelidade.model.FiltroResgate
import com.treinamento.app_fidelidade.model.Resgate
import com.treinamento.app_fidelidade.model.StatusResgate
import com.treinamento.app_fidelidade.repository.Conexao
import com.treinamento.app_fidelidade.repository.ResgateRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ListaResgatesUiState(
    val carregando: Boolean = true,
    val resgates: List<Resgate> = emptyList(),
    val filtro: FiltroResgate = FiltroResgate.TODOS,
    val mensagem: String? = null
) {
    // O filtro das abas e regra de apresentacao: mora no estado, nao na tela.
    val resgatesFiltrados: List<Resgate>
        get() = when (filtro) {
            FiltroResgate.TODOS -> resgates
            FiltroResgate.CONCLUIDOS -> resgates.filter { it.status == StatusResgate.CONCLUIDO }
            FiltroResgate.PENDENTES -> resgates.filter { it.status == StatusResgate.PENDENTE }
        }

    val vazio: Boolean get() = resgatesFiltrados.isEmpty()
}

class ListaResgatesViewModel(
    private val resgateRepository: ResgateRepository,
    private val conexao: Conexao
) : ViewModel() {

    private val filtro = MutableStateFlow(FiltroResgate.TODOS)
    private val mensagem = MutableStateFlow<String?>(null)
    private val carregando = MutableStateFlow(true)

    val uiState: StateFlow<ListaResgatesUiState> = combine(
        resgateRepository.resgates,
        filtro,
        mensagem,
        carregando
    ) { resgates, filtroAtual, msg, carregandoAtual ->
        ListaResgatesUiState(
            carregando = carregandoAtual,
            resgates = resgates,
            filtro = filtroAtual,
            mensagem = msg
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ListaResgatesUiState())

    /**
     * Busca os concluidos no extrato do servidor. Os pendentes nao dependem disso:
     * eles sao locais e continuam na lista mesmo se a chamada falhar.
     */
    fun carregar() = viewModelScope.launch {
        carregando.value = true
        when (val resultado = resgateRepository.atualizarConcluidos()) {
            is ResultadoApi.Sucesso -> Unit
            ResultadoApi.SemConexao ->
                mensagem.value = "Sem internet. Mostrando apenas os resgates deste aparelho."
            is ResultadoApi.Erro -> mensagem.value = resultado.mensagem
        }
        carregando.value = false
    }

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
        if (conexao.estaOnline()) {
            onOnline(resgateId)
        } else {
            mensagem.value = "Voce esta sem internet. Tente novamente mais tarde."
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ListaResgatesViewModel(
                    resgateRepository = AppContainer.resgateRepository,
                    conexao = AppContainer.conexao
                )
            }
        }
    }
}
