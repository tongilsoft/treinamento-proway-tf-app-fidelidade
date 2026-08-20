package com.treinamento.app_fidelidade.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Usuario(
    @SerializedName("idUsuario")
    val id: Long,
    val name: String,
    val senha: String,
    val email: String,
    val pontosSaldo: Long,
    val qrCode: String,
    val token: String,
    val pontosUtilizados: BigInteger? = null,
    val totalPontosGanhos: BigInteger? = null,
    val nivelMembro: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)

