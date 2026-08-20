package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

data class MovimentacaoResponse(
    val id: BigInteger,
    val idUsuario: BigInteger,
    val idProduto: BigInteger? = null,
    // Os tres campos abaixo so vem em movimentacao de resgate.
    // Credito (bonificacao) e transferencia chegam sem eles, por isso sao nulaveis.
    val idResgate: BigInteger? = null,
    val nomeProduto: String? = null,
    val quantidade: BigInteger? = null,
    val tipo: String, // "credito" or "debito"
    val valorPontos: BigInteger,
    val descricao: String,
    val data: String,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
