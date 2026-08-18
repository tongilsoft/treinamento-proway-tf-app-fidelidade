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
import androidx.compose.material.icons.filled.Person
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
import com.treinamento.app_fidelidade.data.remote.dto.request.UsuarioRegistro
import com.treinamento.app_fidelidade.model.UsuarioLogin
import com.treinamento.app_fidelidade.viewmodel.AuthenticationViewModel
import java.math.BigInteger

private val RegisterFieldColor = Color(0xFF0D2236)
private val RegisterBorderColor = Color(0xFF1D4A6C)
private val RegisterPrimaryBlue = Color(0xFF0788F8)
private val RegisterTextColor = Color(0xFFF4F7FB)
private val LoginTextColor = Color(0xFFF4F7FB)
private val RegisterSecondaryTextColor = Color(0xFFB7C1CE)

@Composable
fun RegisterScreen(
    viewModel: AuthenticationViewModel,
    navController: NavHostController,
    onRegisterClick: () -> Unit,
    onShowMessage: (String) -> Unit
) {
    var name by remember {
        mutableStateOf("")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var confirmPassword by remember {
        mutableStateOf("")
    }

    var passwordVisible by remember {
        mutableStateOf(false)
    }

    var confirmPasswordVisible by remember {
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

                RegisterTextField(
                    value = name,
                    onValueChange = {
                        name = it
                    },
                    placeholder = "Nome",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Nome",
                            tint = RegisterSecondaryTextColor
                        )
                    },
                    keyboardType = KeyboardType.Text
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                RegisterTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    placeholder = "Email",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "E-mail",
                            tint = RegisterSecondaryTextColor
                        )
                    },
                    keyboardType = KeyboardType.Email
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                RegisterTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    placeholder = "Senha",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Senha",
                            tint = RegisterSecondaryTextColor
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
                                tint = RegisterSecondaryTextColor
                            )
                        }
                    },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (passwordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }
                )

                Spacer(
                    modifier = Modifier.height(9.dp)
                )

                RegisterTextField(
                    value = confirmPassword,
                    onValueChange = {
                        confirmPassword = it
                    },
                    placeholder = "Confirmar senha",
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Confirmar senha",
                            tint = RegisterSecondaryTextColor
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                confirmPasswordVisible = !confirmPasswordVisible
                            }
                        ) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (confirmPasswordVisible) {
                                    "Ocultar confirmação da senha"
                                } else {
                                    "Exibir confirmação da senha"
                                },
                                tint = RegisterSecondaryTextColor
                            )
                        }
                    },
                    keyboardType = KeyboardType.Password,
                    visualTransformation = if (confirmPasswordVisible) {
                        VisualTransformation.None
                    } else {
                        PasswordVisualTransformation()
                    }
                )

                Spacer(
                    modifier = Modifier.height(26.dp)
                )

                Button(
                    onClick = {
                        when {
                            name.isBlank() -> {
                                onShowMessage("Informe o nome")
                            }

                            email.isBlank() -> {
                                onShowMessage("Informe o e-mail")
                            }

                            password.isBlank() -> {
                                onShowMessage("Informe a senha")
                            }

                            confirmPassword.isBlank() -> {
                                onShowMessage("Confirme a senha")
                            }

                            password != confirmPassword -> {
                                onShowMessage("As senhas não são iguais")
                            }

                            else -> {
                                viewModel.doRegister(
                                    UsuarioRegistro(
                                        null, name,
                                        email, password,
                                        BigInteger.ZERO,
                                        null,
                                        null, null
                                    ), navController
                                )
                            }
                        }
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
                                color = RegisterPrimaryBlue,
                                shape = RoundedCornerShape(11.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Cadastrar",
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
                        text = "Já possui uma conta? ",
                        color = RegisterSecondaryTextColor,
                        fontSize = 13.sp
                    )

                    Text(
                        text = "Entrar",
                        color = RegisterPrimaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.clickable(
                            onClick = {}//onLoginClick
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
                    text = "Registrando os Dados...",
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
private fun RegisterTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIcon: @Composable (() -> Unit),
    keyboardType: KeyboardType,
    trailingIcon: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .height(55.dp),
        placeholder = {
            Text(
                text = placeholder,
                color = RegisterSecondaryTextColor,
                fontSize = 15.sp
            )
        },
        leadingIcon = leadingIcon,
        trailingIcon = trailingIcon,
        singleLine = true,
        shape = RoundedCornerShape(12.dp),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType
        ),
        visualTransformation = visualTransformation,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = RegisterTextColor,
            unfocusedTextColor = RegisterTextColor,
            focusedContainerColor = RegisterFieldColor,
            unfocusedContainerColor = RegisterFieldColor,
            focusedBorderColor = RegisterPrimaryBlue,
            unfocusedBorderColor = RegisterBorderColor,
            cursorColor = RegisterPrimaryBlue,
            focusedLeadingIconColor = RegisterSecondaryTextColor,
            unfocusedLeadingIconColor = RegisterSecondaryTextColor,
            focusedTrailingIconColor = RegisterSecondaryTextColor,
            unfocusedTrailingIconColor = RegisterSecondaryTextColor
        )
    )
}