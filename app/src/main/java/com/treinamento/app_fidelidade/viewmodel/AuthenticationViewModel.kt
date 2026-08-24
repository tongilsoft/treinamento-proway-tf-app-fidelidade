package com.treinamento.app_fidelidade.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.data.remote.dto.response.Usuario
import com.treinamento.app_fidelidade.data.remote.service.AuthenticationService
import com.treinamento.app_fidelidade.data.repository.api.AuthenticationRepository
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.model.UsuarioLogin
import java.io.IOException
import kotlinx.coroutines.launch
import retrofit2.HttpException

class AuthenticationViewModel (repositoryDB: UsuarioDBRepository) : ViewModel() {


    private val service = AuthenticationService(
        api = RetrofitInstance.api
    )

    private val repository = AuthenticationRepository(
        service = service
    )

    private val repositoryDB = repositoryDB

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
                Log.d("TESTE", "Response: $response")
                if (response.success && response.data != null) {
//                    usuario = response.data
                    salvarSQLite(response.data, repositoryDB)
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
    fun doRegister(usuarioRegister: UsuarioRegistro, navController: NavHostController) {
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
                    /*
                     * O cadastro apenas cria o usuario no backend, ele nao autentica.
                     * Sem este login o servidor continuaria respondendo como o usuario
                     * anterior, e saldo, extrato e resgates viriam da conta errada.
                     *
                     * So marcamos o usuario como autenticado depois que o login volta:
                     * e ele quem traz o token e abre a sessao.
                     */
                    val login = repository.doLogin(
                        UsuarioLogin(
                            email = usuarioRegister.email,
                            senha = usuarioRegister.password
                        )
                    )

                    if (login.success && login.data != null) {
                        salvarSQLite(login.data, repositoryDB)
                    } else {
                        errorMessage =
                            "Cadastro realizado, mas nao foi possivel entrar. Faca login."
                    }
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

    suspend fun salvarSQLite(usuarioNovo: Usuario?, repositoryDB: UsuarioDBRepository){
        try {
            if (usuarioNovo != null) {

                repositoryDB.cadastrar(
                    com.treinamento.app_fidelidade.model.Usuario(
                        id = usuarioNovo.id,
                        nome = usuarioNovo.name,
                        email = usuarioNovo.email,
                        // O login sempre devolve token; o ?: e so para o Gson nao derrubar o app.
                        token = usuarioNovo.token ?: "",
                        pontosSaldo = usuarioNovo.pontosSaldo,
                        qrCode = usuarioNovo.qrCode,
                        createdAt = usuarioNovo.createdAt ,
                        updatedAt = usuarioNovo.updatedAt
                    )
                )
                usuario = usuarioNovo
            }
        }
        catch (e: Exception){
            println(e.message)
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