package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

data class PontosResponse(
    val pontosSaldo: BigInteger,
    val pontosUtilizados: BigInteger,
    val totalPontosGanhos: BigInteger,
    val pontosEmValidade: List<PontoValidade>? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

data class PontoValidade(
    val valor: BigInteger,
    val dataExpiracao: String
)
