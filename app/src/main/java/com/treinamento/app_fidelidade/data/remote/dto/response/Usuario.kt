package com.treinamento.app_fidelidade.data.remote.dto.response

import com.google.gson.annotations.SerializedName
import java.math.BigInteger

/**
 * Usuario devolvido por /auth/login, /auth/cadastro e /usuarios/meusDados.
 *
 * Os tres endpoints NAO mandam os mesmos campos, e por isso o que varia e nulavel:
 * - "token" so vem no login e no cadastro (meusDados nao abre sessao);
 * - "senha" so vem no meusDados, que devolve o registro inteiro.
 *
 * O Gson monta este objeto por reflexao e nao respeita o nao-nulavel do Kotlin: um
 * campo ausente vira null mesmo declarado obrigatorio, e o app so quebra la na frente,
 * quando alguem passa esse null adiante. Declarar a verdade do JSON evita isso.
 */
data class Usuario(
    // A API manda "id"; "idUsuario" fica como alternativa para nao quebrar
    // se algum endpoint usar o outro nome.
    @SerializedName(value = "id", alternate = ["idUsuario"])
    val id: Long,
    val name: String,
    val senha: String? = null,
    val email: String,
    val pontosSaldo: Long,
    val qrCode: String,
    val token: String? = null,
    val pontosUtilizados: BigInteger? = null,
    val totalPontosGanhos: BigInteger? = null,
    val nivelMembro: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
