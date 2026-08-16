package com.treinamento.app_fidelidade.repository

import com.treinamento.app_fidelidade.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioRepository {
    fun observarUsuario(): Flow<Usuario>
    suspend fun atualizarUsuario(): Result<Unit>
    suspend fun atualizarDados(nome: String, email: String): Result<Unit>
    suspend fun alterarSenha(senhaAtual: String, novaSenha: String): Result<Unit>
}
