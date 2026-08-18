package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

data class MovimentacaoResponse(
    val id: BigInteger,
    val idUsuario: BigInteger,
    val idProduto: BigInteger? = null,
    val tipo: String, // "credito" or "debito"
    val valorPontos: BigInteger,
    val descricao: String,
    val data: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
