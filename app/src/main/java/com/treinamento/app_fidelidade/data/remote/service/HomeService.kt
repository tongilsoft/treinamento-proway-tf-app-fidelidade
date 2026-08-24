package com.treinamento.app_fidelidade.data.remote.service

import com.treinamento.app_fidelidade.data.remote.api.FidelidadeApi
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.PontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario

class HomeService (
    private val api: FidelidadeApi
) {

    suspend fun saldoPontos(): PontosResponse {
        return api.getSaldoPontos().data
    }

    suspend fun extrato(): List<MovimentacaoResponse> {
        return api.getExtrato().data
    }
    suspend fun meusDados(): Usuario {
        return api.getMeusDados().data
    }
}