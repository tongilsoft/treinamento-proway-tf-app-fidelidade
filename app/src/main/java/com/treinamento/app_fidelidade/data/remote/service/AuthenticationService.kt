package com.treinamento.app_fidelidade.data.remote.service

import com.treinamento.app_fidelidade.data.remote.api.FidelidadeApi
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioLoginRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.model.AuthenticationData

class AuthenticationService(
    private val api: FidelidadeApi
) {

    suspend fun doLogin(
        usuarioLogin: UsuarioLoginRequest
    ): AuthenticationData {
        val responseLogin = api.doLogin(usuarioLogin)

        return AuthenticationData(
            success = responseLogin.success,
            message = responseLogin.message,
            data = responseLogin.data
        )
    }

    suspend fun doRegister(
        usuarioRegistro: UsuarioRegistro
    ): AuthenticationData {
        val responseLogin = api.cadastrar(usuarioRegistro)

        return AuthenticationData(
            success = responseLogin.success,
            message = responseLogin.message,
            data = responseLogin.data
        )
    }
}