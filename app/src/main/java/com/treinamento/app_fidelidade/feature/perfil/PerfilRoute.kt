package com.treinamento.app_fidelidade.feature.perfil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PerfilRoute(
    viewModel: PerfilViewModel,
    onNavigateToEditarPerfil: () -> Unit,
    onNavigateToAlterarSenha: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PerfilScreen(
        state = uiState,
        onEvent = viewModel::onEvent,
        onEditarClick = onNavigateToEditarPerfil,
        onAlterarSenhaClick = onNavigateToAlterarSenha,
        onSairClick = { /* Sair logic */ },
        onNavigate = onNavigate,
        modifier = modifier
    )
}
