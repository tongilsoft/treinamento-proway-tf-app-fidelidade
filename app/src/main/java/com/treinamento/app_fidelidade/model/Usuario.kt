package com.treinamento.app_fidelidade.model


import java.math.BigInteger
import java.time.LocalDateTime

data class Usuario(
    val id: BigInteger,
    val name: String,
    val email: String,
    val password: String,
    val pontosSaldo: BigInteger,
    val qrCode: String,
    val urlImage: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

