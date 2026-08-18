package com.treinamento.app_fidelidade.data.remote.dto.request

import java.math.BigInteger

/** Corpo de POST /api/estabelecimento/{id}/bonificarPontos. */
data class BonificarPontosRequest(
    val idProduto: BigInteger?,
    val pontosBonificados: BigInteger
)
