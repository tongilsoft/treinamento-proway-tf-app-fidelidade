package com.treinamento.app_fidelidade.data.repository.db

import com.treinamento.app_fidelidade.data.local.dao.UsuarioDao
import com.treinamento.app_fidelidade.data.local.entity.UsuarioEntity
import com.treinamento.app_fidelidade.model.Usuario
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime


class UsuarioRepositoryImpl (private val dao: UsuarioDao): UsuarioDBRepository {

    override val listar: Flow<List<Usuario>> = dao.listar().map { lista -> lista.map { it.toModel() } }

    override val first: Flow<Usuario?> = dao.first().map { it?.toModel() }

    override suspend fun cadastrar(usuario: Usuario) {
        dao.cadastrar(usuario.toEntity())
    }

    override suspend fun editar(usuario: Usuario) {
        dao.editar(usuario.toEntity())
    }

    override suspend fun removerUsuarios() {
        dao.removerUsuarios()
    }
}


    private fun UsuarioEntity.toModel() = Usuario(
        id = id,
        nome = nome,
        email = email,
        pontosSaldo = pontosSaldo,
        qrCode = qrCode,
        urlImage = urlImage,
        createdAt = createdAt,
        updatedAt = updatedAt,
        token = token
    )

    private fun Usuario.toEntity() = UsuarioEntity(
        id = id,
        nome = nome,
        email = email,
        pontosSaldo = pontosSaldo,
        qrCode = qrCode,
        urlImage = urlImage,
        token = token,
        createdAt = createdAt ?: "",
        updatedAt = updatedAt ?: ""
    )