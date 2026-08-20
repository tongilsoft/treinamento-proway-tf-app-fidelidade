package com.treinamento.app_fidelidade.feature.catalogo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.treinamento.app_fidelidade.model.Produto
import java.math.BigInteger

@Composable
fun ProdutoCard(
    produto: Produto,
    pontosAtuais: Long,
    onClick: () -> Unit,
    onAdicionarClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val faltam = produto.pontosFaltantes(pontosAtuais)
    
    ElevatedCard(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.elevatedCardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.surfaceVariant, MaterialTheme.shapes.small),
                contentAlignment = Alignment.Center
            ) {

                AsyncImage(
                    //https://flags.restcountries.com/v5/w640/br.png
                    model = produto.imagemUrl,
//                    model = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRcv_a1URHdVxFR8qCh_UBTyRHOB-rJDKjEOeX-3djIuQ&s=10",
//                imageLoader = imageLoader,
                    modifier = Modifier
                        .fillMaxWidth(),
                    contentDescription = null,
//                onLoading = {
//                    println("Carregando...")
//                },
//                onSuccess = {
//                    println("Imagem OK")
//                },
//                onError = {
//                    println(it.result.throwable)
//                }
                )
            }

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = produto.nome,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Text(
                    text = "${produto.valorPointsFormatado()} pontos",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.secondary
                )

                if (faltam > 0) {
                    Text(
                        text = "Faltam $faltam pontos",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.tertiary
                    )
                } else {
                    Text(
                        text = "Disponível para resgate",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onAdicionarClick,
                    modifier = Modifier.fillMaxWidth(),
                    enabled = produto.disponivel,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                        disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                ) {
                    Text("Adicionar")
                }
            }
        }
    }
}

private fun Produto.valorPointsFormatado(): String = valorPontos.toString()
