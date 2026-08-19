package com.treinamento.app_fidelidade.data.remote.dto.response


data class TransferenciaPontosResponse(
    val success: Boolean,
    val message: String,
    val data: TransferenciaPontos
)
