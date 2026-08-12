package com.treinamento.app_fidelidade.view.Authentication


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val BackgroundColor = Color(0xFF001427)
//private val FieldColor = Color(0xFF0D1F32)
//private val BorderColor = Color(0xFF1D3B57)
//private val PrimaryBlue = Color(0xFF007BFF)
//private val SecondaryBlue = Color(0xFF0065D9)
private val TextColor = Color(0xFFF4F7FB)
private val SecondaryTextColor = Color(0xFFB7C1CE)

@Composable
fun AuthenticationScreen() {
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        containerColor = BackgroundColor
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundColor)
                .padding(paddingValues)
                .padding(horizontal = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Bem-vindo!",
                color = TextColor,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Entre ou cadastre-se para\ncontinuar",
                color = SecondaryTextColor,
                fontSize = 16.sp,
                lineHeight = 21.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            AuthenticationTabSelector(
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )

            Spacer(modifier = Modifier.height(28.dp))

            when (selectedTab) {
                0 -> LoginScreen(
                    onRegisterClick = { selectedTab = 1 }
                )

                1 -> RegisterScreen(
                    onLoginClick = { selectedTab = 0 }
                )
            }
        }
    }
}
//
//@Composable
//private fun AuthenticationTabSelector(
//    selectedTab: Int,
//    onTabSelected: (Int) -> Unit
//) {
//    Row(
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(48.dp)
//            .clip(RoundedCornerShape(13.dp))
//            .border(
//                width = 1.dp,
//                color = BorderColor,
//                shape = RoundedCornerShape(13.dp)
//            )
//            .background(BackgroundColor)
//    ) {
//        AuthenticationTab(
//            text = "Login",
//            selected = selectedTab == 0,
//            onClick = { onTabSelected(0) },
//            modifier = Modifier.weight(1f)
//        )
//
//        AuthenticationTab(
//            text = "Cadastro",
//            selected = selectedTab == 1,
//            onClick = { onTabSelected(1) },
//            modifier = Modifier.weight(1f)
//        )
//    }
//}
//
//@Composable
// fun AuthenticationTab(
//    text: String,
//    selected: Boolean,
//    onClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val selectedBackground = Brush.horizontalGradient(
//        colors = listOf(
//            Color(0xFF098BFF),
//            Color(0xFF0068DE)
//        )
//    )
//
//    Box(
//        modifier = modifier
//            .fillMaxSize()
//            .then(
//                if (selected) {
//                    Modifier.background(
//                        brush = selectedBackground,
//                        shape = RoundedCornerShape(12.dp)
//                    )
//                } else {
//                    Modifier.background(Color.Transparent)
//                }
//            )
//            .clickable(onClick = onClick),
//        contentAlignment = Alignment.Center
//    ) {
//        Text(
//            text = text,
//            color = if (selected) TextColor else SecondaryTextColor,
//            fontSize = 15.sp,
//            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
//        )
//    }
//}

//@Composable
//private fun LoginContent(
//    onRegisterClick: () -> Unit
//) {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var passwordVisible by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        AuthenticationTextField(
//            value = email,
//            onValueChange = { email = it },
//            placeholder = "Email",
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Email,
//                    contentDescription = "E-mail",
//                    tint = SecondaryTextColor
//                )
//            },
//            keyboardType = KeyboardType.Email
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        AuthenticationTextField(
//            value = password,
//            onValueChange = { password = it },
//            placeholder = "Senha",
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Lock,
//                    contentDescription = "Senha",
//                    tint = SecondaryTextColor
//                )
//            },
//            trailingIcon = {
//                IconButton(
//                    onClick = {
//                        passwordVisible = !passwordVisible
//                    }
//                ) {
//                    Icon(
//                        imageVector = if (passwordVisible) {
//                            Icons.Default.VisibilityOff
//                        } else {
//                            Icons.Default.Visibility
//                        },
//                        contentDescription = if (passwordVisible) {
//                            "Ocultar senha"
//                        } else {
//                            "Mostrar senha"
//                        },
//                        tint = SecondaryTextColor
//                    )
//                }
//            },
//            visualTransformation = if (passwordVisible) {
//                VisualTransformation.None
//            } else {
//                PasswordVisualTransformation()
//            },
//            keyboardType = KeyboardType.Password
//        )
//
//        TextButton(
//            onClick = {
//                // TODO: navegar para recuperação de senha
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text(
//                text = "Esqueceu sua senha?",
//                color = PrimaryBlue,
//                fontSize = 14.sp
//            )
//        }
//
//        Spacer(modifier = Modifier.height(38.dp))
//
//        GradientButton(
//            text = "Entrar",
//            onClick = {
//                // TODO: realizar login
//            }
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "Ainda não tem conta?",
//                color = SecondaryTextColor,
//                fontSize = 14.sp
//            )
//
//            Text(
//                text = " Cadastre-se",
//                color = PrimaryBlue,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.clickable(onClick = onRegisterClick)
//            )
//        }
//    }
//}
//
//@Composable
//private fun RegisterContent(
//    onLoginClick: () -> Unit
//) {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//
//    var passwordVisible by remember { mutableStateOf(false) }
//    var confirmPasswordVisible by remember { mutableStateOf(false) }
//
//    Column(
//        modifier = Modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        AuthenticationTextField(
//            value = name,
//            onValueChange = { name = it },
//            placeholder = "Nome",
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Person,
//                    contentDescription = "Nome",
//                    tint = SecondaryTextColor
//                )
//            }
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        AuthenticationTextField(
//            value = email,
//            onValueChange = { email = it },
//            placeholder = "Email",
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Email,
//                    contentDescription = "E-mail",
//                    tint = SecondaryTextColor
//                )
//            },
//            keyboardType = KeyboardType.Email
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        AuthenticationTextField(
//            value = password,
//            onValueChange = { password = it },
//            placeholder = "Senha",
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Lock,
//                    contentDescription = "Senha",
//                    tint = SecondaryTextColor
//                )
//            },
//            trailingIcon = {
//                PasswordVisibilityButton(
//                    passwordVisible = passwordVisible,
//                    onClick = {
//                        passwordVisible = !passwordVisible
//                    }
//                )
//            },
//            visualTransformation = if (passwordVisible) {
//                VisualTransformation.None
//            } else {
//                PasswordVisualTransformation()
//            },
//            keyboardType = KeyboardType.Password
//        )
//
//        Spacer(modifier = Modifier.height(10.dp))
//
//        AuthenticationTextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            placeholder = "Confirmar senha",
//            leadingIcon = {
//                Icon(
//                    imageVector = Icons.Default.Lock,
//                    contentDescription = "Confirmar senha",
//                    tint = SecondaryTextColor
//                )
//            },
//            trailingIcon = {
//                PasswordVisibilityButton(
//                    passwordVisible = confirmPasswordVisible,
//                    onClick = {
//                        confirmPasswordVisible = !confirmPasswordVisible
//                    }
//                )
//            },
//            visualTransformation = if (confirmPasswordVisible) {
//                VisualTransformation.None
//            } else {
//                PasswordVisualTransformation()
//            },
//            keyboardType = KeyboardType.Password
//        )
//
//        Spacer(modifier = Modifier.height(30.dp))
//
//        GradientButton(
//            text = "Cadastrar",
//            onClick = {
//                // TODO: realizar cadastro
//            }
//        )
//
//        Spacer(modifier = Modifier.height(20.dp))
//
//        Row(
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.Center
//        ) {
//            Text(
//                text = "Já possui uma conta?",
//                color = SecondaryTextColor,
//                fontSize = 14.sp
//            )
//
//            Text(
//                text = " Entrar",
//                color = PrimaryBlue,
//                fontSize = 14.sp,
//                fontWeight = FontWeight.SemiBold,
//                modifier = Modifier.clickable(onClick = onLoginClick)
//            )
//        }
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//private fun AuthenticationTextField(
//    value: String,
//    onValueChange: (String) -> Unit,
//    placeholder: String,
//    leadingIcon: @Composable (() -> Unit)? = null,
//    trailingIcon: @Composable (() -> Unit)? = null,
//    visualTransformation: VisualTransformation = VisualTransformation.None,
//    keyboardType: KeyboardType = KeyboardType.Text
//) {
//    OutlinedTextField(
//        value = value,
//        onValueChange = onValueChange,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(62.dp),
//        placeholder = {
//            Text(
//                text = placeholder,
//                color = SecondaryTextColor,
//                fontSize = 16.sp
//            )
//        },
//        leadingIcon = leadingIcon,
//        trailingIcon = trailingIcon,
//        singleLine = true,
//        shape = RoundedCornerShape(13.dp),
//        keyboardOptions = KeyboardOptions(
//            keyboardType = keyboardType
//        ),
//        visualTransformation = visualTransformation,
//        colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
//            focusedTextColor = TextColor,
//            unfocusedTextColor = TextColor,
//            cursorColor = PrimaryBlue,
//            focusedContainerColor = FieldColor,
//            unfocusedContainerColor = FieldColor,
//            focusedBorderColor = PrimaryBlue,
//            unfocusedBorderColor = BorderColor
//        )
//    )
//}

//@Composable
//fun PasswordVisibilityButton(
//    passwordVisible: Boolean,
//    onClick: () -> Unit
//) {
//    IconButton(onClick = onClick) {
//        Icon(
//            imageVector = if (passwordVisible) {
//                Icons.Default.VisibilityOff
//            } else {
//                Icons.Default.Visibility
//            },
//            contentDescription = if (passwordVisible) {
//                "Ocultar senha"
//            } else {
//                "Mostrar senha"
//            },
//            tint = SecondaryTextColor
//        )
//    }
//}

//@Composable
//private fun GradientButton(
//    text: String,
//    onClick: () -> Unit
//) {
//    val buttonGradient = Brush.horizontalGradient(
//        colors = listOf(
//            Color(0xFF0088FF),
//            Color(0xFF0067DA)
//        )
//    )
//
//    Button(
//        onClick = onClick,
//        modifier = Modifier
//            .fillMaxWidth()
//            .height(56.dp),
//        shape = RoundedCornerShape(12.dp),
//        colors = ButtonDefaults.buttonColors(
//            containerColor = Color.Transparent
//        ),
//        contentPadding = androidx.compose.foundation.layout.PaddingValues()
//    ) {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(
//                    brush = buttonGradient,
//                    shape = RoundedCornerShape(12.dp)
//                ),
//            contentAlignment = Alignment.Center
//        ) {
//            Text(
//                text = text,
//                color = Color.White,
//                fontSize = 17.sp,
//                fontWeight = FontWeight.Bold
//            )
//        }
//    }
//}

            // Content based on selected tab
//            when (selectedTab) {
//                0 -> LoginScreen()
//                1 -> RegisterScreen()
//            }
//        }
//    }
//}
