package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDateTime

data class Estabelecimento(
    val id: BigInteger,
    val name: String,
    val endereco: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)