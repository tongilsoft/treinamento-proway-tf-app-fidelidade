package com.treinamento.app_fidelidade.model

import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario

data class AuthenticationData (
    val success: Boolean,
    val message : String,
    val data : Usuario
)