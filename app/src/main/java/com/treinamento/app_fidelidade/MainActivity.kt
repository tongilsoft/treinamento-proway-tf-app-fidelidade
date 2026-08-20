package com.treinamento.app_fidelidade

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.treinamento.app_fidelidade.data.local.config.AppDatabase
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.data.repository.db.UsuarioRepositoryImpl
import com.treinamento.app_fidelidade.rotas.NavGraph
import com.treinamento.app_fidelidade.ui.theme.FidelidadeTheme

import kotlinx.coroutines.flow.first

import com.treinamento.app_fidelidade.view.authentication.AuthenticationScreen
import com.treinamento.app_fidelidade.view.fidelidade.FidelidadeApp


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