package com.treinamento.app_fidelidade.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val FidelidadeColorScheme = darkColorScheme(
    primary = AzulClaro,
    onPrimary = Branco,
    primaryContainer = AzulPrincipal,
    onPrimaryContainer = Branco,
    secondary = Dourado,
    onSecondary = AzulEscuro,
    secondaryContainer = Dourado,
    onSecondaryContainer = AzulEscuro,
    tertiary = AzulSuave,
    onTertiary = AzulEscuro,
    background = FundoApp,
    onBackground = Branco,
    surface = SuperficieApp,
    onSurface = Branco,
    surfaceVariant = SuperficieElevada,
    onSurfaceVariant = AzulSuave,
    outline = ContornoApp,
    outlineVariant = CinzaClaro,
    error = ErroApp,
    onError = Branco,
    errorContainer = ErroApp,
    onErrorContainer = Branco,
    scrim = CinzaEscuro
)

@Composable
fun FidelidadeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = FidelidadeColorScheme,
        typography = FidelidadeTypography,
        content = content
    )
}
