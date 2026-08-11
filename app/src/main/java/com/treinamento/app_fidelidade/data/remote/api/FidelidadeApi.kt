package com.treinamento.app_fidelidade.data.remote.api

import com.treinamento.app_fidelidade.data.remote.dto.response.UsuarioResponse
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface FidelidadeApi {

    @POST("auth/login")
    suspend fun doLogin(
        @Body usuario: UsuarioRequest
    ): UsuarioResponse

    @POST("auth/login")
    suspend fun doLogin(
        @Body usuario: UsuarioRegistro
    ): UsuarioResponse



}