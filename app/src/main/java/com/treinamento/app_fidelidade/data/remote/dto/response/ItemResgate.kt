package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

/** Linha do resgate ja com o preco resolvido pelo servidor. */
data class ItemResgate(
    val idProduto: BigInteger,
    val nomeProduto: String,
    val quantidade: BigInteger,
    val valorPontosUnitario: BigInteger,
    val valorPontosTotal: BigInteger
)
