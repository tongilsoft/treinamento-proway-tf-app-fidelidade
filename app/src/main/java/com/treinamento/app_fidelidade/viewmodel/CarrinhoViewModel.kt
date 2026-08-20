package com.treinamento.app_fidelidade.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.treinamento.app_fidelidade.di.AppContainer
import com.treinamento.app_fidelidade.model.ItemCarrinho
import com.treinamento.app_fidelidade.repository.CarrinhoRepository
import com.treinamento.app_fidelidade.repository.SaldoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ESTADO DA TELA (o "VM" do MVVM entrega isto para a View).
 *
 * A tela nao calcula nada: ela recebe este objeto pronto e desenha. Repare que
 * total, saldo apos o resgate e "pode continuar" sao derivados aqui — se essa conta
 * estivesse dentro do Composable, ela seria refeita a cada recomposicao e nao daria
 * para testar sem subir a tela.
 */
data class CarrinhoUiState(
    val carregando: Boolean = true,
    val itens: List<ItemCarrinho> = emptyList(),
    val saldoPontos: Long = 0,
    /** False quando o saldo ainda nao veio da API (offline, por exemplo). */
    val saldoConhecido: Boolean = false
) {
    val vazio: Boolean get() = itens.isEmpty()
    val totalItens: Int get() = itens.sumOf { it.quantidade }
    val totalPontos: Long get() = itens.sumOf { it.totalPontos }
    val saldoAposResgate: Long get() = saldoPontos - totalPontos

    /** Sem saldo conhecido nao da para afirmar que falta ponto, entao nao bloqueia. */
    val saldoSuficiente: Boolean get() = !saldoConhecido || saldoAposResgate >= 0
    val podeContinuar: Boolean get() = !vazio && saldoSuficiente
}

/**
 * VIEWMODEL do carrinho.
 *
 * Recebe as duas dependencias pelo construtor e nao instancia nada: nao conhece
 * Retrofit, nao conhece o AppContainer, nao importa nada de Compose. Tudo o que ele
 * faz e transformar as fontes de dados em [CarrinhoUiState] e repassar as acoes do
 * usuario para o repositorio.
 */
class CarrinhoViewModel(
    private val carrinhoRepository: CarrinhoRepository,
    private val saldoRepository: SaldoRepository
) : ViewModel() {

    val uiState: StateFlow<CarrinhoUiState> = combine(
        carrinhoRepository.itens,
        saldoRepository.saldo
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
        saldoRepository.atualizar()
    }

    fun aumentar(item: ItemCarrinho) =
        carrinhoRepository.alterarQuantidade(item.produto.id, item.quantidade + 1)

    fun diminuir(item: ItemCarrinho) =
        carrinhoRepository.alterarQuantidade(item.produto.id, item.quantidade - 1)

    fun remover(item: ItemCarrinho) =
        carrinhoRepository.remover(item.produto.id)

    fun limpar() = carrinhoRepository.limpar()

    companion object {
        /**
         * A Factory e a unica parte que conhece o AppContainer. E a fronteira: o
         * Android exige construtor vazio, a Factory resolve isso sem obrigar o
         * ViewModel a ir buscar as proprias dependencias.
         */
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                CarrinhoViewModel(
                    carrinhoRepository = AppContainer.carrinhoRepository,
                    saldoRepository = AppContainer.saldoRepository
                )
            }
        }
    }
}
