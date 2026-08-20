package com.treinamento.app_fidelidade.data.remote.dto.request


/**
 * Corpo de PUT /api/usuarios/meusDados, aplicado ao usuario logado.
 *
 * E um PATCH na pratica: manda so o que mudou, o resto o mock mantem como esta.
 * id, createdAt, saldo de pontos e movimentacoes nao sao alteraveis por aqui.
 */
data class AtualizarUsuarioRequest(
    val name: String? = null,
    val email: String? = null,
    val senha: String? = null,
    val telefone: String? = null,
    val cpf: String? = null,
    val dataNascimento: String? = null,
    val genero: String? = null,
    val ativo: Boolean? = null,
    val qrCode: String? = null,
    val nivelMembro: String? = null,
    val ultimoAcesso: String? = null,
    val endereco: EnderecoRequest? = null
)
