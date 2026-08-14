package com.treinamento.app_fidelidade.view.authentication


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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.treinamento.app_fidelidade.R


private val BackgroundColor = Color(0xFF001427)
private val FieldColor = Color(0xFF0D1F32)
private val BorderColor = Color(0xFF1D3B57)
private val PrimaryBlue = Color(0xFF007BFF)
private val SecondaryBlue = Color(0xFF0065D9)
private val TextColor = Color(0xFFF4F7FB)
private val SecondaryTextColor = Color(0xFFB7C1CE)


@Composable
fun LoginScreen(
    onRegisterClick: () -> Unit,
    onLoginSuccess: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthenticationTextField(
            value = email,
            onValueChange = { email = it },
            placeholder = stringResource(id = R.string.email_placeholder),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = stringResource(id = R.string.email_placeholder),
                    tint = SecondaryTextColor
                )
            },
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(10.dp))

        AuthenticationTextField(
            value = password,
            onValueChange = { password = it },
            placeholder = stringResource(id = R.string.password_placeholder),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(id = R.string.password_placeholder),
                    tint = SecondaryTextColor
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
                            stringResource(id = R.string.hide_password)
                        } else {
                            stringResource(id = R.string.show_password)
                        },
                        tint = SecondaryTextColor
                    )
                }
            },
            visualTransformation = if (passwordVisible) {
                VisualTransformation.None
            } else {
                PasswordVisualTransformation()
            },
            keyboardType = KeyboardType.Password
        )

        TextButton(
            onClick = {
                // TODO: navegar para recuperação de senha
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = stringResource(id = R.string.forgot_password),
                color = PrimaryBlue,
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(38.dp))

        GradientButton(
            text = stringResource(id = R.string.login_button),
            onClick = onLoginSuccess
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.no_account),
                color = SecondaryTextColor,
                fontSize = 14.sp
            )

            Text(
                text = stringResource(id = R.string.register_action),
                color = PrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onRegisterClick)
            )
        }
    }
}

//@Composable
//fun LoginScreen() {
//    var email by remember { mutableStateOf("") }
//    var password by remember { mutableStateOf("") }
//    var loginMessage by remember { mutableStateOf("") }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(horizontal = 32.dp, vertical = 16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Text(
//            text = "Welcome Back!",
//            fontSize = 24.sp,
//            style = MaterialTheme.typography.headlineMedium
//        )
//
//        Spacer(modifier = Modifier.height(32.dp))
//
//        OutlinedTextField(
//            value = email,
//            onValueChange = { newValue -> email = newValue },
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
//            onValueChange = { newValue -> password = newValue },
//            label = { Text("Password") },
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
//                if (email.isNotEmpty() && password.isNotEmpty()) {
//                    loginMessage = "Login successful!"
//                } else {
//                    loginMessage = "Please fill all fields"
//                }
//            },
//            modifier = Modifier.fillMaxWidth()
//        ) {
//            Text("Login")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        if (loginMessage.isNotEmpty()) {
//            Text(
//                text = loginMessage,
//                color = if (loginMessage.contains("successful"))
//                    MaterialTheme.colorScheme.primary
//                else
//                    MaterialTheme.colorScheme.error
//            )
//        }
//    }
//}
