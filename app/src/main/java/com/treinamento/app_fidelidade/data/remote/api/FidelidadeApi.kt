package com.treinamento.app_fidelidade.data.remote.api

import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioLoginRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.response.AuthResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface FidelidadeApi {

    @POST("auth/login")
    suspend fun doLogin(
        @Body usuario: UsuarioLoginRequest
    ): AuthResponse

    @POST("auth/cadastro")
    suspend fun cadastrar(
        @Body usuario: UsuarioRegistro
    ): AuthResponse
}