package com.example.practica.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme

@Composable
fun Home(context: Context, catalogoEsVisible: MutableState<Boolean>) {
    val objetosVistosRecientemente = remember { mutableStateOf<List<String>>(emptyList()) }

    LaunchedEffect(1) {
        objetosVistosRecientemente.value = buscarObjetosVistosRecientemente(context)
    }

    val addFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            println("Selected file URI: $it")
        }
    }
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Top
    ) {
        Button(
            onClick = {
                addFileLauncher.launch("*/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
        ) {
            Text(text= "Seleccionar nuevo")
        }
        Button(
            onClick = {catalogoEsVisible.value = true},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp)
        ) {
            Text(text = "Buscar en catálogo")
        }
        ListaObjetosRecientes(objetosVistosRecientemente.value)
    }
}

@Composable
fun ListaObjetosRecientes(objetosVistosRecientemente: List<String>) {
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
                    ObjetoReciente(it[index])
                }
            }
        }
    }
}

@Composable
fun ObjetoReciente(nombreObjeto: String) {
    Card(
        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        Text(
            modifier = Modifier.padding(8.dp),
            text = nombreObjeto
        )
    }
}

fun buscarObjetosVistosRecientemente(context: Context): List<String> {
    val nombresDeArchivos = mutableListOf<String>()

    val directorio = context.getExternalFilesDir(null)

    if (directorio != null && directorio.exists() && directorio.isDirectory) {
        val archivos = directorio.listFiles()
        if (archivos != null) {
            for (archivo in archivos) {
                if (archivo.isFile) {
                    nombresDeArchivos.add(archivo.name)
                }
            }
        }
    }
    return nombresDeArchivos
}