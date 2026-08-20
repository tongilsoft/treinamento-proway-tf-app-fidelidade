package com.treinamento.app_fidelidade.rotas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.treinamento.app_fidelidade.data.repository.db.UsuarioDBRepository
import com.treinamento.app_fidelidade.view.authentication.AuthenticationScreen
import com.treinamento.app_fidelidade.view.authentication.LoyaltySplashScreen
import com.treinamento.app_fidelidade.view.fidelidade.FidelidadeApp

@Composable
fun NavGraph(navController: NavHostController,repository: UsuarioDBRepository){

    NavHost (
        navController = navController,
        startDestination = Rotas.SPLASH
    ){
        composable(Rotas.SPLASH){ LoyaltySplashScreen(navController = navController, repository = repository) }
        composable(Rotas.AUTHENTICATION){ AuthenticationScreen(navController = navController, repository = repository) }
        composable(Rotas.FIDELIDADE){ FidelidadeApp(navController = navController, repository = repository) }

    }
}
