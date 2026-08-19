package com.treinamento.app_fidelidade.model

import com.treinamento.app_fidelidade.model.Produto
import java.text.NumberFormat
import java.util.Locale

/**
 * MODEL do carrinho.
 *
 * Classe de dominio: nao conhece Compose, nao conhece Retrofit, nao conhece Android.
 * E so o produto escolhido e quantas unidades o usuario quer resgatar.
 */
data class ItemCarrinho(
    val produto: Produto,
    val quantidade: Int
) {
    val totalPontos: Long get() = produto.valorPontos * quantidade
}

/** Deixa o numero no padrao do design: 13123678 -> "13.123.678". */
fun Long.formatarPontos(): String =
    NumberFormat.getIntegerInstance(Locale.forLanguageTag("pt-BR")).format(this)
