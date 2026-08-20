package com.treinamento.app_fidelidade.repository

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Contrato de conexao.
 *
 * O ViewModel pergunta "estou online?" para esta interface, nao para o Android.
 * Por isso ele continua testavel: no teste passamos uma implementacao que responde
 * false e conferimos se o resgate virou pendente, sem precisar de emulador.
 */
interface Conexao {
    val online: StateFlow<Boolean>
    fun estaOnline(): Boolean
}

/**
 * Implementacao MOCKADA. Na integracao final vira ConnectivityManager.
 * Por enquanto o botao de wifi da AppBar liga/desliga, so para demonstrar
 * os dois fluxos (com e sem internet).
 */
object ConexaoMock : Conexao {

    private val _online = MutableStateFlow(true)
    override val online: StateFlow<Boolean> = _online.asStateFlow()

    override fun estaOnline(): Boolean = _online.value

    fun alternar() {
        _online.value = !_online.value
    }
}
