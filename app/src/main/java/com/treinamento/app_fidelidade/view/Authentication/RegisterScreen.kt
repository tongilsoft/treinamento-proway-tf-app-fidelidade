package com.treinamento.app_fidelidade.view.Authentication


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
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


private val BackgroundColor = Color(0xFF001427)
private val FieldColor = Color(0xFF0D1F32)
private val BorderColor = Color(0xFF1D3B57)
private val PrimaryBlue = Color(0xFF007BFF)
private val SecondaryBlue = Color(0xFF0065D9)
private val TextColor = Color(0xFFF4F7FB)
private val SecondaryTextColor = Color(0xFFB7C1CE)

@Composable
fun RegisterScreen(
    onLoginClick: () -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var passwordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthenticationTextField(
            value = name,
            onValueChange = { name = it },
            placeholder = "Nome",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Nome",
                    tint = SecondaryTextColor
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

        AuthenticationTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = "Email",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "E-mail",
                    tint = SecondaryTextColor
                )
            },
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(10.dp))

        AuthenticationTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = "Senha",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Senha",
                    tint = SecondaryTextColor
                )
            },
            trailingIcon = {
                PasswordVisibilityButton(
                    passwordVisible = passwordVisible,
                    onClick = {
                        passwordVisible = !passwordVisible
                    }
                )
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(10.dp))

        AuthenticationTextField(
            value = confirmPassword,
            onValueChange = { confirmPassword = it },
            placeholder = "Confirmar senha",
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Confirmar senha",
                    tint = SecondaryTextColor
                )
            },
            trailingIcon = {
                PasswordVisibilityButton(
                    passwordVisible = confirmPasswordVisible,
                    onClick = {
                        confirmPasswordVisible = !confirmPasswordVisible
                    }
                )
            },
            visualTransformation = if (confirmPasswordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardType = KeyboardType.Password
        )

        Spacer(modifier = Modifier.height(30.dp))

        GradientButton(
            text = "Cadastrar",
            onClick = {
                // TODO: realizar cadastro
            }
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Já possui uma conta?",
                color = SecondaryTextColor,
                fontSize = 14.sp
            )

            Text(
                text = " Entrar",
                color = PrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }
    }
}
//@Composable
//fun RegisterScreen() {
//    var name by remember { mutableStateOf("") }
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var confirmPassword by remember { mutableStateOf("") }
//    var registerMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 32.dp, vertical = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Create Account",
//            fontSize = 24.sp,
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        OutlinedTextField(
//            value = name,
//            onValueChange = { name = it },
//            label = { Text("Full Name") },
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = { email = it },
//            label = { Text("Email") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = password,
//            onValueChange = { password = it },
//            label = { Text("Password") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        OutlinedTextField(
//            value = confirmPassword,
//            onValueChange = { confirmPassword = it },
//            label = { Text("Confirm Password") },
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//            visualTransformation = PasswordVisualTransformation(),
//            modifier = Modifier.fillMaxWidth(),
//            singleLine = true
//        )
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        Button(
//            onClick = {
//                // Handle registration logic here
//                when {
//                    name.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() -> {
//                        registerMessage = "Please fill all fields"
//                    }
//                    password != confirmPassword -> {
//                        registerMessage = "Passwords do not match"
//                    }
//                    password.length < 6 -> {
//                        registerMessage = "Password must be at least 6 characters"
//                    }
//                    else -> {
//                        registerMessage = "Registration successful!"
//                    }
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Register")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (registerMessage.isNotEmpty()) {
//            Text(
//                text = registerMessage,
//                color = if (registerMessage.contains("successful"))
//                    MaterialTheme.colorScheme.primary
//                else
//                    MaterialTheme.colorScheme.error
//            )
//        }
//    }
//}