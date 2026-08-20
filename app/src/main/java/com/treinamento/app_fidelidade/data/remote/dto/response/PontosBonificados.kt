package com.treinamento.app_fidelidade.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

data class PontosBonificados(
    val idUsuario: BigInteger,
    val idEstabelecimento: BigInteger,
    // o mock envia a chave como "pontosBonificados"
    @SerializedName(value = "pontosBonificados", alternate = ["pontos"])
    val pontos: BigInteger,
    // campos extras que POST /api/estabelecimento/{id}/bonificarPontos devolve
    val idProduto: BigInteger? = null,
    val pontosSaldoAnterior: BigInteger? = null,
    val pontosSaldoAtual: BigInteger? = null,
    val nomeEstabelecimento: String? = null,
    val motivo: String? = null,
    // datas chegam como texto ISO; o Gson padrao nao converte LocalDateTime
    val createdAt: String? = null,
    val updatedAt: String? = null
)
