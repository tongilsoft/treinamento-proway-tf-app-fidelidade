package com.treinamento.app_fidelidade.data.remote.dto.request


/** Campos nulos nao vao no JSON (Gson omite null), entao o mock preserva o valor atual. */
data class EnderecoRequest(
    val logradouro: String? = null,
    val numero: String? = null,
    val bairro: String? = null,
    val cidade: String? = null,
    val uf: String? = null,
    val cep: String? = null
)
