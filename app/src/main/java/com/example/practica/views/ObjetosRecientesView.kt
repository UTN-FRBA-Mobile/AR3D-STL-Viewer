package com.example.practica.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.practica.R
import com.example.practica.repository.eliminarObjetoVistoRecientemente

@Composable
fun ListaObjetosRecientes(
    objetosVistosRecientemente: List<String>,
    context: Context,
    objetoEliminado: MutableState<Boolean>
) {
    Text(
        modifier = Modifier
            .padding(0.dp, 16.dp, 0.dp, 0.dp),
        text = if(objetosVistosRecientemente.size > 0) "Últimas visualizaciones" else ""
    )
    Box (
        modifier = Modifier
            .padding(0.dp, 8.dp, 0.dp, 0.dp)
            .fillMaxHeight()) {
        LazyColumn() {
            objetosVistosRecientemente.let {
                items(it.size) { index ->
                    ObjetoReciente(it[index], context, objetoEliminado)
                }
            }
        }
    }
}

@Composable
fun ObjetoReciente(nombreObjeto: String, context: Context, objetoEliminado: MutableState<Boolean>) {
    val verPopUp = remember { mutableStateOf(false) }
    Divider()
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(0.dp, 4.dp, 0.dp, 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = eliminarExtensionObj(nombreObjeto),
            fontWeight = FontWeight.Bold
        )
        Box {
            TextButton(
                modifier = Modifier,
                onClick = { lanzarVistaPreviaObjetoReciente(context, nombreObjeto) }
            ) {
                Text(text = "Ver")
            }
            TextButton(
                modifier = Modifier
                    .padding(60.dp, 0.dp, 0.dp, 0.dp),
                onClick = { verPopUp.value = true }
            ) {
                Icon(
                    Icons.Rounded.Delete,
                    contentDescription = stringResource(id = R.string.eliminacion_objeto_visto),
                    tint = Color.Red
                )
            }
        }
    }

    PopUp(
        verPopUp = verPopUp,
        onConfirmation = {
            verPopUp.value = false
            objetoEliminado.value = eliminarObjetoVistoRecientemente(nombreObjeto, context)
        },
        onDismissRequest = { verPopUp.value = false },
        textoConfirmation = "Si",
        dialogTitle = "Eliminación",
        dialogText = "Estás seguro que querés eliminar el objeto ${eliminarExtensionObj(nombreObjeto)}?",
        textoDismiss = "No",
    )
}

fun eliminarExtensionObj(nombreArchivoObj: String): String {
    return nombreArchivoObj.removeSuffix(".obj")
}