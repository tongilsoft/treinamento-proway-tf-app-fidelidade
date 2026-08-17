package com.treinamento.app_fidelidade.feature.resgate

import com.treinamento.app_fidelidade.feature.carrinho.ItemCarrinho
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

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
    val data: String
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

/** Converte o que esta no carrinho para os itens do resgate. */
fun List<ItemCarrinho>.paraItensResgate(): List<ItemResgate> = map {
    ItemResgate(it.produto.id, it.produto.nome, it.produto.valorPontos, it.quantidade)
}

object ResgateRepositorio {

    private val formatoData = SimpleDateFormat("dd/MM/yyyy", Locale.forLanguageTag("pt-BR"))

    private val _resgates = MutableStateFlow(resgatesMockados())
    val resgates: StateFlow<List<Resgate>> = _resgates.asStateFlow()

    private var proximoId = 100L

    fun buscarPorId(id: Long): Resgate? = _resgates.value.find { it.id == id }

    fun salvar(itens: List<ItemResgate>, status: StatusResgate) {
        val novo = Resgate(proximoId++, itens, status, formatoData.format(Date()))
        _resgates.update { listOf(novo) + it }
    }

    fun concluir(id: Long) {
        _resgates.update { lista ->
            lista.map {
                if (it.id == id) it.copy(status = StatusResgate.CONCLUIDO, data = formatoData.format(Date()))
                else it
            }
        }
    }

    // Historico fixo so para a tela ficar igual ao design na apresentacao.
    private fun resgatesMockados() = listOf(
        Resgate(1, listOf(ItemResgate(1, "Fone de Ouvido Bluetooth", 1_000, 1)), StatusResgate.CONCLUIDO, "06/05/2025"),
        Resgate(2, listOf(ItemResgate(2, "Cafeteira Eletrica", 2_800, 1)), StatusResgate.CONCLUIDO, "02/05/2025"),
        Resgate(3, listOf(ItemResgate(4, "Mochila Executiva", 1_500, 1)), StatusResgate.CONCLUIDO, "28/04/2025"),
        Resgate(4, listOf(ItemResgate(3, "Caneca Termica", 600, 1)), StatusResgate.PENDENTE, "07/05/2025"),
        Resgate(5, listOf(ItemResgate(5, "Smartwatch", 5_000, 1)), StatusResgate.PENDENTE, "07/05/2025")
    )
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
