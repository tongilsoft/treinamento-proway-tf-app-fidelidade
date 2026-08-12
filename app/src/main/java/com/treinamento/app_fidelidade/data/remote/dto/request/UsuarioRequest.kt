package com.treinamento.app_fidelidade.data.remote.dto.request


import java.math.BigInteger
import java.time.LocalDateTime

data class UsuarioRequest(
    val name: String,
    val email: String,
    val password: String,
)

