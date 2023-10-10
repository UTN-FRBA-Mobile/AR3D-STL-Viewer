package com.example.practica.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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


@Composable
fun ListaObjetosRecientes(
    objetosVistosRecientemente: List<String>,
    context: Context,
    objetoEliminado: MutableState<Boolean>
) {
    Text(
        modifier = Modifier
            .padding(16.dp, 50.dp, 0.dp, 0.dp),
        text = "Objetos vistos recientemente"
    )
    Box (
        modifier = Modifier
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .height(400.dp)) {
        LazyColumn(
        ) {
            objetosVistosRecientemente?.let {
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
    Card(
        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 8.dp, 16.dp, 8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp, 0.dp, 8.dp, 0.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = nombreObjeto, fontWeight = FontWeight.Bold)
            Box {
                ElevatedButton(
                    modifier = Modifier,
                    onClick = {lanzarVistaPreviaObjetoReciente(context, nombreObjeto)}
                ) {
                    Icon(
                        Icons.Rounded.PlayArrow,
                        contentDescription = stringResource(id = R.string.visualizar_objeto_reciente),
                        tint = Color.Black
                    )
                }
                ElevatedButton(
                    modifier = Modifier
                        .padding(78.dp, 0.dp, 0.dp, 0.dp),
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
    }
    PopUp(verPopUp, context, nombreObjeto, objetoEliminado)
}