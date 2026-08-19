package com.treinamento.app_fidelidade.data.remote.dto.response


data class AtualizarUsuarioResponse(
    val success: Boolean,
    val message: String,
    val data: UsuarioDetalhado
)
