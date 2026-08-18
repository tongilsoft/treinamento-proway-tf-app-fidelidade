package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

data class Produto(
    val id: BigInteger,
    val name: String,
    val descricao: String,
    val valorPontos: BigInteger,
    val idCategoria: BigInteger,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
