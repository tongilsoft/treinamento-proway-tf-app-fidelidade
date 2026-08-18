package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger
import java.time.LocalDateTime

data class PontosBonificados(
    val idUsuario: BigInteger,
    val idEstabelecimento: BigInteger,
    val pontos: BigInteger,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)