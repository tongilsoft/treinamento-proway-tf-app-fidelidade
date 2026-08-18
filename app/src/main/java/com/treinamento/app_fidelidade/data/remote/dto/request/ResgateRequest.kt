package com.treinamento.app_fidelidade.data.remote.dto.request

import java.math.BigInteger

/**
 * Corpo de POST /api/resgate. O total em pontos e somado no servidor
 * (valorPontos do produto x quantidade de cada linha).
 */
data class ResgateRequest(
    val itens: List<ItemResgateRequest>
) {
    companion object {
        /** Atalho para o resgate de um unico produto. */
        fun deUmProduto(idProduto: BigInteger, quantidade: BigInteger = BigInteger.ONE) =
            ResgateRequest(listOf(ItemResgateRequest(idProduto, quantidade)))
    }
}
