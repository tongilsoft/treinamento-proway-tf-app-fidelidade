package com.treinamento.app_fidelidade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.*
import com.treinamento.app_fidelidade.ui.theme.App_FidelidadeTheme
import com.treinamento.app_fidelidade.view.authentication.AuthenticationScreen
import com.treinamento.app_fidelidade.view.home.HomeScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_FidelidadeTheme {
//                MainNavigation()
                HomeScreen()
            }
        }
    }
}

@Composable
fun MainNavigation() {
    var currentScreen by remember { mutableStateOf("authentication") }

    when (currentScreen) {
        "authentication" -> AuthenticationScreen(
            onAuthSuccess = { currentScreen = "home" }
        )
        "home" -> HomeScreen()
    }
}
