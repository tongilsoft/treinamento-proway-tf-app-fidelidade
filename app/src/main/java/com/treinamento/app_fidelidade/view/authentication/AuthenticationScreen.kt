package com.treinamento.app_fidelidade.view.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.treinamento.app_fidelidade.viewmodel.AuthenticationViewModel
import kotlinx.coroutines.launch

private val BackgroundColor = Color(0xFF001427)
private val PrimaryBlue = Color(0xFF0788F8)
private val BorderColor = Color(0xFF1D4A6C)
private val TextColor = Color(0xFFF4F7FB)
private val SecondaryTextColor = Color(0xFFB7C1CE)

private const val LOGIN_TAB = 0
private const val REGISTER_TAB = 1

@Composable
fun AuthenticationScreen(
    viewModel: AuthenticationViewModel = viewModel()
) {
    var selectedTab by remember {
        mutableIntStateOf(LOGIN_TAB)
    }

    val snackbarHostState = remember {
        SnackbarHostState()
    }

    val coroutineScope = rememberCoroutineScope()

    /*
     * Como usuario e errorMessage são estados do Compose,
     * a tela será recomposta quando eles forem alterados.
     */
    val authenticatedUser = viewModel.usuario
    val errorMessage = viewModel.errorMessage

    /*
     * Este efeito será executado quando o usuário autenticado for alterado.
     *
     * A navegação só acontece depois que o backend retorna sucesso
     * e o ViewModel atribui response.data para usuario.
     */
    LaunchedEffect(authenticatedUser) {
        if (authenticatedUser != null) {

        }
    }

    /*
     * Observa as mensagens produzidas pelo ViewModel.
     */
    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            snackbarHostState.showSnackbar(
                message = message
            )

            viewModel.clearErrorMessage()
        }
    }

    Scaffold(
        containerColor = BackgroundColor,
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState
            )
        }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 46.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(
                modifier = Modifier.height(82.dp)
            )

            Text(
                text = "Bem-vindo!",
                color = TextColor,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(
                modifier = Modifier.height(8.dp)
            )

            Text(
                text = "Entre ou cadastre-se para\ncontinuar",
                color = SecondaryTextColor,
                fontSize = 15.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            )

            Spacer(
                modifier = Modifier.height(30.dp)
            )

            AuthenticationTabSelector(
                selectedTab = selectedTab,
                onTabSelected = { tab: Int ->
                    /*
                     * Impede a troca de tab enquanto uma requisição
                     * estiver em andamento.
                     */
                    if (!viewModel.isLoading) {
                        selectedTab = tab
                    }
                }
            )

            Spacer(
                modifier = Modifier.height(26.dp)
            )

            when (selectedTab) {
                LOGIN_TAB -> {
                    LoginScreen(
                        viewModel = viewModel,
                        onLoginClick = {
                            if (!viewModel.isLoading) {
                                selectedTab = REGISTER_TAB
                            }
                        },
                        onShowMessage = { message: String ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = message
                                )
                            }
                        }
                    )
                }

                REGISTER_TAB -> {
                    RegisterScreen(
                        viewModel = viewModel,
                        onRegisterClick = {
                            if (!viewModel.isLoading) {
                                selectedTab = LOGIN_TAB
                            }
                        },
                        onShowMessage = { message: String ->
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar(
                                    message = message
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun AuthenticationTabSelector(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabShape = RoundedCornerShape(12.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
            .clip(tabShape)
            .border(
                width = 1.dp,
                color = BorderColor,
                shape = tabShape
            )
    ) {
        AuthenticationTab(
            text = "Login",
            selected = selectedTab == LOGIN_TAB,
            onClick = {
                onTabSelected(LOGIN_TAB)
            },
            modifier = Modifier.weight(1f)
        )

        AuthenticationTab(
            text = "Cadastro",
            selected = selectedTab == REGISTER_TAB,
            onClick = {
                onTabSelected(REGISTER_TAB)
            },
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun AuthenticationTab(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .clip(RoundedCornerShape(11.dp))
            .background(
                color = if (selected) {
                    PrimaryBlue
                } else {
                    Color.Transparent
                }
            )
            .clickable(
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = TextColor,
            fontSize = 14.sp,
            fontWeight = if (selected) {
                FontWeight.Bold
            } else {
                FontWeight.Normal
            }
        )
    }
}