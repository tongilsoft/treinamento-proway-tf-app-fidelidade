package com.treinamento.app_fidelidade.data.remote.dto.response

import java.math.BigInteger
import java.time.LocalDateTime

data class MeusDadosResponse(
    val id: BigInteger?,
    val name: String?,
    val email: String?,
    val senha: String?,
    val telefone: String?,
    val cpf: String?,
    val dataNascimento: String?,
    val genero: String?,
    val endereco: EnderecoResponse?,
    val ativo: Boolean?,
    val qrCode: String?,
    val nivelMembro: String?,
    val dataRegistro: String?,
    val ultimoAcesso: LocalDateTime?,
    val pontosSaldo: BigInteger?,
    val pontosUtilizados: BigInteger?,
    val totalPontosGanhos: BigInteger?,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?,
    val movimentacoes: List<MovimentacaoResponse>?
)