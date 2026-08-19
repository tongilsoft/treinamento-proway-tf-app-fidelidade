package com.treinamento.app_fidelidade.data.remote.service

import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.data.remote.api.FidelidadeApi
import com.treinamento.app_fidelidade.data.remote.dto.request.ItemResgateRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.ResgateRequest
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.PontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Resgate
import java.math.BigInteger

class ResgateService(
    private val api: FidelidadeApi = RetrofitInstance.api
) {

    /**
     * Envia o carrinho inteiro em uma unica requisicao.
     * O servidor soma o total (preco do produto x quantidade), debita os pontos
     * e cria uma movimentacao no extrato para cada linha.
     */
    suspend fun criarResgate(itens: List<ItemResgateRequest>): ResultadoApi<Resgate> =
        chamarApi { api.criarResgate(ResgateRequest(itens)).data }

    suspend fun criarResgate(idProduto: Long, quantidade: Int): ResultadoApi<Resgate> =
        criarResgate(
            listOf(
                ItemResgateRequest(
                    idProduto = BigInteger.valueOf(idProduto),
                    quantidade = BigInteger.valueOf(quantidade.toLong())
                )
            )
        )

    suspend fun buscarSaldo(): ResultadoApi<PontosResponse> =
        chamarApi { api.getSaldoPontos().data }

    /** Extrato completo: creditos, resgates e transferencias juntos. */
    suspend fun buscarExtrato(): ResultadoApi<List<MovimentacaoResponse>> =
        chamarApi { api.getExtrato().data }
}
