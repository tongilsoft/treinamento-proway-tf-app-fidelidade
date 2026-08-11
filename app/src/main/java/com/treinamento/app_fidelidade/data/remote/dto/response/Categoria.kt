package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger
import java.time.LocalDateTime

data class Categoria(
    val id: BigInteger,
    val name: String,
    val descricao: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)