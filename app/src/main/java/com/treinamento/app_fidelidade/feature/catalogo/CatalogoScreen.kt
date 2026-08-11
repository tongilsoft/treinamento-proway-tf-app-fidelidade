package com.treinamento.app_fidelidade.feature.catalogo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.treinamento.app_fidelidade.feature.catalogo.components.ProdutoCard
import com.treinamento.app_fidelidade.ui.components.FidelidadeBottomBar
import com.treinamento.app_fidelidade.ui.components.LoadingContent
import com.treinamento.app_fidelidade.ui.components.OfflineBanner

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogoScreen(
    state: CatalogoUiState,
    onEvent: (CatalogoEvent) -> Unit,
    onBack: () -> Unit,
    onNavigateToCart: () -> Unit,
    onNavigate: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var filtrosAbertos by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = "Catálogo de Recompensas",
                        color = MaterialTheme.colorScheme.onBackground,
                        style = MaterialTheme.typography.titleMedium
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Voltar",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onNavigateToCart) {
                        BadgedBox(
                            badge = {
                                if (state.quantidadeCarrinho > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.secondary,
                                        contentColor = MaterialTheme.colorScheme.onSecondary
                                    ) { Text(state.quantidadeCarrinho.toString()) }
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.ShoppingCart, 
                                contentDescription = "Abrir carrinho",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
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
            FidelidadeBottomBar(
                selecionado = "catalogo",
                quantidadeCarrinho = state.quantidadeCarrinho,
                onNavigate = onNavigate
            )
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                if (state.offline) {
                    item { OfflineBanner() }
                }

                item {
                    OutlinedTextField(
                        value = state.busca,
                        onValueChange = { onEvent(CatalogoEvent.Buscar(it)) },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { 
                            Text(
                                text = "Buscar produtos...",
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            ) 
                        },
                        leadingIcon = { 
                            Icon(
                                imageVector = Icons.Default.Search, 
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            ) 
                        },
                        trailingIcon = {
                            BadgedBox(
                                badge = {
                                    if (state.quantidadeFiltrosAtivos > 0) {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.secondary,
                                            contentColor = MaterialTheme.colorScheme.onSecondary
                                        ) {
                                            Text(text = state.quantidadeFiltrosAtivos.toString())
                                        }
                                    }
                                }
                            ) {
                                IconButton(onClick = { filtrosAbertos = true }) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList, 
                                        contentDescription = if (state.quantidadeFiltrosAtivos > 0) {
                                            "Abrir filtros do catálogo, ${state.quantidadeFiltrosAtivos} filtros ativos"
                                        } else {
                                            "Abrir filtros do catálogo"
                                        },
                                        tint = if (state.quantidadeFiltrosAtivos > 0) 
                                            MaterialTheme.colorScheme.primary 
                                        else 
                                            MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurface,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outline
                        )
                    )
                }

                item {
                    Text(
                        text = "Seus pontos: ${state.pontosAtuais}",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                if (state.produtos.isEmpty()) {
                    item {
                        Text(
                            text = if (state.busca.isBlank()) "Nenhuma recompensa encontrada." else "Nenhum resultado para \"${state.busca}\".",
                            modifier = Modifier.padding(vertical = 32.dp),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onBackground
                        )
                    }
                } else {
                    items(state.produtos, key = { it.id }) { produto ->
                        ProdutoCard(
                            produto = produto,
                            pontosAtuais = state.pontosAtuais,
                            onClick = { onEvent(CatalogoEvent.SelecionarProduto(produto.id)) },
                            onAdicionarClick = { onEvent(CatalogoEvent.AdicionarAoCarrinho(produto.id, 1)) }
                        )
                    }
                }
            }
        }

        if (filtrosAbertos) {
            FiltrosBottomSheet(
                state = state,
                onAplicar = { ordenacao, categoria ->
                    onEvent(CatalogoEvent.AplicarFiltros(ordenacao, categoria))
                    filtrosAbertos = false
                },
                onLimpar = {
                    onEvent(CatalogoEvent.LimparFiltros)
                    filtrosAbertos = false
                },
                onDismiss = { filtrosAbertos = false }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FiltrosBottomSheet(
    state: CatalogoUiState,
    onAplicar: (OrdenacaoCatalogo, String?) -> Unit,
    onLimpar: () -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var ordenacaoTemporaria by remember { mutableStateOf(state.ordenacao) }
    var categoriaTemporaria by remember { mutableStateOf(state.categoriaSelecionada) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 32.dp, start = 16.dp, end = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Filtros",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                TextButton(
                    onClick = onLimpar,
                    enabled = state.quantidadeFiltrosAtivos > 0 || 
                             ordenacaoTemporaria != OrdenacaoCatalogo.MAIS_RELEVANTES || 
                             categoriaTemporaria != null
                ) {
                    Text("Limpar filtros")
                }
            }

            Text(
                text = "Ordenar por",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            
            Column(Modifier.selectableGroup()) {
                OrdenacaoCatalogo.entries.forEach { ordenacao ->
                    FiltroOpcao(
                        text = ordenacao.label,
                        selected = ordenacaoTemporaria == ordenacao,
                        onClick = { ordenacaoTemporaria = ordenacao }
                    )
                }
            }

            HorizontalDivider(color = MaterialTheme.colorScheme.outline)

            Text(
                text = "Categorias",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
            )

            Column(Modifier.selectableGroup()) {
                FiltroOpcao(
                    text = "Todas as categorias",
                    selected = categoriaTemporaria == null,
                    onClick = { categoriaTemporaria = null }
                )
                state.categorias.forEach { categoria ->
                    FiltroOpcao(
                        text = categoria,
                        selected = categoriaTemporaria == categoria,
                        onClick = { categoriaTemporaria = categoria }
                    )
                }
            }

            Button(
                onClick = { onAplicar(ordenacaoTemporaria, categoriaTemporaria) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                )
            ) {
                Text("Aplicar filtros")
            }
        }
    }
}

@Composable
private fun FiltroOpcao(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        Modifier
            .fillMaxWidth()
            .selectable(
                selected = selected,
                onClick = onClick,
                role = Role.RadioButton
            )
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = selected,
            onClick = null
        )
        Text(
            text = text,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 16.dp),
            color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

private val OrdenacaoCatalogo.label: String
    get() = when (this) {
        OrdenacaoCatalogo.MAIS_RELEVANTES -> "Mais relevantes"
        OrdenacaoCatalogo.MENOR_PONTUACAO -> "Menor pontuação"
        OrdenacaoCatalogo.MAIOR_PONTUACAO -> "Maior pontuação"
        OrdenacaoCatalogo.NOME -> "Nome"
    }
