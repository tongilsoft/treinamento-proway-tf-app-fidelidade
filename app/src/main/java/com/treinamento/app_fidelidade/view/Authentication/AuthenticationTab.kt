package com.treinamento.app_fidelidade.view.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val TextColor = Color(0xFFF4F7FB)
private val SecondaryTextColor = Color(0xFFB7C1CE)

@Composable
fun AuthenticationTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val selectedBackground = Brush.horizontalGradient(
        colors = listOf(
            Color(0xFF098BFF),
            Color(0xFF0068DE)
        )
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .then(
                if (selected) {
                    Modifier.background(
                        brush = selectedBackground,
                        shape = RoundedCornerShape(12.dp)
                    )
                } else {
                    Modifier.background(Color.Transparent)
                }
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) TextColor else SecondaryTextColor,
            fontSize = 15.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
        )
    }
}