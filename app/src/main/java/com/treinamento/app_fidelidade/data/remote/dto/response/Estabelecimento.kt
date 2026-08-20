package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigDecimal
import java.math.BigInteger

data class Estabelecimento(
    val id: BigInteger,
    val name: String,
    val endereco: String,
    val latitude: BigDecimal,
    val longitude: BigDecimal,
    // campos extras que GET /api/parceiros devolve
    val telefone: String? = null,
    val distancia: String? = null,
    // datas chegam como texto ISO; o Gson padrao nao converte LocalDateTime
    val createdAt: String? = null,
    val updatedAt: String? = null
)
