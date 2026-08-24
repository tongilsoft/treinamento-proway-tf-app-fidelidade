package com.treinamento.app_fidelidade.feature.perfil

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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

    /*
     * Busca os dados toda vez que a tela entra em cena.
     *
     * Antes a unica chamada era um LaunchedEffect(Unit) la no FidelidadeApp, que roda
     * uma vez so na abertura do app. Isso deixava dois furos: se aquela primeira
     * tentativa falhasse, o perfil ficava vazio ate reabrir o app; e quem acabou de se
     * cadastrar continuava vendo os dados do usuario anterior, porque a busca ja tinha
     * acontecido antes do login.
     */
    LaunchedEffect(Unit) {
        viewModel.onEvent(PerfilEvent.Atualizar)
    }

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
