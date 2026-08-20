package com.treinamento.app_fidelidade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.rememberNavController
import com.treinamento.app_fidelidade.data.local.config.AppDatabase
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.data.repository.db.UsuarioRepositoryImpl
import com.treinamento.app_fidelidade.rotas.NavGraph
import com.treinamento.app_fidelidade.ui.theme.FidelidadeTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val dao = AppDatabase.getDatabase(context = applicationContext).usuarioDao()
            val repository: UsuarioDBRepository = UsuarioRepositoryImpl(dao)
//            val factory = AlunoViewModelFactory(repository)

            FidelidadeTheme {
                NavGraph(navController, repository)
                //AuthenticationScreen(rememberNavController())
//                FidelidadeApp()

            }
        }
    }
}


@Composable
fun MainNavigation() {
    var currentScreen by remember { mutableStateOf("authentication") }

//    when (currentScreen) {
//        "authentication" -> AuthenticationScreen(
//            onAuthSuccess = { currentScreen = "home" }
//        )
//        "home" -> HomeScreen()
//    }
}

