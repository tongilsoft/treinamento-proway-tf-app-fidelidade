package com.treinamento.app_fidelidade.data.repository.db

import com.treinamento.app_fidelidade.data.local.entity.UsuarioEntity
import com.treinamento.app_fidelidade.model.Usuario
import kotlinx.coroutines.flow.Flow

interface UsuarioDBRepository {
    val listar: Flow<List<Usuario>>
    val first: Flow<Usuario?>
    suspend fun cadastrar(usuario: Usuario)
    suspend fun editar(usuario: Usuario)
    suspend fun removerUsuarios()
}