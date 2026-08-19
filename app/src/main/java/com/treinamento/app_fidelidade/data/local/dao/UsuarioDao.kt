package com.treinamento.app_fidelidade.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.treinamento.app_fidelidade.data.local.entity.UsuarioEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UsuarioDao {

    @Query("SELECT * FROM usuarios")
    fun listar(): Flow<List<UsuarioEntity>>

    @Query("SELECT * FROM usuarios ORDER BY id ASC LIMIT 1")
    fun first(): Flow<UsuarioEntity?>

    @Insert
    suspend fun cadastrar(usuario: UsuarioEntity)

    @Update
    suspend fun editar(aluno: UsuarioEntity)

    @Query("DELETE FROM usuarios WHERE id > 0")
    suspend fun removerUsuarios()

}