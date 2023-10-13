package com.example.practica.views

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.practica.R
import com.example.practica.utils.hayConexionAInternet
import kotlinx.coroutines.delay

@Composable
fun MensajeSinConexionAInternet(paddingTop: Dp) {
    val context = LocalContext.current
    val toastVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        delay(1000)
        toastVisible.value = !hayConexionAInternet(context = context)
    }

    if(toastVisible.value) {
        ToastSinInternet(toastVisible, paddingTop)
    }
}

@Composable
fun ToastSinInternet(toastVisible: MutableState<Boolean>, paddingTop: Dp) {
    Box(
        modifier = Modifier
            .absolutePadding(top = paddingTop)
    ) {
        Snackbar(
            modifier = Modifier
                .padding(16.dp),
            contentColor = Color.White,
            containerColor = Color.Red,
            action = {
                TextButton(
                    onClick = { toastVisible.value = false }
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.cerrar_toast_conexion_a_internet),
                        tint = Color.White
                    )
                }
            }
        ) {
            Text("Sin conexión a internet!")
        }
    }
}