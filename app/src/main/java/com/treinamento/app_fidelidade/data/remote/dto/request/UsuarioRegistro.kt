package com.treinamento.app_fidelidade.data.remote.dto.request

import java.math.BigInteger

data class UsuarioRegistro(
    val id: BigInteger?,
    val name: String,
    val email: String,
    // o mock aceita tanto "senha" quanto "password" neste campo
    val password: String,
    val pontosSaldo: BigInteger = BigInteger.ZERO,
    val qrCode: String?,
    // datas chegam como texto ISO ("2026-08-18T00:44:44.288-03:00"); o Gson padrao nao converte LocalDateTime
    val createdAt: String?,
    val updatedAt: String?
)
