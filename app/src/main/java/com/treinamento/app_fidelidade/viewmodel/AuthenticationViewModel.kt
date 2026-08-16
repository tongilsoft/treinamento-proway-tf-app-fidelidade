package com.treinamento.app_fidelidade.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import com.treinamento.app_fidelidade.data.remote.service.AuthenticationService
import com.treinamento.app_fidelidade.data.repository.AuthenticationRepository
import com.treinamento.app_fidelidade.model.UsuarioLogin
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class AuthenticationViewModel : ViewModel() {

    private val service = AuthenticationService(
        api = RetrofitInstance.api
    )

    private val repository = AuthenticationRepository(
        service = service
    )

    /*
     * Usuário autenticado.
     *
     * Quando este valor deixa de ser nulo, a AuthenticationScreen
     * detecta o sucesso e solicita a navegação para a próxima tela.
     */
    var usuario by mutableStateOf<Usuario?>(null)
        private set

    /*
     * Indica que existe uma requisição em andamento.
     *
     * É utilizado para desabilitar os botões e mostrar o indicador
     * de carregamento.
     */
    var isLoading by mutableStateOf(false)
        private set

    /*
     * Mensagem que será apresentada no Snackbar.
     */
    var errorMessage by mutableStateOf<String?>(null)
        private set

    /**
     * Realiza o login no backend.
     */
    fun doLogin(usuarioLogin: UsuarioLogin) {
        /*
         * Evita enviar mais de uma chamada caso o usuário
         * clique várias vezes rapidamente.
         */
        if (isLoading) {
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                usuario = null

                val response = repository.doLogin(usuarioLogin)

                if (response.success && response.data != null) {
                    usuario = response.data
                } else {
                    errorMessage = response.message
                        ?.takeIf { message ->
                            message.isNotBlank()
                        }
                        ?: "E-mail ou senha inválidos"
                }
            } catch (exception: HttpException) {
                exception.printStackTrace()

                errorMessage = getHttpErrorMessage(
                    statusCode = exception.code()
                )
            } catch (exception: IOException) {
                exception.printStackTrace()

                errorMessage =
                    "Não foi possível conectar ao servidor. Verifique sua internet."
            } catch (exception: Exception) {
                exception.printStackTrace()

                errorMessage = exception.message
                    ?.takeIf { message ->
                        message.isNotBlank()
                    }
                    ?: "Ocorreu um erro inesperado ao realizar o login."
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Realiza o cadastro no backend.
     */
    fun doRegister(usuarioRegister: UsuarioRegistro) {
        /*
         * Evita enviar mais de uma chamada caso o usuário
         * clique várias vezes rapidamente.
         */
        if (isLoading) {
            return
        }

        viewModelScope.launch {
            try {
                isLoading = true
                errorMessage = null
                usuario = null

                val response = repository.doRegister(usuarioRegister)

                if (response.success && response.data != null) {
                    usuario = response.data
                } else {
                    errorMessage = response.message
                        ?.takeIf { message ->
                            message.isNotBlank()
                        }
                        ?: "Não foi possível realizar o cadastro"
                }
            } catch (exception: HttpException) {
                exception.printStackTrace()

                errorMessage = getHttpErrorMessage(
                    statusCode = exception.code()
                )
            } catch (exception: IOException) {
                exception.printStackTrace()

                errorMessage =
                    "Não foi possível conectar ao servidor. Verifique sua internet."
            } catch (exception: Exception) {
                exception.printStackTrace()

                errorMessage = exception.message
                    ?.takeIf { message ->
                        message.isNotBlank()
                    }
                    ?: "Ocorreu um erro inesperado ao realizar o cadastro."
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Limpa a mensagem depois que o Snackbar termina de apresentá-la.
     */
    fun clearErrorMessage() {
        errorMessage = null
    }

    /**
     * Limpa todos os estados relacionados à autenticação.
     *
     * Pode ser chamado no logout ou antes de voltar para a
     * tela de autenticação.
     */
    fun clearAuthenticationState() {
        usuario = null
        errorMessage = null
        isLoading = false
    }

    private fun getHttpErrorMessage(
        statusCode: Int
    ): String {
        return when (statusCode) {
            400 -> "Os dados informados são inválidos."
            401 -> "E-mail ou senha inválidos."
            403 -> "Acesso não autorizado."
            404 -> "O serviço de autenticação não foi encontrado."
            408 -> "O servidor demorou muito para responder."
            422 -> "Não foi possível processar os dados informados."
            500 -> "Ocorreu um erro interno no servidor."
            502 -> "O servidor está temporariamente indisponível."
            503 -> "O serviço está temporariamente indisponível."
            else -> "Erro ao realizar o login. Código HTTP: $statusCode."
        }
    }
}