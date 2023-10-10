package com.example.practica.views

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.practica.repository.eliminarObjetoVistoRecientemente

@Composable
fun PopUp(
    verPopUp: MutableState<Boolean>,
    context: Context,
    nombreObjeto: String,
    objetoEliminado: MutableState<Boolean>
) {
    when {
        verPopUp.value -> {
            AlertDialogExample(
                onDismissRequest = { verPopUp.value = false },
                onConfirmation = {
                    verPopUp.value = false
                    val archivoEliminado = eliminarObjetoVistoRecientemente(nombreObjeto, context)
                    objetoEliminado.value = archivoEliminado
                },
                dialogTitle = "Eliminación",
                dialogText = "Estás seguro que querés eliminar el objeto ${nombreObjeto}?",
                icon = Icons.Default.Info
            )
        }
    }
}

@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text("Si")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("No")
            }
        }
    )
}