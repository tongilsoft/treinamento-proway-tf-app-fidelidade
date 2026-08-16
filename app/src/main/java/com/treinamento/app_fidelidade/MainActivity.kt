package com.treinamento.app_fidelidade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.treinamento.app_fidelidade.ui.theme.FidelidadeTheme

import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.treinamento.app_fidelidade.view.authentication.AuthenticationScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FidelidadeTheme {
                FidelidadeApp()
            }
        }
    }
}
