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
import java.time.Instant
import java.time.ZoneOffset

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
     * Periodo escolhido no calendario, guardado no formato da API ("2026-08-01").
     * Null nos dois quer dizer "sem filtro de data".
     */
    var dataInicio by mutableStateOf<String?>(null)
        private set

    var dataFim by mutableStateOf<String?>(null)
        private set

    val periodoAtivo: Boolean
        get() = dataInicio != null || dataFim != null

    /** Texto do periodo para a tela, tipo "01/08/2026 - 24/08/2026". */
    val periodoFormatado: String?
        get() = when {
            dataInicio != null && dataFim != null -> "${paraExibicao(dataInicio!!)} - ${paraExibicao(dataFim!!)}"
            dataInicio != null -> "A partir de ${paraExibicao(dataInicio!!)}"
            dataFim != null -> "Ate ${paraExibicao(dataFim!!)}"
            else -> null
        }

    /*
     * Quem filtra e o ViewModel, nao a tela: a tela so desenha a lista que recebe.
     * Credito e o que entrou (bonificacao, transferencia recebida) e debito e o que
     * saiu (resgate, transferencia enviada).
     */
    val extratoFiltrado: List<MovimentacaoResponse>
        get() = extrato
            .filter { cabeNoTipo(it) }
            .filter { cabeNoPeriodo(it) }

    private fun cabeNoTipo(movimentacao: MovimentacaoResponse): Boolean = when (filtro) {
        FiltroExtrato.TODOS -> true
        FiltroExtrato.GANHOS -> movimentacao.tipo.equals("credito", ignoreCase = true)
        FiltroExtrato.GASTOS -> movimentacao.tipo.equals("debito", ignoreCase = true)
    }

    /*
     * Data da API vem como "2026-08-01". Nesse formato a comparacao de texto ja da a
     * ordem certa (ano, mes, dia), entao nao precisa converter para Date so para
     * saber se a movimentacao esta dentro do intervalo.
     */
    private fun cabeNoPeriodo(movimentacao: MovimentacaoResponse): Boolean {
        val data = movimentacao.data.take(10)
        val depoisDoInicio = dataInicio?.let { data >= it } ?: true
        val antesDoFim = dataFim?.let { data <= it } ?: true
        return depoisDoInicio && antesDoFim
    }

    fun alterarFiltro(novoFiltro: FiltroExtrato) {
        filtro = novoFiltro
    }

    /**
     * Recebe o que o DateRangePicker devolve (milissegundos em UTC) e guarda
     * ja no formato da API.
     */
    fun alterarPeriodo(inicioMillis: Long?, fimMillis: Long?) {
        dataInicio = inicioMillis?.let { paraIso(it) }
        dataFim = fimMillis?.let { paraIso(it) }
    }

    fun limparPeriodo() {
        dataInicio = null
        dataFim = null
    }

    private fun paraIso(millis: Long): String =
        Instant.ofEpochMilli(millis).atZone(ZoneOffset.UTC).toLocalDate().toString()

    private fun paraExibicao(iso: String): String {
        val partes = iso.split("-")
        return if (partes.size == 3) "${partes[2]}/${partes[1]}/${partes[0]}" else iso
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