package com.treinamento.app_fidelidade.view.fidelidade

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoRoute
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoViewModel
import com.treinamento.app_fidelidade.feature.catalogo.DetalhesProdutoRoute
import com.treinamento.app_fidelidade.feature.perfil.AlterarSenhaScreen
import com.treinamento.app_fidelidade.feature.perfil.EditarPerfilScreen
import com.treinamento.app_fidelidade.feature.perfil.PerfilEvent
import com.treinamento.app_fidelidade.feature.perfil.PerfilRoute
import com.treinamento.app_fidelidade.feature.perfil.PerfilViewModel
import com.treinamento.app_fidelidade.repository.*

private enum class AppRoute { CATALOGO, DETALHES, PERFIL, EDITAR_PERFIL, ALTERAR_SENHA }

@Composable
fun FidelidadeApp() {
    val produtoRepository = remember { InMemoryProdutoRepository() }
    val usuarioRepository = remember { InMemoryUsuarioRepository() }

    val catalogoViewModel: CatalogoViewModel = viewModel(
        factory = factory { CatalogoViewModel(produtoRepository, usuarioRepository) }
    )
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = factory { PerfilViewModel(usuarioRepository) }
    )

    val catalogo by catalogoViewModel.uiState.collectAsStateWithLifecycle()
    val perfil by perfilViewModel.uiState.collectAsStateWithLifecycle()

    var currentRoute by rememberSaveable { mutableStateOf(AppRoute.CATALOGO) }

    fun navigate(route: String) {
        when (route) {
            "home" -> currentRoute = AppRoute.CATALOGO
            "catalogo" -> currentRoute = AppRoute.CATALOGO
            "perfil" -> currentRoute = AppRoute.PERFIL
            "carrinho" -> { /* Destino externo */ }
        }
    }

    when (currentRoute) {
        AppRoute.CATALOGO -> CatalogoRoute(
            viewModel = catalogoViewModel,
            onBack = { /* Home ou fechar app */ },
            onNavigateToDetails = {
                currentRoute = AppRoute.DETALHES
            },
            onNavigateToCart = { navigate("carrinho") },
            onNavigate = ::navigate,
            modifier = Modifier
        )

        AppRoute.DETALHES -> DetalhesProdutoRoute(
            viewModel = catalogoViewModel,
            onBack = { currentRoute = AppRoute.CATALOGO },
            onNavigate = ::navigate
        )

        AppRoute.PERFIL -> PerfilRoute(
            viewModel = perfilViewModel,
            onNavigateToEditarPerfil = { currentRoute = AppRoute.EDITAR_PERFIL },
            onNavigateToAlterarSenha = { currentRoute = AppRoute.ALTERAR_SENHA },
            onNavigate = ::navigate
        )

        AppRoute.EDITAR_PERFIL -> EditarPerfilScreen(
            state = perfil,
            onBack = { currentRoute = AppRoute.PERFIL },
            onSalvar = { nome: String, email: String, endereco: String ->
                perfilViewModel.onEvent(PerfilEvent.SalvarDados(nome, email, endereco))
                currentRoute = AppRoute.PERFIL
            }
        )

        AppRoute.ALTERAR_SENHA -> AlterarSenhaScreen(
            onBack = { currentRoute = AppRoute.PERFIL },
            onSalvar = { senhaAtual, novaSenha ->
                perfilViewModel.alterarSenha(
                    senhaAtual = senhaAtual,
                    novaSenha = novaSenha,
                    onSuccess = { currentRoute = AppRoute.PERFIL }
                )
            }
        )
    }
}

private inline fun <reified VM : ViewModel> factory(
    crossinline create: () -> VM
): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(VM::class.java))
        @Suppress("UNCHECKED_CAST")
        return create() as T
    }
}
