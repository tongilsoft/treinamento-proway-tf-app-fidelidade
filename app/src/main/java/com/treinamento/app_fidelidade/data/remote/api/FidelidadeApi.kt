package com.treinamento.app_fidelidade.data.remote.api

import com.treinamento.app_fidelidade.data.remote.dto.request.AtualizarUsuarioRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.BonificarPontosRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.ResgateRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.TransferenciaPontosRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioLoginRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.response.ApiResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.AtualizarUsuarioResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.AuthResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Estabelecimento
import com.treinamento.app_fidelidade.data.remote.dto.response.ExtratoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.MeusDadosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.MovimentacaoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.ParceirosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.PontosBonificadosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.PontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Produto
import com.treinamento.app_fidelidade.data.remote.dto.response.ProdutoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.ResgateResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.SaldoPontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.TransferenciaPontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import java.math.BigInteger
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

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

    @PUT("usuarios/meusDados")
    suspend fun atualizarMeusDados(
        @Body usuario: AtualizarUsuarioRequest
    ): AtualizarUsuarioResponse

    @GET("pontos")
    suspend fun getSaldoPontos(): ApiResponse<PontosResponse>

    @GET("pontos/extrato")
    suspend fun getExtrato(): ApiResponse<List<MovimentacaoResponse>>

    @POST("pontos/transferir")
    suspend fun transferirPontos(
        @Body transferencia: TransferenciaPontosRequest
    ): TransferenciaPontosResponse

    @GET("parceiros")
    suspend fun getParceiros(): ApiResponse<List<Estabelecimento>>

    @GET("produto")
    suspend fun getProdutos(): ApiResponse<List<Produto>>

    @POST("resgate")
    suspend fun criarResgate(
        @Body resgate: ResgateRequest
    ): ResgateResponse

    @POST("estabelecimento/{id}/bonificarPontos")
    suspend fun bonificarPontos(
        @Path("id") idEstabelecimento: BigInteger,
        @Body bonificacao: BonificarPontosRequest
    ): PontosBonificadosResponse
}

