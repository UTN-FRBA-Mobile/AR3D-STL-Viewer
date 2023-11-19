package com.example.practica.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState

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