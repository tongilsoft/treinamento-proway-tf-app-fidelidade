package com.treinamento.app_fidelidade.view.carrinho

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.treinamento.app_fidelidade.ui.components.FidelidadeBottomBar
import com.treinamento.app_fidelidade.ui.components.LoadingContent
import com.treinamento.app_fidelidade.view.carrinho.componentes.ItemCarrinhoCard
import com.treinamento.app_fidelidade.view.carrinho.componentes.ResumoPontos
import com.treinamento.app_fidelidade.viewmodel.CarrinhoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CarrinhoScreen(
    onBack: () -> Unit,
    onContinuar: () -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: CarrinhoViewModel = viewModel(factory = CarrinhoViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Carrinho de Resgates",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                actions = {
                    IconButton(onClick = { viewModel.limpar() }, enabled = !state.vazio) {
                        Icon(Icons.Default.DeleteOutline, "Esvaziar carrinho")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            Column {
                if (!state.vazio) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ResumoPontos(
                            totalItens = state.totalItens,
                            totalPontos = state.totalPontos,
                            saldoPontos = state.saldoPontos
                        )

                        Button(
                            onClick = onContinuar,
                            enabled = state.podeContinuar,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(52.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.secondary,
                                contentColor = MaterialTheme.colorScheme.onSecondary
                            )
                        ) {
                            Text(
                                text = "Continuar",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                FidelidadeBottomBar(
                    selecionado = "carrinho",
                    quantidadeCarrinho = state.totalItens,
                    onNavigate = onNavigate
                )
            }
        }
    ) { padding ->
        when {
            state.carregando -> LoadingContent(Modifier.padding(padding))

            state.vazio -> Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(32.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Seu carrinho de resgates esta vazio.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(state.itens, key = { it.produto.id }) { item ->
                    ItemCarrinhoCard(
                        item = item,
                        onRemover = { viewModel.remover(item) },
                        onDiminuir = { viewModel.diminuir(item) },
                        onAumentar = { viewModel.aumentar(item) }
                    )
                }
            }
        }
    }
}
