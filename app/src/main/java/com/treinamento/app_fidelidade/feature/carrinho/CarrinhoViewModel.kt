package com.treinamento.app_fidelidade.feature.carrinho

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.repository.SaldoPontosRepositorio
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class CarrinhoUiState(
    val carregando: Boolean = true,
    val itens: List<ItemCarrinho> = emptyList(),
    val saldoPontos: Long = 0,
    /** False quando o saldo ainda nao veio da API (offline, por exemplo). */
    val saldoConhecido: Boolean = false
) {
    // Tudo calculado aqui: muda a quantidade, o total muda junto (tempo real).
    val vazio: Boolean get() = itens.isEmpty()
    val totalItens: Int get() = itens.sumOf { it.quantidade }
    val totalPontos: Long get() = itens.sumOf { it.totalPontos }
    val saldoAposResgate: Long get() = saldoPontos - totalPontos

    /** Sem saldo conhecido nao da para afirmar que falta ponto, entao nao bloqueia. */
    val saldoSuficiente: Boolean get() = !saldoConhecido || saldoAposResgate >= 0
    val podeContinuar: Boolean get() = !vazio && saldoSuficiente
}

class CarrinhoViewModel : ViewModel() {

    val uiState: StateFlow<CarrinhoUiState> = combine(
        CarrinhoRepositorio.itens,
        SaldoPontosRepositorio.saldo
    ) { itens, saldo ->
        CarrinhoUiState(
            carregando = false,
            itens = itens,
            saldoPontos = saldo ?: 0,
            saldoConhecido = saldo != null
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CarrinhoUiState())

    init {
        atualizarSaldo()
    }

    fun atualizarSaldo() = viewModelScope.launch {
        SaldoPontosRepositorio.atualizar()
    }

    fun aumentar(item: ItemCarrinho) =
        CarrinhoRepositorio.alterarQuantidade(item.produto.id, item.quantidade + 1)

    fun diminuir(item: ItemCarrinho) =
        CarrinhoRepositorio.alterarQuantidade(item.produto.id, item.quantidade - 1)

    fun remover(item: ItemCarrinho) =
        CarrinhoRepositorio.remover(item.produto.id)

    fun limpar() = CarrinhoRepositorio.limpar()
}
