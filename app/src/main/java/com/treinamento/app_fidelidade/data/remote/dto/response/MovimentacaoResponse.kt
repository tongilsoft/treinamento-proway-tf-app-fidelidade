package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger
import java.time.LocalDateTime

data class MovimentacaoResponse(
    val id: BigInteger?,
    val idUsuario: BigInteger?,
    val idProduto: BigInteger?,
    val tipo: String?,
    val valorPontos: BigInteger?,
    val descricao: String?,
    val data: String?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
)