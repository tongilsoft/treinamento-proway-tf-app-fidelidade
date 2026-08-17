package com.treinamento.app_fidelidade.rotas

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.treinamento.app_fidelidade.view.authentication.AuthenticationScreen
import com.treinamento.app_fidelidade.view.fidelidade.FidelidadeApp


@Composable
fun NavGraph(navController: NavHostController){

    NavHost (
        navController = navController,
        startDestination = Rotas.AUTHENTICATION
    ){
        composable(Rotas.AUTHENTICATION){ AuthenticationScreen(navController = navController) }
        composable(Rotas.FIDELIDADE){ FidelidadeApp() }
    }
}
