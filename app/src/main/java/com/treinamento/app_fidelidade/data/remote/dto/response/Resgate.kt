package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

/**
 * Resgate criado por POST /api/resgate. "status" chega como "pendente".
 * O resgate pode ter um ou varios itens; pontosUtilizados e a soma de valorPontosTotal de todos eles.
 */
data class Resgate(
    val idResgate: BigInteger,
    val idUsuario: BigInteger,
    // quantidade de linhas do resgate (nao a soma das quantidades)
    val quantidadeItens: BigInteger,
    val itens: List<ItemResgate>,
    val pontosUtilizados: BigInteger,
    val pontosSaldoAnterior: BigInteger,
    val pontosSaldoAtual: BigInteger,
    val status: String,
    val dataCriacao: String,
    val dataConfirmacao: String?,
    val createdAt: String,
    val updatedAt: String
)
