package com.treinamento.app_fidelidade.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "usuarios")
data class UsuarioEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val nome: String,
    val email: String,
    val pontosSaldo: Long,
    val qrCode: String,
    val token: String,
    val urlImage: String? = null,
    val createdAt: String,
    val updatedAt: String? = null
)
