package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.model.ItemCarrinho
import com.treinamento.app_fidelidade.model.Produto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Contrato do carrinho (camada MODEL).
 *
 * O ViewModel depende desta interface, nunca da implementacao. E isso que permite
 * trocar o carrinho em memoria por um carrinho em banco depois, ou passar um falso
 * no teste, sem tocar em uma linha do ViewModel.
 */
interface CarrinhoRepository {

    /** Fonte da verdade do carrinho. Quem observa recebe toda alteracao. */
    val itens: StateFlow<List<ItemCarrinho>>

    fun adicionar(produto: Produto, quantidade: Int = 1)

    fun alterarQuantidade(produtoId: Long, novaQuantidade: Int)

    fun remover(produtoId: Long)

    fun limpar()
}

/**
 * Implementacao atual: o carrinho vive em memoria.
 *
 * Sobrevive a navegacao entre telas porque quem segura a instancia e o
 * [com.treinamento.app_fidelidade.di.AppContainer], nao a tela. Nao sobrevive ao
 * app ser fechado — para isso seria preciso uma implementacao com Room.
 */
class CarrinhoRepositoryEmMemoria : CarrinhoRepository {

    private val _itens = MutableStateFlow<List<ItemCarrinho>>(emptyList())
    override val itens: StateFlow<List<ItemCarrinho>> = _itens.asStateFlow()

    /** Somar de novo o mesmo produto aumenta a quantidade da linha existente. */
    override fun adicionar(produto: Produto, quantidade: Int) {
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

    override fun alterarQuantidade(produtoId: Long, novaQuantidade: Int) {
        if (novaQuantidade < 1) return
        _itens.update { lista ->
            lista.map { if (it.produto.id == produtoId) it.copy(quantidade = novaQuantidade) else it }
        }
    }

    override fun remover(produtoId: Long) {
        _itens.update { lista -> lista.filterNot { it.produto.id == produtoId } }
    }

    override fun limpar() {
        _itens.value = emptyList()
    }
}
