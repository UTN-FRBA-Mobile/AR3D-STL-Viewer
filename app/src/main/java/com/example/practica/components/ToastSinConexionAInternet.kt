package com.example.practica.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.zIndex
import com.example.practica.R
import kotlinx.coroutines.delay

@Composable
fun Toast(texto: String, color: Color, esVisible: () -> Boolean = {true}, onDismiss: () -> Unit = {}) {
    val toastVisible = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        toastVisible.value = esVisible()
        delay(5000)
        toastVisible.value = false
        onDismiss()
    }

    if(toastVisible.value) {
        ToastMensaje(toastVisible, texto, color, onDismiss)
    }
}

@Composable
fun ToastMensaje(
    toastVisible: MutableState<Boolean>,
    texto: String,
    color: Color,
    onDismiss: () -> Unit
) {
    Box(modifier =  Modifier
        .fillMaxSize()
        .zIndex(1f)
    ) {
        Snackbar(
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.BottomCenter),
            contentColor = Color.White,
            containerColor = color,
            action = {
                TextButton(
                    onClick = {
                        toastVisible.value = false
                        onDismiss()
                    }
                ) {
                    Icon(
                        Icons.Rounded.Close,
                        contentDescription = stringResource(id = R.string.cerrar_toast),
                        tint = Color.White
                    )
                }
            }
        ) {
            Text(texto)
        }
    }
}