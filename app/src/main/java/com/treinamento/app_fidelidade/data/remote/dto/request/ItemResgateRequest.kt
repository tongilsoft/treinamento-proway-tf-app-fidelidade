package com.treinamento.app_fidelidade.data.remote.dto.request

import java.math.BigInteger

/** Uma linha do resgate. Quantidade ausente no JSON vira 1 no mock. */
data class ItemResgateRequest(
    val idProduto: BigInteger,
    val quantidade: BigInteger = BigInteger.ONE
)
