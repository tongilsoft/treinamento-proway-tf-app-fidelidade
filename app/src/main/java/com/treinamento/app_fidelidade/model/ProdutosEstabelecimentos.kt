package com.treinamento.app_fidelidade.model

import java.math.BigInteger
import java.time.LocalDateTime

data class ProdutosEstabelecimentos(
    val id: BigInteger,
    val idProduto: BigInteger,
    val idEstabelecimento: BigInteger,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
