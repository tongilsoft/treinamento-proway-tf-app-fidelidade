package com.treinamento.app_fidelidade.view.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.treinamento.app_fidelidade.model.UsuarioLogin
import com.treinamento.app_fidelidade.rotas.Rotas
import com.treinamento.app_fidelidade.viewmodel.AuthenticationViewModel

private val LoginFieldColor = Color(0xFF0D2236)
private val LoginBorderColor = Color(0xFF1D4A6C)
private val LoginPrimaryBlue = Color(0xFF0788F8)
private val LoginTextColor = Color(0xFFF4F7FB)
private val LoginSecondaryTextColor = Color(0xFFB7C1CE)

@Composable
fun LoginScreen(
    viewModel: AuthenticationViewModel,
    navController: NavHostController,
    onLoginClick: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }


    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!viewModel.isLoading) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Email",
                            color = LoginSecondaryTextColor,
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "E-mail",
                            tint = LoginSecondaryTextColor
                        )
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email
                    ),
                    colors = loginTextFieldColors()
                )

                Spacer(
                    modifier = Modifier.height(10.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    placeholder = {
                        Text(
                            text = "Senha",
                            color = LoginSecondaryTextColor,
                            fontSize = 15.sp
                        )
                    },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Senha",
                            tint = LoginSecondaryTextColor
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                passwordVisible = !passwordVisible
                            }
                        ) {
                            Icon(
                                imageVector = if (passwordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (passwordVisible) {
                                    "Ocultar senha"
                                } else {
                                    "Exibir senha"
                                },
                                tint = LoginSecondaryTextColor
                            )
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    },
                    colors = loginTextFieldColors()
                )

                Spacer(
                    modifier = Modifier.height(34.dp)
                )

                Button(
                    onClick = {
                        if (email.isBlank()) {
                            onShowMessage("Informe o e-mail")
                            return@Button
                        }

                        if (password.isBlank()) {
                            onShowMessage("Informe a senha")
                            return@Button
                        }
                        viewModel.doLogin(
                            UsuarioLogin(email, password)
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(11.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent
                    ),
                    contentPadding = PaddingValues()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = LoginPrimaryBlue,
                                shape = RoundedCornerShape(11.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Entrar",
                            color = Color.White,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(
                    modifier = Modifier.height(20.dp)
                )

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Ainda não tem conta? ",
                        color = LoginSecondaryTextColor,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Cadastre-se",
                        color = LoginPrimaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(
                            onClick = {}//onRegisterClick
                        )
                    )
                }
            }
        } else {

            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Loading indicator
                CircularProgressIndicator(
                    color = Color.White,
                    strokeWidth = 5.dp,
                    modifier = Modifier.size(100.dp)
                )
                Spacer(modifier = Modifier.height(30.dp))

                Text(
                    text = "Carregando os Dados...",
                    color = LoginTextColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 30.dp)
                )

            }
        }
    }
}

@Composable
fun loginTextFieldColors() =
    OutlinedTextFieldDefaults.colors(
        focusedTextColor = LoginTextColor,
        unfocusedTextColor = LoginTextColor,
        focusedContainerColor = LoginFieldColor,
        unfocusedContainerColor = LoginFieldColor,
        focusedBorderColor = LoginPrimaryBlue,
        unfocusedBorderColor = LoginBorderColor,
        cursorColor = LoginPrimaryBlue,
        focusedLeadingIconColor = LoginSecondaryTextColor,
        unfocusedLeadingIconColor = LoginSecondaryTextColor,
        focusedTrailingIconColor = LoginSecondaryTextColor,
        unfocusedTrailingIconColor = LoginSecondaryTextColor
    )