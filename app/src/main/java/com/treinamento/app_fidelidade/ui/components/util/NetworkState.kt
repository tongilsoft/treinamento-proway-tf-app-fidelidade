package com.treinamento.app_fidelidade.ui.components.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun rememberNetworkConnection(): Boolean {
    val context = LocalContext.current

    val networkMonitor = remember(context) {
        NetworkMonitor(context)
    }

    val isConnected by networkMonitor.isConnected
        .collectAsStateWithLifecycle(initialValue = false)

    return isConnected
}