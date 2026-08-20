package com.treinamento.app_fidelidade.feature.home


import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoEvent
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoScreen
import com.treinamento.app_fidelidade.feature.catalogo.CatalogoViewModel
import com.treinamento.app_fidelidade.view.home.HomeScreen
import java.math.BigInteger

@Composable
fun HomeRoute(
    viewModel: CatalogoViewModel,
    onBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
//    onNavigateToCart: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Busca o catalogo toda vez que a tela entra em cena: se a primeira tentativa
    // falhou (servidor fora do ar), voltar para a aba tenta de novo sem reabrir o app.
//    LaunchedEffect(Unit) {
//        viewModel.onEvent(CatalogoEvent.Atualizar)
//    }

    HomeScreen(
        state = uiState,
        onEvent = { event ->
//            if (event is CatalogoEvent.SelecionarProduto) {
//                viewModel.onEvent(event)
//                onNavigateToDetails(event.id)
//            } else {
//                viewModel.onEvent(event)
//            }
        },
        onBack = onBack,
        onNavigate = onNavigate,
        modifier = modifier
    )
}
