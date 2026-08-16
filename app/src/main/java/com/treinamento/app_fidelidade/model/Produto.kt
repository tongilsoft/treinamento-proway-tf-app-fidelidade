package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime

data class Produto(
    val id: Long,
    val nome: String,
    val descricao: String,
    val valorPontos: Long,
    val categoria: String,
    val relevancia: Int,
//    val idCategoria: Long,
    val imagemUrl: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    val disponivel: Boolean = true

    fun pontosFaltantes(pontosAtuais: Long): Long =
        (valorPontos - pontosAtuais).coerceAtLeast(0)
}
