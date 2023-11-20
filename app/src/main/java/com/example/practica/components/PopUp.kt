package com.example.practica.components

import android.text.Layout.Alignment
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.practica.R

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
            Row(
                verticalAlignment = androidx.compose.ui.Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Rounded.Warning,
                    modifier = Modifier.size(30.dp),
                    contentDescription = stringResource(id = R.string.menu_objeto_reciente)
                )
                Text(
                    modifier = Modifier.padding(start = 5.dp),
                    text = dialogTitle
                )
            }
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