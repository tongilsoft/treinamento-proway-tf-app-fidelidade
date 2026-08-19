package com.treinamento.app_fidelidade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "usuario")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val email: String,
    val password: String,
    val pontosSaldo: Long,
    val qrCode: String,
    val urlImage: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime? = null
)
