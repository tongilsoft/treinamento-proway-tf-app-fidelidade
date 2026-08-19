package com.treinamento.app_fidelidade.ui.components.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.util.Log
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged

class NetworkMonitor(
    context: Context
) {

    private val connectivityManager =
        context.applicationContext.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager

    val isConnected: Flow<Boolean> = callbackFlow {

        fun verificarConexaoAtual(): Boolean {
            val network = connectivityManager.activeNetwork
//                ?: return false

            Log.d("NETWORK", "network=$network")

            val capabilities =
                connectivityManager.getNetworkCapabilities(network)
                    ?: return false


            Log.d(
                "NETWORK",
                """
                INTERNET=${capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)}
                VALIDATED=${capabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)}
                WIFI=${capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)}
                CELLULAR=${capabilities?.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)}
                """.trimIndent()
            )


            return capabilities.hasCapability(
                NetworkCapabilities.NET_CAPABILITY_INTERNET
            )
        }

        val callback =
            object : ConnectivityManager.NetworkCallback() {

                override fun onAvailable(network: Network) {
                    trySend(verificarConexaoAtual())
                }

                override fun onLost(network: Network) {
                    trySend(verificarConexaoAtual())
                }

                override fun onCapabilitiesChanged(
                    network: Network,
                    networkCapabilities: NetworkCapabilities
                ) {
//                    val conectado =
//                        networkCapabilities.hasCapability(
//                            NetworkCapabilities.NET_CAPABILITY_INTERNET
//                        ) && networkCapabilities.hasCapability(
//                            NetworkCapabilities.NET_CAPABILITY_VALIDATED
//                        )
//
//                    trySend(conectado)

                    val conectado =
                    networkCapabilities.hasCapability(
                                NetworkCapabilities.NET_CAPABILITY_INTERNET
                    )
                    Log.d(
                        "NETWORK_CALLBACK",
                        "conectado=$conectado"
                    )
                    trySend(conectado)
                }
            }

        trySend(verificarConexaoAtual())

        connectivityManager.registerDefaultNetworkCallback(callback)

        awaitClose {
            connectivityManager.unregisterNetworkCallback(callback)
        }
    }.distinctUntilChanged()
}