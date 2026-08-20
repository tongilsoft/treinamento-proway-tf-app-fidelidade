package com.treinamento.app_fidelidade.feature.carrinho.componentes

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.treinamento.app_fidelidade.feature.carrinho.formatarPontos

/**
 * Bloco de totais usado no rodape do carrinho e na confirmacao do resgate.
 * Recebe os numeros prontos, entao recalcula sozinho a cada mudanca.
 */
@Composable
fun ResumoPontos(
    totalItens: Int,
    totalPontos: Long,
    saldoPontos: Long,
    modifier: Modifier = Modifier
) {
    val saldoApos = saldoPontos - totalPontos
    val suficiente = saldoApos >= 0

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            CardTotal(
                titulo = "Total de itens",
                valor = "$totalItens ${if (totalItens == 1) "item" else "itens"}",
                modifier = Modifier.weight(1f)
            )
            CardTotal(
                titulo = "Total em pontos",
                valor = "${totalPontos.formatarPontos()} pontos",
                destaque = true,
                modifier = Modifier.weight(1f)
            )
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Linha("Saldo de pontos", saldoPontos.formatarPontos())

                HorizontalDivider(color = MaterialTheme.colorScheme.outline)

                Linha(
                    rotulo = if (suficiente) "Apos o resgate ficara com" else "Pontos faltantes",
                    valor = (if (suficiente) saldoApos else -saldoApos).formatarPontos(),
                    cor = if (suficiente) MaterialTheme.colorScheme.secondary
                    else MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun CardTotal(
    titulo: String,
    valor: String,
    modifier: Modifier = Modifier,
    destaque: Boolean = false
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (destaque) MaterialTheme.colorScheme.secondary
                else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun Linha(
    rotulo: String,
    valor: String,
    cor: Color = MaterialTheme.colorScheme.onSurface
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = rotulo,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = "$valor pontos",
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            color = cor
        )
    }
}
