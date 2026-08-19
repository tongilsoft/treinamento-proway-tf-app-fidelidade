package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.service.ResgateService
import com.treinamento.app_fidelidade.data.remote.service.ResultadoApi
import com.treinamento.app_fidelidade.model.ItemResgate
import com.treinamento.app_fidelidade.model.Resgate
import com.treinamento.app_fidelidade.model.ResgateConcluido
import com.treinamento.app_fidelidade.model.StatusResgate
import com.treinamento.app_fidelidade.model.paraItensRequest
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * Contrato dos resgates (camada MODEL).
 *
 * O ViewModel fala com esta interface e nao sabe que existe Retrofit do outro lado.
 * Quem traduz JSON em [Resgate] e a implementacao — se amanha o resgate vier de um
 * banco local, o ViewModel nao muda.
 */
interface ResgateRepository {

    /** Pendentes e concluidos ja combinados, prontos para a tela. */
    val resgates: StateFlow<List<Resgate>>

    fun buscarPorId(id: Long): Resgate?

    /** Rebusca os concluidos no servidor. */
    suspend fun atualizarConcluidos(): ResultadoApi<List<Resgate>>

    /**
     * Envia o resgate para a API. E aqui que o Retrofit e usado — o ViewModel
     * so ve [ResgateConcluido] ou um [ResultadoApi] de falha.
     */
    suspend fun criar(itens: List<ItemResgate>): ResultadoApi<ResgateConcluido>

    fun salvarPendente(itens: List<ItemResgate>): Resgate

    fun concluir(id: Long)
}

class ResgateRepositoryPadrao(
    private val service: ResgateService,
    private val escopo: CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
) : ResgateRepository {

    private val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
    private val formatoDataApi = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("pt-BR"))

    /** Concluidos: vem do extrato do servidor, nao sao guardados aqui. */
    private val _concluidos = MutableStateFlow<List<Resgate>>(emptyList())

    /** Pendentes: so existem no app enquanto nao sobem. Vai virar SQLite. */
    private val _pendentes = MutableStateFlow<List<Resgate>>(emptyList())

    /** Pendente primeiro (e o que exige acao), concluido depois. */
    override val resgates: StateFlow<List<Resgate>> =
        combine(_pendentes, _concluidos) { pendentes, concluidos -> pendentes + concluidos }
            .stateIn(escopo, SharingStarted.Eagerly, emptyList())

    private var proximoId = 1L

    override fun buscarPorId(id: Long): Resgate? =
        _pendentes.value.find { it.id == id } ?: _concluidos.value.find { it.id == id }

    override suspend fun criar(itens: List<ItemResgate>): ResultadoApi<ResgateConcluido> =
        when (val resultado = service.criarResgate(itens.paraItensRequest())) {
            is ResultadoApi.Sucesso -> ResultadoApi.Sucesso(
                ResgateConcluido(
                    idResgate = resultado.dados.idResgate.toLong(),
                    pontosUtilizados = resultado.dados.pontosUtilizados.toLong(),
                    pontosSaldoAtual = resultado.dados.pontosSaldoAtual.toLong()
                )
            )

            ResultadoApi.SemConexao -> ResultadoApi.SemConexao
            is ResultadoApi.Erro -> resultado
        }

    /**
     * Monta a lista de resgates concluidos a partir de GET /pontos/extrato.
     *
     * Resgate no extrato e debito com produto: transferencia enviada tambem e
     * debito, mas vem com idProduto nulo. Cada item do resgate e uma movimentacao
     * separada, por isso o agrupamento pelo idResgate que todas compartilham.
     */
    override suspend fun atualizarConcluidos(): ResultadoApi<List<Resgate>> {
        return when (val resultado = service.buscarExtrato()) {
            is ResultadoApi.Sucesso -> {
                val concluidos = resultado.dados
                    .filter { it.tipo == "debito" && it.idProduto != null }
                    .groupBy { it.idResgate?.toLong() }
                    .map { (idResgate, movimentacoes) -> montarResgate(idResgate, movimentacoes) }
                    .sortedByDescending { it.idResgate ?: it.id }
                _concluidos.value = concluidos
                ResultadoApi.Sucesso(concluidos)
            }

            ResultadoApi.SemConexao -> ResultadoApi.SemConexao
            is ResultadoApi.Erro -> resultado
        }
    }

    private fun montarResgate(idResgate: Long?, movimentacoes: List<MovimentacaoResponse>): Resgate {
        val itens = movimentacoes.map { mov ->
            val quantidade = (mov.quantidade?.toInt() ?: 1).coerceAtLeast(1)
            ItemResgate(
                produtoId = mov.idProduto?.toLong() ?: 0L,
                nome = mov.nomeProduto ?: mov.descricao,
                // O extrato traz o total da linha; o card mostra o valor unitario.
                pontos = mov.valorPontos.toLong() / quantidade,
                quantidade = quantidade
            )
        }

        // Movimentacao sem idResgate (historico antigo) usa o id dela mesma como chave.
        val id = idResgate ?: movimentacoes.first().id.toLong()
        return Resgate(
            id = id,
            itens = itens,
            status = StatusResgate.CONCLUIDO,
            data = formatarData(movimentacoes.first().data),
            idResgate = idResgate
        )
    }

    private fun formatarData(dataApi: String): String =
        try {
            formatoDataApi.parse(dataApi)?.let { formatoData.format(it) } ?: dataApi
        } catch (_: ParseException) {
            dataApi
        }

    /**
     * So pendente entra aqui. Resgate concluido nao e guardado no app: ele volta
     * pelo extrato do servidor, e salvar dos dois lados duplicaria o card na lista.
     */
    override fun salvarPendente(itens: List<ItemResgate>): Resgate {
        val novo = Resgate(proximoId++, itens, StatusResgate.PENDENTE, formatoData.format(Date()))
        _pendentes.update { listOf(novo) + it }
        return novo
    }

    /**
     * O pendente sobe com sucesso: sai da lista local porque agora ele existe
     * no extrato do servidor.
     */
    override fun concluir(id: Long) {
        _pendentes.update { lista -> lista.filterNot { it.id == id } }
    }
}
