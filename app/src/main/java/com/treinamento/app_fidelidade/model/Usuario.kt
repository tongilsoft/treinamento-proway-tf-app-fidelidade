package com.treinamento.app_fidelidade.model

data class Usuario(
    val id: Long,
    val nome: String,
    val email: String,
    val endereco: String = "",
    val saldoPontos: Long,
    val qrCode: String
)
