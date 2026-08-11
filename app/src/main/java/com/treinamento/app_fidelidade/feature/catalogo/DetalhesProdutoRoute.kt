package com.treinamento.app_fidelidade.feature.catalogo

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetalhesProdutoRoute(
    viewModel: CatalogoViewModel,
    onBack: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val produto = uiState.produtoSelecionado

    if (produto != null) {
        DetalhesProdutoScreen(
            produto = produto,
            pontosAtuais = uiState.pontosAtuais,
            quantidadeCarrinho = uiState.quantidadeCarrinho,
            quantidadeSelecionada = uiState.quantidadeSelecionada,
            onBack = onBack,
            onAjustarQuantidade = { viewModel.onEvent(CatalogoEvent.AjustarQuantidade(it)) },
            onAdicionar = { viewModel.onEvent(CatalogoEvent.AdicionarAoCarrinho(produto.id, uiState.quantidadeSelecionada)) },
            onNavigate = onNavigate,
            modifier = modifier
        )
    }
}
