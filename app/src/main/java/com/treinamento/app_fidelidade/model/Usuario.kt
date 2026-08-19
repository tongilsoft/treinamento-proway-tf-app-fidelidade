package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime

data class Usuario(
    val id: Long,
    val nome: String,
    val email: String,
    val pontosSaldo: Long,
    val token: String,
    val qrCode: String,
    val urlImage: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
