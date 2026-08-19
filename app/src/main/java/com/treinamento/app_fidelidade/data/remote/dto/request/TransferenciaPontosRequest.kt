package com.treinamento.app_fidelidade.data.remote.dto.request

import java.math.BigInteger

/**
 * Corpo de POST /api/pontos/transferir.
 * O usuario de origem e o que esta logado (definido no /api/auth/login), por isso nao vai no corpo.
 */
data class TransferenciaPontosRequest(
    val emailDestino: String,
    val valorPontos: BigInteger
)
