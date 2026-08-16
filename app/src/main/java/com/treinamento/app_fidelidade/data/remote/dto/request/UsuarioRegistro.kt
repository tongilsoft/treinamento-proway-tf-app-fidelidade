package com.treinamento.app_fidelidade.data.remote.dto.request

import java.math.BigInteger
import java.time.LocalDateTime

data class UsuarioRegistro(
    val id: BigInteger?,
    val name: String,
    val email: String,
    val password: String,
    val pontosSaldo: BigInteger = BigInteger.ZERO,
    val qrCode: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)
