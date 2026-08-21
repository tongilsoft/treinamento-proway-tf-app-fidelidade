package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

/**
 * Resgate criado por POST /api/resgate.
 *
 * Tudo aqui e nulavel de proposito. O Gson cria o objeto por reflexao e nao respeita
 * o nao-nulavel do Kotlin: se o servidor omitir um campo, o valor chega null mesmo
 * declarado como obrigatorio, e o app estoura so quando alguem for usar. Como o
 * contrato do backend ainda esta mudando, o tipo reflete o que o JSON pode trazer.
 */
data class Resgate(
    val idResgate: BigInteger? = null,
    val idUsuario: BigInteger? = null,
    // quantidade de linhas do resgate (nao a soma das quantidades)
    val quantidadeItens: BigInteger? = null,
    val itens: List<ItemResgate>? = null,
    val pontosUtilizados: BigInteger? = null,
    val pontosSaldoAnterior: BigInteger? = null,
    val pontosSaldoAtual: BigInteger? = null,
    val status: String? = null,
    val dataCriacao: String? = null,
    val dataConfirmacao: String? = null,
    val createdAt: String? = null,
    val updatedAt: String? = null
)
