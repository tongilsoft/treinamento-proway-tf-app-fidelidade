package com.treinamento.app_fidelidade.data.remote.api

import com.treinamento.app_fidelidade.data.remote.dto.request.AtualizarUsuarioRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.BonificarPontosRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.ResgateRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.TransferenciaPontosRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioLoginRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.response.AtualizarUsuarioResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.AuthResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.ExtratoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.MeusDadosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.ParceirosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.PontosBonificadosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.ProdutoResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.ResgateResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.SaldoPontosResponse
import com.treinamento.app_fidelidade.data.remote.dto.response.TransferenciaPontosResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import java.math.BigInteger

interface FidelidadeApi {

    @POST("auth/login")
    suspend fun doLogin(
        @Body usuario: UsuarioLoginRequest
    ): AuthResponse

    @POST("auth/cadastro")
    suspend fun cadastrar(
        @Body usuario: UsuarioRegistro
    ): AuthResponse

    /** Cadastro completo do usuario logado. */
    @GET("usuarios/meusDados")
    suspend fun buscarMeusDados(): MeusDadosResponse

    /**
     * Altera os dados do usuario logado. Funciona como PATCH: os campos nulos
     * nao sao enviados e o servidor mantem o valor atual deles.
     */
    @PUT("usuarios/meusDados")
    suspend fun atualizarMeusDados(
        @Body usuario: AtualizarUsuarioRequest
    ): AtualizarUsuarioResponse

    /** Saldo, pontos utilizados e total ganho do usuario logado. */
    @GET("pontos")
    suspend fun buscarSaldoPontos(): SaldoPontosResponse

    /** Extrato de creditos e debitos do usuario logado. */
    @GET("pontos/extrato")
    suspend fun buscarExtrato(): ExtratoResponse

    /** Transfere pontos do usuario logado para outro usuario, identificado pelo email. */
    @POST("pontos/transferir")
    suspend fun transferirPontos(
        @Body transferencia: TransferenciaPontosRequest
    ): TransferenciaPontosResponse

    /** Farmacias parceiras. */
    @GET("parceiros")
    suspend fun listarParceiros(): ParceirosResponse

    /** Catalogo de produtos disponiveis para resgate. */
    @GET("produto")
    suspend fun listarProdutos(): ProdutoResponse

    /**
     * Cria um resgate de um ou varios itens debitando os pontos do usuario logado.
     * O total e calculado no servidor a partir do preco de cada produto.
     */
    @POST("resgate")
    suspend fun criarResgate(
        @Body resgate: ResgateRequest
    ): ResgateResponse

    /** Credita pontos ao usuario logado por uma compra no estabelecimento informado. */
    @POST("estabelecimento/{id}/bonificarPontos")
    suspend fun bonificarPontos(
        @Path("id") idEstabelecimento: BigInteger,
        @Body bonificacao: BonificarPontosRequest
    ): PontosBonificadosResponse
}
