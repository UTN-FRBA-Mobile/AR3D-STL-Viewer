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
import androidx.navigation.NavHostController
import com.example.practica.repository.eliminarObjetoVistoRecientemente


@Composable
fun PopUp(
    verPopUp: MutableState<Boolean>,
    onConfirmation: () -> Unit,
    textoConfirmation: String,
    dialogTitle: String,
    dialogText: String,
    onDismissRequest: () -> Unit = {},
    textoDismiss: String = ""
) {
    when {
        verPopUp.value -> {
            AlertDialog(
                onDismissRequest = {onDismissRequest()},
                onConfirmation = {onConfirmation()},
                textoConfirmation = textoConfirmation,
                textoDismiss = textoDismiss,
                dialogTitle = dialogTitle,
                dialogText = dialogText
            )
        }
    }
}

@Composable
fun AlertDialog(
    onDismissRequest: () -> Unit?,
    onConfirmation: () -> Unit,
    textoConfirmation: String,
    textoDismiss: String,
    dialogTitle: String,
    dialogText: String,
) {
    AlertDialog(
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
                Text(textoConfirmation)
            }
        },
        dismissButton = {
            if(textoDismiss != "") {
                TextButton(
                    onClick = {
                        onDismissRequest()
                    }
                ) {
                    Text(textoDismiss)
                }
            }
        }
    )
}