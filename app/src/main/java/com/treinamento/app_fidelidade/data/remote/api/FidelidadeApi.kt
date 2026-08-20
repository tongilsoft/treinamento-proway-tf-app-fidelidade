package com.treinamento.app_fidelidade.data.remote.api

import com.treinamento.app_fidelidade.data.remote.dto.response.ApiResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.UsuarioResponse
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRequest
import com.treinamento.app_fidelidade.data.remote.dto.response.MeusDadosResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface FidelidadeApi {

    @POST("auth/login")
    suspend fun doLogin(
        @Body usuario: UsuarioRequest
    ): ApiResponse<UsuarioResponse>

    @POST("auth/login")
    suspend fun doLogin(
        @Body usuario: UsuarioRegistro
    ): ApiResponse<UsuarioResponse>

    @GET("usuarios/meusDados")
    suspend fun getMeusDados(): ApiResponse<MeusDadosResponse>

}