package com.treinamento.app_fidelidade.view.resgate.componentes

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassEmpty
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.treinamento.app_fidelidade.model.Resgate
import com.treinamento.app_fidelidade.model.StatusResgate
import com.treinamento.app_fidelidade.model.formatarPontos
import com.treinamento.app_fidelidade.ui.theme.AvisoApp
import com.treinamento.app_fidelidade.ui.theme.Sucesso

@Composable
fun ResgateCard(
    resgate: Resgate,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val pendente = resgate.status == StatusResgate.PENDENTE

    Card(
        // So o pendente e clicavel: e o unico que tem acao (reenviar).
        modifier = modifier
            .fillMaxWidth()
            .clickable(enabled = pendente, onClick = onClick),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = resgate.titulo,
                style = MaterialTheme.typography.titleMedium
            )

            Text(
                text = "${resgate.totalPontos.formatarPontos()} pontos",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (pendente) {
                Text(
                    text = "Pendente",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = AvisoApp
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.HourglassEmpty,
                        contentDescription = null,
                        tint = AvisoApp,
                        modifier = Modifier.size(14.dp)
                    )
                    Text(
                        text = "Aguardando sincronizacao",
                        style = MaterialTheme.typography.labelMedium,
                        color = AvisoApp
                    )
                }
            } else {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Concluido",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Bold,
                        color = Sucesso
                    )
                    Text(
                        text = resgate.data,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}
