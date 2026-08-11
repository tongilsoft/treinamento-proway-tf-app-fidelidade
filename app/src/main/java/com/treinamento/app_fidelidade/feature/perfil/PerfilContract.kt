package com.treinamento.app_fidelidade.feature.perfil

data class PerfilUiState(
    val carregando: Boolean = true,
    val atualizando: Boolean = false,
    val usuarioId: Long = 0,
    val nome: String = "",
    val email: String = "",
    val endereco: String = "",
    val saldoPontos: Long = 0,
    val qrCode: String = "",
    val offline: Boolean = false,
    val mensagem: String? = null
)

sealed interface PerfilEvent {
    data object Atualizar : PerfilEvent
    data object LimparMensagem : PerfilEvent
    data class SalvarDados(val nome: String, val email: String, val endereco: String) : PerfilEvent
}
