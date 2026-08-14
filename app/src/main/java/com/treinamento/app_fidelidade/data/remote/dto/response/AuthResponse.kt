package com.treinamento.app_fidelidade.data.remote.dto.response

data class AuthResponse (
    val success: Boolean,
    val message : String,
    val data : Usuario
)