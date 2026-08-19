package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger
import java.time.LocalDateTime


data class HistoricoPontos(
    val id: BigInteger,
    val idEstabelecimento: BigInteger,
    val pontoGasto: BigInteger,
    val idProduto: BigInteger,
    val createdAt: String,
    val updatedAt: String
)
