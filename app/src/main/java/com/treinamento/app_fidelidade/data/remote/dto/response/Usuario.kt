package com.treinamento.app_fidelidade.data.remote.dto.response


import java.math.BigInteger

data class Usuario(
    val id: BigInteger,
    val name: String,
    val email: String,
    val pontosSaldo: BigInteger,
    val qrCode: String,
    val token: String
)

