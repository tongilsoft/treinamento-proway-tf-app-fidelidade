package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

data class TransferenciaPontos(
    val idUsuarioOrigem: BigInteger,
    val nomeUsuarioOrigem: String,
    val idUsuarioDestino: BigInteger,
    val nomeUsuarioDestino: String,
    val emailDestino: String,
    val pontosTransferidos: BigInteger,
    val pontosSaldoAnteriorOrigem: BigInteger,
    val pontosSaldoAtualOrigem: BigInteger,
    val createdAt: String
)
