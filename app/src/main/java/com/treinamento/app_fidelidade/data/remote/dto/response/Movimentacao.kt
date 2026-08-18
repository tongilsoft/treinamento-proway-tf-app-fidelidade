package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

/**
 * Item do extrato de pontos. "tipo" e "credito" ou "debito".
 * idProduto vem nulo quando a movimentacao e uma transferencia entre usuarios.
 */
data class Movimentacao(
    val id: BigInteger,
    val idUsuario: BigInteger,
    val idProduto: BigInteger?,
    val tipo: String,
    val valorPontos: BigInteger,
    val descricao: String,
    val data: String,
    val createdAt: String,
    val updatedAt: String
)
