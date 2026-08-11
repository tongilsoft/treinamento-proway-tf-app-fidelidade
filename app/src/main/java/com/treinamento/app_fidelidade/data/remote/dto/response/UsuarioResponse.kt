package com.treinamento.app_fidelidade.data.remote.dto.response


import java.math.BigInteger
import java.time.LocalDateTime

data class UsuarioResponse(
    val id: BigInteger,
    val name: String,
    val email: String,
    val password: String,
    val pontosSaldo: BigInteger,
    val qrCode: String,
    val token: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)

