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
fun RegisterScreen(
    onLoginClick: () -> Unit,
    onRegisterSuccess: () -> Unit
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
            placeholder = stringResource(id = R.string.name_placeholder),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = stringResource(id = R.string.name_placeholder),
                    tint = SecondaryTextColor
                )
            }
        )

        Spacer(modifier = Modifier.height(10.dp))

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
            placeholder = stringResource(id = R.string.confirm_password_placeholder),
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = stringResource(id = R.string.confirm_password_placeholder),
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
            text = stringResource(id = R.string.register_button),
            onClick = onRegisterSuccess
        )

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.already_have_account),
                color = SecondaryTextColor,
                fontSize = 14.sp
            )

            Text(
                text = stringResource(id = R.string.login_action),
                color = PrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable(onClick = onLoginClick)
            )
        }
    }
}
