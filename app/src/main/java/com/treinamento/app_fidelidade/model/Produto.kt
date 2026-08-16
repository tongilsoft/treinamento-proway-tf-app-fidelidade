package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime

data class Produto(
    val id: BigInteger,
    val name: String,
    val descricao: String,
    val valorPontos: Long,
    val idCategoria: BigInteger,
    val categoria: String,
    val imagemUrl: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    val disponivel: Boolean = true

    fun pontosFaltantes(pontosAtuais: Long): Long =
        (valorPontos - pontosAtuais).coerceAtLeast(0L)
}
