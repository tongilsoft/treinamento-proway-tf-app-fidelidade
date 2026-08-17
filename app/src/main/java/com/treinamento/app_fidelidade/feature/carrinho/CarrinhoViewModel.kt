package com.treinamento.app_fidelidade.feature.carrinho

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.repository.InMemoryUsuarioRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

data class CarrinhoUiState(
    val carregando: Boolean = true,
    val itens: List<ItemCarrinho> = emptyList(),
    val saldoPontos: Long = 0
) {
    // Tudo calculado aqui: muda a quantidade, o total muda junto (tempo real).
    val vazio: Boolean get() = itens.isEmpty()
    val totalItens: Int get() = itens.sumOf { it.quantidade }
    val totalPontos: Long get() = itens.sumOf { it.totalPontos }
    val saldoAposResgate: Long get() = saldoPontos - totalPontos
    val saldoSuficiente: Boolean get() = saldoAposResgate >= 0
    val podeContinuar: Boolean get() = !vazio && saldoSuficiente
}

class CarrinhoViewModel : ViewModel() {

    private val usuarioRepository = InMemoryUsuarioRepository()

    val uiState: StateFlow<CarrinhoUiState> = combine(
        CarrinhoRepositorio.itens,
        usuarioRepository.observarUsuario()
    ) { itens, usuario ->
        CarrinhoUiState(
            carregando = false,
            itens = itens,
            saldoPontos = usuario.saldoPontos
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CarrinhoUiState())

    fun aumentar(item: ItemCarrinho) =
        CarrinhoRepositorio.alterarQuantidade(item.produto.id, item.quantidade + 1)

    fun diminuir(item: ItemCarrinho) =
        CarrinhoRepositorio.alterarQuantidade(item.produto.id, item.quantidade - 1)

    fun remover(item: ItemCarrinho) =
        CarrinhoRepositorio.remover(item.produto.id)

    fun limpar() = CarrinhoRepositorio.limpar()
}
