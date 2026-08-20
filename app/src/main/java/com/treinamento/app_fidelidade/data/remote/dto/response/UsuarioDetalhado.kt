package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger

/**
 * Cadastro completo devolvido por GET /api/usuarios/meusDados.
 * Bem maior que [Usuario] (que so traz o resumo do login), por isso e uma classe separada.
 */
data class UsuarioDetalhado(
    val id: BigInteger,
    val name: String,
    val email: String,
    val telefone: String?,
    val cpf: String?,
    val dataNascimento: String?,
    val genero: String?,
    val endereco: Endereco?,
    val ativo: Boolean,
    val qrCode: String?,
    val nivelMembro: String?,
    val dataRegistro: String?,
    val ultimoAcesso: String?,
    val pontosSaldo: BigInteger,
    val pontosUtilizados: BigInteger,
    val totalPontosGanhos: BigInteger,
    val createdAt: String?,
    val updatedAt: String?,
    val movimentacoes: List<Movimentacao> = emptyList()
)
