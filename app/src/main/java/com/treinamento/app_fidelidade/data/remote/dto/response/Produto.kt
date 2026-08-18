package com.treinamento.app_fidelidade.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class Produto(
    val id: BigInteger,
    val name: String,
    val descricao: String,
    val valorPontos: BigInteger,
    val idCategoria: BigInteger,
    // datas chegam como texto ISO; o Gson padrao nao converte LocalDateTime
    val createdAt: String? = null,
    // o catalogo do mock envia a chave como "updateAt", sem o "d"
    @SerializedName(value = "updatedAt", alternate = ["updateAt"])
    val updatedAt: String? = null
)
