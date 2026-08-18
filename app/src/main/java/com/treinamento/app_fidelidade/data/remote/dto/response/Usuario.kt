package com.treinamento.app_fidelidade.data.remote.dto.response


import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Usuario(
    @SerializedName("idUsuario")
    val id: BigInteger,
    val name: String,
    val email: String,
    val pontosSaldo: BigInteger,
    val qrCode: String,
    val token: String? = null,
    val pontosUtilizados: BigInteger? = null,
    val totalPontosGanhos: BigInteger? = null,
    val nivelMembro: String? = null
)

