package com.treinamento.app_fidelidade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.treinamento.app_fidelidade.rotas.NavGraph
import com.treinamento.app_fidelidade.ui.theme.FidelidadeTheme
import com.treinamento.app_fidelidade.view.authentication.AuthenticationScreen
import com.treinamento.app_fidelidade.view.fidelidade.FidelidadeApp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            FidelidadeTheme {
                NavGraph(navController)
            }
        }
    }
}