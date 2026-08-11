package com.treinamento.app_fidelidade.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CardGiftcard
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight

@Composable
fun FidelidadeBottomBar(
    selecionado: String,
    quantidadeCarrinho: Int = 0,
    onNavigate: (String) -> Unit
) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        contentColor = MaterialTheme.colorScheme.onSurface
    ) {
        val itemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.onSurface,
            selectedTextColor = MaterialTheme.colorScheme.onSurface,
            unselectedIconColor = MaterialTheme.colorScheme.onSurface,
            unselectedTextColor = MaterialTheme.colorScheme.onSurface,
            indicatorColor = MaterialTheme.colorScheme.primaryContainer
        )

        item("home", "Home", { Icon(Icons.Outlined.Home, null) }, selecionado, itemColors, onNavigate)
        item("catalogo", "Catálogo", { Icon(Icons.Outlined.CardGiftcard, null) }, selecionado, itemColors, onNavigate)
        item("carrinho", "Carrinho", {
            BadgedBox(
                badge = {
                    if (quantidadeCarrinho > 0) {
                        Badge(
                            containerColor = MaterialTheme.colorScheme.secondary,
                            contentColor = MaterialTheme.colorScheme.onSecondary
                        ) { Text(quantidadeCarrinho.toString()) }
                    }
                }
            ) { Icon(Icons.Outlined.ShoppingCart, null) }
        }, selecionado, itemColors, onNavigate)
        item("perfil", "Perfil", { Icon(Icons.Outlined.Person, null) }, selecionado, itemColors, onNavigate)
    }
}

@Composable
private fun androidx.compose.foundation.layout.RowScope.item(
    route: String,
    label: String,
    icon: @Composable () -> Unit,
    selecionado: String,
    colors: androidx.compose.material3.NavigationBarItemColors,
    onNavigate: (String) -> Unit
) {
    NavigationBarItem(
        selected = route == selecionado,
        onClick = { onNavigate(route) },
        icon = icon,
        label = { 
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Normal),
                maxLines = 1
            ) 
        },
        colors = colors
    )
}
