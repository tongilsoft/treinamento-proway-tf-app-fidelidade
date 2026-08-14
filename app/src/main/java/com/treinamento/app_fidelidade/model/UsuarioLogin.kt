package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime


data class UsuarioLogin(
    val email: String,
    val senha: String
)