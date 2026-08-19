package com.treinamento.app_fidelidade.data.remote.dto.response

import java.time.LocalDateTime


data class Usuario(
    val id: Long,
    val name: String,
    val senha: String,
    val email: String,
    val pontosSaldo: Long,
    val qrCode: String,
    val token: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

