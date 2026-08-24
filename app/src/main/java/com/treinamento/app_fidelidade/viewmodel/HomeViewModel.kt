package com.treinamento.app_fidelidade.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import com.treinamento.app_fidelidade.data.remote.service.HomeService
import com.treinamento.app_fidelidade.data.repository.api.HomeRepository

//meusDados() para nome
//pontos() saldo de pontos
//extrato() para extrato de pontos


/** Abas do extrato na Home. */
enum class FiltroExtrato { TODOS, GANHOS, GASTOS }

class HomeViewModel : ViewModel() {

    private val service = HomeService(
        api = RetrofitInstance.api
    )

    private val repository = HomeRepository(
        service = service
    )

    var saldoPontos by mutableStateOf("")
        private set

    var usuario: Usuario? by mutableStateOf(null)
        private set

    var extrato by mutableStateOf<List<MovimentacaoResponse>>(emptyList())
    private set

    var filtro by mutableStateOf(FiltroExtrato.TODOS)
        private set

    /*
     * Quem filtra e o ViewModel, nao a tela: a tela so desenha a lista que recebe.
     * Credito e o que entrou (bonificacao, transferencia recebida) e debito e o que
     * saiu (resgate, transferencia enviada).
     */
    val extratoFiltrado: List<MovimentacaoResponse>
        get() = when (filtro) {
            FiltroExtrato.TODOS -> extrato
            FiltroExtrato.GANHOS -> extrato.filter { it.tipo.equals("credito", ignoreCase = true) }
            FiltroExtrato.GASTOS -> extrato.filter { it.tipo.equals("debito", ignoreCase = true) }
        }

    fun alterarFiltro(novoFiltro: FiltroExtrato) {
        filtro = novoFiltro
    }

    var carregando by mutableStateOf(false)
        private set

    var erro by mutableStateOf<String?>(null)
        private set


    /**
     * Carrega tudo o que a Home mostra: nome, saldo e extrato.
     *
     * O extrato vem completo de proposito — credito, debito e transferencia.
     * Quem filtra e a tela de Resgates, que so considera debito com idProduto.
     */
    suspend fun carregarHome() {
        try {
            carregando = true
            erro = null

            usuario = repository.meusDados()
            saldoPontos = repository.saldoPontos().pontosSaldo.toString()
            // Mais recente primeiro: a API devolve do mais antigo para o mais novo.
            extrato = repository.extrato().reversed()
        } catch (e: Exception) {
            erro = e.message ?: "Nao foi possivel carregar a Home."
        } finally {
            carregando = false
        }
    }

    suspend fun meusDados() {
        try {
            carregando = true
            erro = null

            usuario = repository.meusDados()

        } catch (e: Exception) {
            erro = e.message
        } finally {
            carregando = false
        }
    }
}