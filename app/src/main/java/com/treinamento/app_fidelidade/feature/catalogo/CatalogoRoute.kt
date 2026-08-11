package com.treinamento.app_fidelidade.feature.catalogo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CatalogoRoute(
    viewModel: CatalogoViewModel,
    onBack: () -> Unit,
    onNavigateToDetails: (Long) -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CatalogoScreen(
        state = uiState,
        onEvent = { event ->
            if (event is CatalogoEvent.SelecionarProduto) {
                viewModel.onEvent(event)
                onNavigateToDetails(event.id)
            } else {
                viewModel.onEvent(event)
            }
        },
        onBack = onBack,
        onNavigateToCart = onNavigateToCart,
        onNavigate = onNavigate,
        modifier = modifier
    )
}
