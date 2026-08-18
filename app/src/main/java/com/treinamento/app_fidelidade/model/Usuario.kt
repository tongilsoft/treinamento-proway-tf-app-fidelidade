package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime

data class Usuario(
    val id: Long,
    val nome: String,
    val email: String,
    val password: String? = null,
    val pontosSaldo: Long,
    val qrCode: String,
    val urlImage: String? = null,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
)
