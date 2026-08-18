package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

data class SaldoPontos(
    val pontosSaldo: BigInteger,
    val pontosUtilizados: BigInteger,
    val totalPontosGanhos: BigInteger,
    val createdAt: String?,
    val updatedAt: String?
)
