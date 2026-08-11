package com.treinamento.app_fidelidade.model

data class Produto(
    val id: Long,
    val nome: String,
    val descricao: String,
    val valorPontos: Long,
    val categoria: String,
    val relevancia: Int,
    val imagemUrl: String? = null
) {
    val disponivel: Boolean = true

    fun pontosFaltantes(pontosAtuais: Long): Long =
        (valorPontos - pontosAtuais).coerceAtLeast(0L)
}
