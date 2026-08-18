package com.treinamento.app_fidelidade.data.remote.api

import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioLoginRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.response.ApiResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.AuthResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Estabelecimento
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.PontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Produto
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import retrofit2.http.Body
import retrofit2.http.GET
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

    @GET("usuarios/meusDados")
    suspend fun getMeusDados(): ApiResponse<Usuario>

    @GET("produto")
    suspend fun getProdutos(): ApiResponse<List<Produto>>

    @GET("pontos")
    suspend fun getSaldoPontos(): ApiResponse<PontosResponse>

    @GET("pontos/extrato")
    suspend fun getExtrato(): ApiResponse<List<MovimentacaoResponse>>

    @GET("parceiros")
    suspend fun getParceiros(): ApiResponse<List<Estabelecimento>>
}
