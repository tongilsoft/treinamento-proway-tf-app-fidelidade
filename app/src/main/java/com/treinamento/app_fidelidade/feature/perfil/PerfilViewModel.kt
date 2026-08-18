package com.treinamento.app_fidelidade.feature.perfil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.treinamento.app_fidelidade.repository.UsuarioRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

private data class Controls(
    val atualizando: Boolean = false,
    val offline: Boolean = false,
    val mensagem: String? = null
)

class PerfilViewModel(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {
    private val controls = MutableStateFlow(Controls())

    val uiState: StateFlow<PerfilUiState> = combine(
        usuarioRepository.observarUsuario(),
        controls
    ) { usuario, c ->
        PerfilUiState(
            carregando = false,
            atualizando = c.atualizando,
            usuarioId = usuario.id,
            nome = usuario.nome,
            email = usuario.email,
            saldoPontos = usuario.pontosSaldo,
            offline = c.offline,
            mensagem = c.mensagem
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PerfilUiState())

    fun onEvent(event: PerfilEvent) {
        when (event) {
            PerfilEvent.Atualizar -> atualizar()
            PerfilEvent.LimparMensagem -> controls.update { it.copy(mensagem = null) }
            is PerfilEvent.SalvarDados -> salvarDados(event.nome, event.email, event.endereco)
        }
    }

    private fun salvarDados(nome: String, email: String, endereco: String) {
        viewModelScope.launch {
            controls.update { it.copy(atualizando = true, mensagem = null) }
            usuarioRepository.atualizarDados(nome, email)
                .onSuccess {
                    controls.update { it.copy(atualizando = false, mensagem = "Dados atualizados com sucesso!") }
                }
                .onFailure { error ->
                    controls.update { it.copy(atualizando = false, mensagem = error.message ?: "Erro ao atualizar dados") }
                }
        }
    }

    fun alterarSenha(
        senhaAtual: String,
        novaSenha: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            controls.update { it.copy(atualizando = true, mensagem = null) }
            usuarioRepository.alterarSenha(senhaAtual, novaSenha)
                .onSuccess {
                    controls.update { it.copy(atualizando = false, mensagem = "Senha alterada com sucesso!") }
                    onSuccess()
                }
                .onFailure { error ->
                    controls.update { it.copy(atualizando = false, mensagem = error.message ?: "Erro ao alterar senha") }
                }
        }
    }

    private fun atualizar() = viewModelScope.launch {
        controls.update { it.copy(atualizando = true, mensagem = null) }
        usuarioRepository.atualizarUsuario()
            .onSuccess { controls.update { it.copy(atualizando = false, offline = false) } }
            .onFailure { controls.update { it.copy(atualizando = false, offline = true, mensagem = "Erro ao atualizar dados") } }
    }
}
