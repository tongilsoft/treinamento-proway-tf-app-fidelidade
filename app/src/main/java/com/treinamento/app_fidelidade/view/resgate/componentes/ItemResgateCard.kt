package com.treinamento.app_fidelidade.view.resgate.componentes

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material.icons.filled.WifiOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.treinamento.app_fidelidade.model.ItemResgate
import com.treinamento.app_fidelidade.model.formatarPontos
import com.treinamento.app_fidelidade.repository.ConexaoMock
import com.treinamento.app_fidelidade.ui.theme.AvisoApp
import com.treinamento.app_fidelidade.view.carrinho.componentes.MiniaturaProduto

/** Linha de item na tela de confirmacao (imagem 7). */
@Composable
fun ItemResgateCard(
    item: ItemResgate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size( 48.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(

                    model = item.imagemUrl,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentDescription = null,
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = item.nome,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "${item.pontos.formatarPontos()} pontos",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                text = "x${item.quantidade}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

/**
 * SO PARA A DEMONSTRACAO: liga e desliga a conexao mockada, para mostrar
 * o fluxo online e o offline na apresentacao. Remover na integracao.
 */
@Composable
fun BotaoTesteConexao() {
    val online by ConexaoMock.online.collectAsStateWithLifecycle()

    IconButton(onClick = { ConexaoMock.alternar() }) {
        Icon(
            imageVector = if (online) Icons.Default.Wifi else Icons.Default.WifiOff,
            contentDescription = if (online) "Simular ficar sem internet" else "Simular voltar a internet",
            tint = if (online) MaterialTheme.colorScheme.onBackground else AvisoApp
        )
    }
}
