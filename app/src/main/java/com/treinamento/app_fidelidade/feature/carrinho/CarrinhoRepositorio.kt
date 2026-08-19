package com.treinamento.app_fidelidade.feature.carrinho

import com.treinamento.app_fidelidade.model.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.text.NumberFormat
import java.util.Locale

/** Produto escolhido + quantas unidades o usuario quer resgatar. */
data class ItemCarrinho(
    val produto: Produto,
    val quantidade: Int
) {
    val totalPontos: Long get() = produto.valorPontos * quantidade
}

/** Deixa o numero no padrao do design: 13123678 -> "13.123.678". */
fun Long.formatarPontos(): String =
    NumberFormat.getIntegerInstance(Locale.forLanguageTag("pt-BR")).format(this)

/**
 * Repositorio do carrinho em memoria.
 * E um object para a tela de Resgate enxergar os mesmos itens e limpar o
 * carrinho depois que o resgate e concluido.
 */
object CarrinhoRepositorio {

    private val _itens = MutableStateFlow<List<ItemCarrinho>>(emptyList())
    val itens: StateFlow<List<ItemCarrinho>> = _itens.asStateFlow()

    /** Somar de novo o mesmo produto aumenta a quantidade da linha existente. */
    fun adicionar(produto: Produto, quantidade: Int = 1) {
        if (quantidade < 1) return
        _itens.update { lista ->
            val existente = lista.find { it.produto.id == produto.id }
            if (existente == null) {
                lista + ItemCarrinho(produto, quantidade)
            } else {
                lista.map {
                    if (it.produto.id == produto.id) it.copy(quantidade = it.quantidade + quantidade) else it
                }
            }
        }
    }

    fun alterarQuantidade(produtoId: Long, novaQuantidade: Int) {
        if (novaQuantidade < 1) return
        _itens.update { lista ->
            lista.map { if (it.produto.id == produtoId) it.copy(quantidade = novaQuantidade) else it }
        }
    }

    fun remover(produtoId: Long) {
        _itens.update { lista -> lista.filterNot { it.produto.id == produtoId } }
    }

    fun limpar() {
        _itens.value = emptyList()
    }
}
