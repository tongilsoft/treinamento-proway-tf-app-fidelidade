package com.treinamento.app_fidelidade.feature.perfil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.rotas.Rotas

@Composable
fun PerfilRoute(
    viewModel: PerfilViewModel,
    onNavigateToEditarPerfil: () -> Unit,
    onNavigateToAlterarSenha: () -> Unit,
//    onSairClick: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier,
    navController: NavController
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PerfilScreen(
        state = uiState,
        onEvent = viewModel::onEvent,
        onEditarClick = onNavigateToEditarPerfil,
        onAlterarSenhaClick = onNavigateToAlterarSenha,
        onSairClick = {
            viewModel.sairConta()
            navController.navigate(Rotas.AUTHENTICATION) {
                launchSingleTop = false
            }
        },
        onNavigate = onNavigate,
        modifier = modifier
    )
}
