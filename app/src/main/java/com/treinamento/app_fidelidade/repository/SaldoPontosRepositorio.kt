package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.data.remote.service.ResgateService
import com.treinamento.app_fidelidade.data.remote.service.ResultadoApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Saldo de pontos do usuario logado, vindo de GET /api/pontos.
 *
 * E um object porque carrinho, confirmacao de resgate e catalogo precisam enxergar
 * o mesmo saldo: quando o resgate e concluido o servidor devolve o saldo novo e
 * todas as telas acompanham na hora.
 */
object SaldoPontosRepositorio {

    private val service = ResgateService()

    private val _saldo = MutableStateFlow<Long?>(null)

    /** Null enquanto a primeira carga nao terminou. */
    val saldo: StateFlow<Long?> = _saldo.asStateFlow()

    suspend fun atualizar(): ResultadoApi<Long> {
        return when (val resultado = service.buscarSaldo()) {
            is ResultadoApi.Sucesso -> {
                val valor = resultado.dados.pontosSaldo.toLong()
                _saldo.value = valor
                ResultadoApi.Sucesso(valor)
            }

            ResultadoApi.SemConexao -> ResultadoApi.SemConexao
            is ResultadoApi.Erro -> resultado
        }
    }

    /** Usado logo apos um resgate, com o saldo que o proprio servidor devolveu. */
    fun definir(novoSaldo: Long) {
        _saldo.value = novoSaldo
    }
}
