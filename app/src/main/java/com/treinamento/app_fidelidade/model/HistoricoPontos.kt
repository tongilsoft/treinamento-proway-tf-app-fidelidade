package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime


data class HistoricoPontos(
    val id: BigInteger,
    val idEstabelecimento: BigInteger,
    val pontoGasto: BigInteger,
    val idProduto: BigInteger,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
