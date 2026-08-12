package com.treinamento.app_fidelidade.view.Authentication

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


private val BackgroundColor = Color(0xFF001427)
private val FieldColor = Color(0xFF0D1F32)
private val BorderColor = Color(0xFF1D3B57)
private val PrimaryBlue = Color(0xFF007BFF)
private val SecondaryBlue = Color(0xFF0065D9)
private val TextColor = Color(0xFFF4F7FB)
private val SecondaryTextColor = Color(0xFFB7C1CE)
@Composable
fun PasswordVisibilityButton(
    passwordVisible: Boolean,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (passwordVisible) {
                Icons.Default.VisibilityOff
            } else {
                Icons.Default.Visibility
            },
            contentDescription = if (passwordVisible) {
                "Ocultar senha"
            } else {
                "Mostrar senha"
            },
            tint = SecondaryTextColor
        )
    }
}