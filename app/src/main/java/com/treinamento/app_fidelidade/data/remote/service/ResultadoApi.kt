package com.treinamento.app_fidelidade.data.remote.service

import com.google.gson.Gson
import com.treinamento.app_fidelidade.data.remote.dto.response.ErroResponse
import retrofit2.HttpException
import java.io.IOException

/**
 * Resultado de uma chamada da API.
 *
 * [SemConexao] existe separado de [Erro] porque o app trata os dois casos de forma
 * diferente: sem rede o resgate vira pendente para reenviar depois, enquanto um erro
 * de regra (saldo insuficiente, produto inexistente) precisa ser mostrado na hora.
 */
sealed interface ResultadoApi<out T> {
    data class Sucesso<T>(val dados: T) : ResultadoApi<T>
    data object SemConexao : ResultadoApi<Nothing>
    data class Erro(val mensagem: String, val codigo: Int) : ResultadoApi<Nothing>
}

private val gson = Gson()

/**
 * Executa a chamada convertendo as falhas conhecidas:
 * falta de rede vira [ResultadoApi.SemConexao] e resposta 4xx/5xx vira [ResultadoApi.Erro]
 * ja com a mensagem que o backend mandou no corpo.
 */
suspend fun <T> chamarApi(bloco: suspend () -> T): ResultadoApi<T> =
    try {
        ResultadoApi.Sucesso(bloco())
    } catch (e: IOException) {
        ResultadoApi.SemConexao
    } catch (e: HttpException) {
        ResultadoApi.Erro(mensagemDoErro(e), e.code())
    }

private fun mensagemDoErro(e: HttpException): String {
    val corpo = try {
        e.response()?.errorBody()?.string()
    } catch (_: IOException) {
        null
    }

    val mensagem = corpo
        ?.takeIf { it.isNotBlank() }
        ?.let {
            try {
                gson.fromJson(it, ErroResponse::class.java)?.message
            } catch (_: Exception) {
                null
            }
        }

    return mensagem ?: "Nao foi possivel completar a operacao (erro ${e.code()})."
}
