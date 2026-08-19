package com.treinamento.app_fidelidade.view.authentication


import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Log
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.rotas.Rotas
import com.treinamento.app_fidelidade.ui.components.util.rememberNetworkConnection
import kotlinx.coroutines.flow.first
import java.lang.Thread.sleep

@Composable
fun LoyaltySplashScreen(
    navController: NavHostController,
    repository: UsuarioDBRepository
    ) {

    val snackbarHostState = remember {
        SnackbarHostState()
    }


    val isConnected = rememberNetworkConnection()


    LaunchedEffect(repository) {
        try {

//            sleep(3000) // Simula um atraso de 2 segundos para a tela de splash
            val usuario = repository.first.first()

            Log.d("SPLASH", "Usuário encontrado: $usuario")

            val destino = if (usuario != null) {
                Rotas.FIDELIDADE
            } else {
                Rotas.AUTHENTICATION
            }

            navController.navigate(destino) {
                launchSingleTop = true
            }
        } catch (exception: Exception) {
            Log.e(
                "SPLASH",
                "Erro ao consultar usuário no SQLite",
                exception
            )

            navController.navigate(Rotas.AUTHENTICATION) {
                launchSingleTop = false
            }
        }
    }

    LaunchedEffect(isConnected) {

        val mensagem = if (isConnected) {
            "Internet conectada"
        } else {
            "Sem conexão com a internet"
        }
        snackbarHostState.showSnackbar(
            message = mensagem
        )
    }


    // Animação de pulso para o ícone
    val infiniteTransition = rememberInfiniteTransition()
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    // Animação de fade para o texto
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    Scaffold(
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF1A237E), // Azul escuro
                            Color(0xFF0D47A1)  // Azul mais escuro
                        )
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.padding(32.dp)
            ) {
                // Ícone do App - Substitua por seu ícone
                // Se não tiver ícone, use um círculo com inicial
                Surface(
                    modifier = Modifier
                        .size(150.dp)
                        .graphicsLayer(scaleX = scale, scaleY = scale),
                    shape = CircleShape,
                    color = Color(0xFFFFD700)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "F+",
                            fontSize = 60.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Nome do App com animação
                Text(
                    text = "Fidelidade+",
                    fontSize = 36.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.graphicsLayer(alpha = alpha),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Slogan
                Text(
                    text = "Ganhe pontos, troque por produtos!",
                    fontSize = 16.sp,
                    color = Color(0xFFB3E5FC),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.graphicsLayer(alpha = alpha * 0.8f)
                )

                Spacer(modifier = Modifier.height(48.dp))

                // Loading indicator
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 3.dp,
                    modifier = Modifier.size(40.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Texto de carregamento
                Text(
                    text = "Carregando dados...",
                    fontSize = 14.sp,
                    color = Color(0xFFB3E5FC),
                    modifier = Modifier.graphicsLayer(alpha = alpha)
                )
            }
        }
    }
}