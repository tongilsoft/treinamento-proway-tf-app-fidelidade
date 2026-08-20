package com.treinamento.app_fidelidade.view.resgate

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.treinamento.app_fidelidade.model.FiltroResgate
import com.treinamento.app_fidelidade.ui.components.FidelidadeBottomBar
import com.treinamento.app_fidelidade.ui.components.LoadingContent
import com.treinamento.app_fidelidade.view.resgate.componentes.ResgateCard
import com.treinamento.app_fidelidade.viewmodel.ListaResgatesViewModel
import com.treinamento.app_fidelidade.view.resgate.componentes.BotaoTesteConexao

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaResgatesScreen(
    onBack: () -> Unit,
    onConfirmarPendente: (Long) -> Unit,
    onNavigate: (String) -> Unit,
    viewModel: ListaResgatesViewModel = viewModel(factory = ListaResgatesViewModel.Factory),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Rebusca o extrato toda vez que a tela abre: um resgate feito agora ha pouco
    // precisa aparecer aqui sem reabrir o app.
    LaunchedEffect(Unit) {
        viewModel.carregar()
    }

    LaunchedEffect(state.mensagem) {
        state.mensagem?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.limparMensagem()
        }
    }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Meus Resgates", style = MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Voltar")
                    }
                },
                // MOCK
                actions = { BotaoTesteConexao() },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground,
                    navigationIconContentColor = MaterialTheme.colorScheme.onBackground,
                    actionIconContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        },
        bottomBar = {
            FidelidadeBottomBar(selecionado = "perfil", onNavigate = onNavigate)
        }
    ) { padding ->
        if (state.carregando) {
            LoadingContent(Modifier.padding(padding))
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        FiltroResgate.entries.forEach { opcao ->
                            val selecionado = opcao == state.filtro
                            FilterChip(
                                selected = selecionado,
                                onClick = { viewModel.alterarFiltro(opcao) },
                                label = { Text(opcao.rotulo) },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surface,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }

                if (state.vazio) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 48.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Nenhum resgate por aqui.",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                } else {
                    items(state.resgatesFiltrados, key = { it.id }) { resgate ->
                        ResgateCard(
                            resgate = resgate,
                            onClick = { viewModel.abrirPendente(resgate.id, onConfirmarPendente) }
                        )
                    }
                }
            }
        }
    }
}

private val FiltroResgate.rotulo: String
    get() = when (this) {
        FiltroResgate.TODOS -> "Todos"
        FiltroResgate.CONCLUIDOS -> "Concluidos"
        FiltroResgate.PENDENTES -> "Pendentes"
    }
