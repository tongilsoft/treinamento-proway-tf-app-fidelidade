package com.treinamento.app_fidelidade.data.repository.api

import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioLoginRequest
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.service.AuthenticationService
import com.treinamento.app_fidelidade.model.AuthenticationData
import com.treinamento.app_fidelidade.model.UsuarioLogin

class AuthenticationRepository(
    private val service: AuthenticationService
) {

    suspend fun doLogin(
        usuarioLogin: UsuarioLogin
    ): AuthenticationData {
        val request = UsuarioLoginRequest(
            email = usuarioLogin.email,
            senha = usuarioLogin.senha
        )

        return service.doLogin(request)
    }

    suspend fun doRegister(
        usuarioRegister: UsuarioRegistro
    ): AuthenticationData {
        return service.doRegister(usuarioRegister)
    }
}