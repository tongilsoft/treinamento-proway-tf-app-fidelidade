package com.treinamento.app_fidelidade.view.fidelidade

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.data.remote.RetrofitInstance
import com.treinamento.app_fidelidade.di.AppContainer
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoEvent
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoRoute
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoViewModel
import com.treinamento.app_fidelidade.feature.catalogo.DetalhesProdutoRoute
import com.treinamento.app_fidelidade.feature.home.HomeRoute
import com.treinamento.app_fidelidade.feature.perfil.AlterarSenhaScreen
import com.treinamento.app_fidelidade.feature.perfil.EditarPerfilScreen
import com.treinamento.app_fidelidade.feature.perfil.PerfilEvent
import com.treinamento.app_fidelidade.feature.perfil.PerfilRoute
import com.treinamento.app_fidelidade.feature.perfil.PerfilViewModel
import com.treinamento.app_fidelidade.model.OrigemResgate
import com.treinamento.app_fidelidade.repository.*
import com.treinamento.app_fidelidade.ui.components.util.rememberNetworkConnection
import com.treinamento.app_fidelidade.viewmodel.AuthenticationViewModelFactory

import com.treinamento.app_fidelidade.view.carrinho.CarrinhoScreen
import com.treinamento.app_fidelidade.view.home.HomeScreen
import com.treinamento.app_fidelidade.view.resgate.ConfirmarResgateScreen
import com.treinamento.app_fidelidade.view.resgate.ListaResgatesScreen
import com.treinamento.app_fidelidade.viewmodel.AuthenticationViewModel

private enum class AppRoute {
    HOME, CATALOGO, DETALHES, PERFIL, EDITAR_PERFIL, ALTERAR_SENHA,
    CARRINHO, CONFIRMAR_RESGATE, LISTA_RESGATES
}

@Composable
fun FidelidadeApp(
    navController: NavHostController,
    repository: UsuarioDBRepository
) {

    val produtoRepository = remember { RemoteProdutoRepository(RetrofitInstance.api) }
    val usuarioRepository = remember { RemoteUsuarioRepository(RetrofitInstance.api) }

    val catalogoViewModel: CatalogoViewModel = viewModel(
        factory = factory { CatalogoViewModel(produtoRepository, usuarioRepository, AppContainer.carrinhoRepository, AppContainer.saldoRepository) }
    )
    val perfilViewModel: PerfilViewModel = viewModel(
        factory = factory { PerfilViewModel(usuarioRepository, repository) }
    )

    val authenticationViewModelFactory = remember(repository) {
        AuthenticationViewModelFactory(repository)
    }

    val authenticationViewModel: AuthenticationViewModel = viewModel(
        factory = authenticationViewModelFactory
    )


    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val isConnected = rememberNetworkConnection()
    LaunchedEffect(isConnected) {

        val mensagem = if (isConnected) {
            "Internet conectada"
        } else {
            "Sem conexão com a internet"
        }
        snackbarHostState.showSnackbar(
                    message = mensagem
        )
    }


    // Obter AuthenticationViewModel compartilhado para poder limpar estado no logout
//    val authenticationViewModel: AuthenticationViewModel = viewModel(
//        factory = AuthenticationViewModelFactory(
//            repository = repository
//        )
//    )

    LaunchedEffect(Unit) {
        catalogoViewModel.onEvent(CatalogoEvent.Atualizar)
        perfilViewModel.onEvent(PerfilEvent.Atualizar)
    }

    val catalogo by catalogoViewModel.uiState.collectAsStateWithLifecycle()
    val perfil by perfilViewModel.uiState.collectAsStateWithLifecycle()

    var currentRoute by rememberSaveable { mutableStateOf(AppRoute.CATALOGO) }

    // De onde a confirmação foi aberta
    var origemResgate by remember { mutableStateOf<OrigemResgate>(OrigemResgate.Carrinho) }

    fun navigate(route: String) {
        when (route) {
            "home" -> currentRoute = AppRoute.HOME
            "catalogo" -> currentRoute = AppRoute.CATALOGO
            "perfil" -> currentRoute = AppRoute.PERFIL
            "carrinho" -> currentRoute = AppRoute.CARRINHO
            "resgates" -> currentRoute = AppRoute.LISTA_RESGATES
        }
    }

    when (currentRoute) {
        AppRoute.HOME -> HomeRoute(
            viewModel = catalogoViewModel,
            onBack = { },
            onNavigateToDetails = {},
//            onNavigateToCart = { navigate("carrinho") },
            onNavigate = ::navigate,
            modifier = Modifier
        )
        AppRoute.CATALOGO -> CatalogoRoute(
            viewModel = catalogoViewModel,
            onBack = { },
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
            onNavigate = ::navigate,
            navController = navController
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

        AppRoute.CARRINHO -> CarrinhoScreen(
            onBack = { currentRoute = AppRoute.CATALOGO },
            onContinuar = {
                origemResgate = OrigemResgate.Carrinho
                currentRoute = AppRoute.CONFIRMAR_RESGATE
            },
            onNavigate = ::navigate
        )

        AppRoute.CONFIRMAR_RESGATE -> ConfirmarResgateScreen(
            origem = origemResgate,
            onBack = { currentRoute = AppRoute.CARRINHO },
            onResgateFinalizado = { currentRoute = AppRoute.LISTA_RESGATES },
            onNavigate = ::navigate
        )

        AppRoute.LISTA_RESGATES -> ListaResgatesScreen(
            onBack = { currentRoute = AppRoute.PERFIL },
            onConfirmarPendente = { id ->
                origemResgate = OrigemResgate.Pendente(id)
                currentRoute = AppRoute.CONFIRMAR_RESGATE
            },
            onNavigate = ::navigate
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
