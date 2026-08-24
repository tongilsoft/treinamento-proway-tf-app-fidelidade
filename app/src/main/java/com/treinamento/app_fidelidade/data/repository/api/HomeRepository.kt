package com.treinamento.app_fidelidade.data.repository.api

import com.treinamento.app_fidelidade.data.remote.service.AuthenticationService
import com.treinamento.app_fidelidade.data.remote.service.HomeService

class HomeRepository (
    private val service: HomeService
) {
    suspend fun saldoPontos() =
        service.saldoPontos()

    suspend fun extrato() =
        service.extrato()
    suspend fun meusDados() = service.meusDados()
}