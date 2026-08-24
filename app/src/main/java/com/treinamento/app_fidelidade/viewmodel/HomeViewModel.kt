package com.treinamento.app_fidelidade.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import com.treinamento.app_fidelidade.data.remote.service.HomeService
import com.treinamento.app_fidelidade.data.repository.api.HomeRepository

//meusDados() para nome
//pontos() saldo de pontos
//extrato() para extrato de pontos


class HomeViewModel : ViewModel() {

    private val service = HomeService(
        api = RetrofitInstance.api
    )

    private val repository = HomeRepository(
        service = service
    )

    var saldoPontos by mutableStateOf("")
        private set

    var usuario: Usuario? by mutableStateOf(null)
        private set

    var extrato by mutableStateOf<List<MovimentacaoResponse>>(emptyList())
    private set

    var carregando by mutableStateOf(false)
        private set

    var erro by mutableStateOf<String?>(null)
        private set


    suspend fun carregarHome() {

        usuario = repository.meusDados()

        saldoPontos =
            repository.saldoPontos().pontosSaldo.toString()
        extrato =
        repository.extrato()
    }

    suspend fun meusDados() {
        try {
            carregando = true
            erro = null

            usuario = repository.meusDados()

        } catch (e: Exception) {
            erro = e.message
        } finally {
            carregando = false
        }
    }
}