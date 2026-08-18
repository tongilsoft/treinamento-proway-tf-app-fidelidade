package com.treinamento.app_fidelidade.data.remote.dto.response

data class Endereco(
    val logradouro: String,
    val numero: String,
    val bairro: String,
    val cidade: String,
    val uf: String,
    val cep: String
)
