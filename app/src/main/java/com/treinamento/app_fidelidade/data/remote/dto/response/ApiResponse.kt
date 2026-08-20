package com.treinamento.app_fidelidade.data.remote.dto.response

data class ApiResponse<T>(
    val success: Boolean,
    val message: String? = null,
    val data: T? = null
)
