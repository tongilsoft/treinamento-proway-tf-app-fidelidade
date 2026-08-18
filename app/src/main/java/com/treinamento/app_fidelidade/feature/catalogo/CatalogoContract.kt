package com.treinamento.app_fidelidade.feature.catalogo

import com.treinamento.app_fidelidade.model.Produto
import java.math.BigInteger

//enum class OrdenacaoCatalogo { MAIS_RELEVANTES, MENOR_PONTUACAO, MAIOR_PONTUACAO, NOME }

data class CatalogoUiState(
    val carregando: Boolean = true,
    val atualizando: Boolean = false,
    val produtos: List<Produto> = emptyList(),
    val categorias: List<String> = emptyList(),
    val pontosAtuais: Long = 0,
    val busca: String = "",
//    val ordenacao: OrdenacaoCatalogo = OrdenacaoCatalogo.MAIS_RELEVANTES,
    val categoriaSelecionada: String? = null,
    val quantidadeCarrinho: Int = 0,
    val produtoSelecionado: Produto? = null,
    val quantidadeSelecionada: Int = 1,
    val offline: Boolean = false,
    val mensagem: String? = null
) {
    val quantidadeFiltrosAtivos: Int
        get() = listOf(
            categoriaSelecionada != null,
//            ordenacao != OrdenacaoCatalogo.MAIS_RELEVANTES
        ).count { it }
}

sealed interface CatalogoEvent {
    data class Buscar(val texto: String) : CatalogoEvent
    data class SelecionarProduto(val id: Long) : CatalogoEvent
    data class AjustarQuantidade(val novaQuantidade: Int) : CatalogoEvent
    data class AdicionarAoCarrinho(val id: Long, val quantidade: Int) : CatalogoEvent
//    data class AplicarFiltros(val ordenacao: OrdenacaoCatalogo, val categoria: String?) : CatalogoEvent
//    data object LimparFiltros : CatalogoEvent
    data object LimparMensagem : CatalogoEvent
    data object Atualizar : CatalogoEvent
}
