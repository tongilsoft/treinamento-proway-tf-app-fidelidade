package com.treinamento.app_fidelidade.data.remote.dto.response

/**
 * Corpo devolvido pelo mock nos erros (400 / 401 / 404).
 * Use com response.errorBody() para ler a mensagem antes de mostrar ao usuario.
 */
data class ErroResponse(
    val success: Boolean,
    val message: String
)
