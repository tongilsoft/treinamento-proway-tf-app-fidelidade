package com.treinamento.app_fidelidade.feature.perfil

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlterarSenhaScreen(
    onBack: () -> Unit,
    onSalvar: (String, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var senhaAtual by remember { mutableStateOf("") }
    var novaSenha by remember { mutableStateOf("") }
    var confirmacaoSenha by remember { mutableStateOf("") }

    var mostrarSenhaAtual by remember { mutableStateOf(false) }
    var mostrarNovaSenha by remember { mutableStateOf(false) }
    var mostrarConfirmacao by remember { mutableStateOf(false) }

    val senhasIguais = novaSenha == confirmacaoSenha
    val novaSenhaDiferente = novaSenha != senhaAtual && senhaAtual.isNotBlank()
    val tamanhoValido = novaSenha.length >= 6
    val formularioValido = senhaAtual.isNotBlank() && tamanhoValido && senhasIguais && novaSenhaDiferente

    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        text = "Alterar Senha",
                        color = MaterialTheme.colorScheme.onBackground
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    titleContentColor = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            PasswordField(
                label = "Senha atual",
                value = senhaAtual,
                onValueChange = { senhaAtual = it },
                visivel = mostrarSenhaAtual,
                onToggleVisibility = { mostrarSenhaAtual = !mostrarSenhaAtual }
            )

            PasswordField(
                label = "Nova senha",
                value = novaSenha,
                onValueChange = { novaSenha = it },
                visivel = mostrarNovaSenha,
                onToggleVisibility = { mostrarNovaSenha = !mostrarNovaSenha },
                isError = !tamanhoValido && novaSenha.isNotBlank()
            )

            PasswordField(
                label = "Confirmar nova senha",
                value = confirmacaoSenha,
                onValueChange = { confirmacaoSenha = it },
                visivel = mostrarConfirmacao,
                onToggleVisibility = { mostrarConfirmacao = !mostrarConfirmacao },
                isError = !senhasIguais && confirmacaoSenha.isNotBlank()
            )

            Text(
                text = "A senha deve ter no mínimo 6 caracteres.",
                style = MaterialTheme.typography.bodySmall,
                color = if (tamanhoValido) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.error
            )

            if (!senhasIguais && confirmacaoSenha.isNotBlank()) {
                Text(
                    text = "As senhas não coincidem.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            if (!novaSenhaDiferente && novaSenha.isNotBlank() && senhaAtual.isNotBlank() && novaSenha == senhaAtual) {
                Text(
                    text = "A nova senha deve ser diferente da atual.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { onSalvar(senhaAtual, novaSenha) },
                enabled = formularioValido,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
                    disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            ) {
                Text("Salvar Nova Senha")
            }
        }
    }
}

@Composable
private fun PasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    visivel: Boolean,
    onToggleVisibility: () -> Unit,
    isError: Boolean = false
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        isError = isError,
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visivel) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visivel) "Ocultar senha" else "Mostrar senha",
                    tint = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        visualTransformation = if (visivel) VisualTransformation.None else PasswordVisualTransformation(),
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = MaterialTheme.colorScheme.onSurface,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
            disabledTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline,
            errorBorderColor = MaterialTheme.colorScheme.error
        )
    )
}
