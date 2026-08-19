package com.treinamento.app_fidelidade.feature.resgate

import com.treinamento.app_fidelidade.data.remote.dto.request.ItemResgateRequest
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.service.ResgateService
import com.treinamento.app_fidelidade.data.remote.service.ResultadoApi
import com.treinamento.app_fidelidade.feature.carrinho.ItemCarrinho
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import java.math.BigInteger
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/** Escopo do object: vive junto com o processo, nao com uma tela. */
private val escopo = CoroutineScope(SupervisorJob() + Dispatchers.Default)

enum class StatusResgate { CONCLUIDO, PENDENTE }

/** Abas da tela "Meus Resgates". */
enum class FiltroResgate { TODOS, CONCLUIDOS, PENDENTES }

data class ItemResgate(
    val produtoId: Long,
    val nome: String,
    val pontos: Long,
    val quantidade: Int
) {
    val totalPontos: Long get() = pontos * quantidade
}

data class Resgate(
    val id: Long,
    val itens: List<ItemResgate>,
    val status: StatusResgate,
    val data: String,
    /** Id devolvido por POST /api/resgate. Null enquanto o resgate esta pendente. */
    val idResgate: Long? = null
) {
    val totalItens: Int get() = itens.sumOf { it.quantidade }
    val totalPontos: Long get() = itens.sumOf { it.totalPontos }

    /** Titulo do card na lista: primeiro produto (+ quantos outros). */
    val titulo: String
        get() {
            val primeiro = itens.firstOrNull()?.nome ?: "Resgate"
            val outros = itens.size - 1
            return if (outros > 0) "$primeiro + $outros item(ns)" else primeiro
        }
}

/** De onde a tela de confirmacao foi aberta. */
sealed interface OrigemResgate {
    /** Veio do carrinho: o resgate ainda nao existe. */
    data object Carrinho : OrigemResgate

    /** Veio da lista: e um pendente que sera reenviado. */
    data class Pendente(val resgateId: Long) : OrigemResgate
}

/**
 * Converte os itens do resgate para o corpo de POST /api/resgate.
 * O id do produto no catalogo e o mesmo que a API espera em idProduto.
 */
fun List<ItemResgate>.paraItensRequest(): List<ItemResgateRequest> = map {
    ItemResgateRequest(
        idProduto = BigInteger.valueOf(it.produtoId),
        quantidade = BigInteger.valueOf(it.quantidade.toLong())
    )
}

/** Converte o que esta no carrinho para os itens do resgate. */
fun List<ItemCarrinho>.paraItensResgate(): List<ItemResgate> = map {
    ItemResgate(it.produto.id, it.produto.nome, it.produto.valorPontos, it.quantidade)
}

object ResgateRepositorio {

    private val service = ResgateService()

    private val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))
    private val formatoDataApi = SimpleDateFormat("yyyy-MM-dd", Locale.forLanguageTag("pt-BR"))

    /** Concluidos: vem do extrato do servidor, nao sao guardados aqui. */
    private val _concluidos = MutableStateFlow<List<Resgate>>(emptyList())

    /** Pendentes: so existem no app enquanto nao sobem. Vai virar SQLite. */
    private val _pendentes = MutableStateFlow<List<Resgate>>(emptyList())

    /** Pendente primeiro (e o que exige acao), concluido depois, do mais novo para o mais velho. */
    val resgates: StateFlow<List<Resgate>> = combine(_pendentes, _concluidos) { pendentes, concluidos ->
        pendentes + concluidos
    }.stateIn(escopo, SharingStarted.Eagerly, emptyList())

    private var proximoId = 1L

    fun buscarPorId(id: Long): Resgate? =
        _pendentes.value.find { it.id == id } ?: _concluidos.value.find { it.id == id }

    /**
     * Monta a lista de resgates concluidos a partir de GET /pontos/extrato.
     *
     * Resgate no extrato e debito com produto: transferencia enviada tambem e
     * debito, mas vem com idProduto nulo. Cada item do resgate e uma movimentacao
     * separada, por isso o agrupamento pelo idResgate que todas compartilham.
     */
    suspend fun atualizarConcluidos(): ResultadoApi<List<Resgate>> {
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
    fun salvarPendente(itens: List<ItemResgate>): Resgate {
        val novo = Resgate(proximoId++, itens, StatusResgate.PENDENTE, formatoData.format(Date()))
        _pendentes.update { listOf(novo) + it }
        return novo
    }

    /**
     * O pendente sobe com sucesso: sai da lista local porque agora ele existe
     * no extrato do servidor.
     */
    fun concluir(id: Long) {
        _pendentes.update { lista -> lista.filterNot { it.id == id } }
    }
}

/**
 * Conexao MOCKADA. Na integracao vira ConnectivityManager.
 * Por enquanto o botao de wifi da AppBar liga/desliga, so para demonstrar
 * os dois fluxos (com e sem internet).
 */
object ConexaoMock {

    private val _online = MutableStateFlow(true)
    val online: StateFlow<Boolean> = _online.asStateFlow()

    fun estaOnline(): Boolean = _online.value

    fun alternar() {
        _online.value = !_online.value
    }
}
