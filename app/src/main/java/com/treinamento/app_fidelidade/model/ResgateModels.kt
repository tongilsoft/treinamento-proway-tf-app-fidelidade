package com.treinamento.app_fidelidade.model

import com.treinamento.app_fidelidade.data.remote.dto.request.ItemResgateRequest
import java.math.BigInteger

/**
 * MODEL do resgate.
 *
 * Nada aqui sabe o que e Compose ou Retrofit. Sao classes de dominio: descrevem o
 * que e um resgate no nosso negocio, e nao como ele chega na tela ou na rede.
 */

enum class StatusResgate { CONCLUIDO, PENDENTE }

/** Abas da tela "Meus Resgates". */
enum class FiltroResgate { TODOS, CONCLUIDOS, PENDENTES }

data class ItemResgate(
    val produtoId: Long,
    val nome: String,
    val pontos: Long,
    val quantidade: Int,
    val imagemUrl: String?
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

/**
 * Resultado de um resgate aceito pelo servidor, ja traduzido para o dominio.
 *
 * O ViewModel recebe isto e nao o DTO: assim ele nao depende do formato do JSON,
 * e mudar o contrato da API nao obriga a mexer no ViewModel.
 */
data class ResgateConcluido(
    val idResgate: Long?,
    val pontosUtilizados: Long?,
    /** Null quando o servidor nao devolve o saldo novo: nesse caso o app rebusca em /pontos. */
    val pontosSaldoAtual: Long?
)

/** De onde a tela de confirmacao foi aberta. */
sealed interface OrigemResgate {
    /** Veio do carrinho: o resgate ainda nao existe. */
    data object Carrinho : OrigemResgate

    /** Veio da lista: e um pendente que sera reenviado. */
    data class Pendente(val resgateId: Long) : OrigemResgate
}

/** Converte o que esta no carrinho para os itens do resgate. */
fun List<ItemCarrinho>.paraItensResgate(): List<ItemResgate> = map {
    ItemResgate(it.produto.id, it.produto.nome, it.produto.valorPontos, it.quantidade,it.imagemUrl)
}

/**
 * Converte os itens do resgate para o corpo de POST /api/resgate.
 * O id do produto no catalogo e o mesmo que a API espera em idProduto.
 */
fun List<ItemResgate>.paraItensRequest(): List<ItemResgateRequest> = map {
    ItemResgateRequest(
        idProduto = BigInteger.valueOf(it.produtoId),
        quantidade = BigInteger.valueOf(it.quantidade.toLong()),

    )
}
