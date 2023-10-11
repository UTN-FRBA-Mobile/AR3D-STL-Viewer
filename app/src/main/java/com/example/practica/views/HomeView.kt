package com.example.practica.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.buscarObjetosVistosRecientemente
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun Home(context: Context, catalogoEsVisible: MutableState<Boolean>) {
    val objetosVistosRecientemente = remember { mutableStateOf<List<String>>(emptyList()) }
    val objetoEliminado = remember { mutableStateOf<Boolean>(false) }
    val addFileLauncher = managedActivityResultLauncher(context, objetosVistosRecientemente)

    LaunchedEffect(1, objetoEliminado.value) {
        objetosVistosRecientemente.value = buscarObjetosVistosRecientemente(context)
        objetoEliminado.value = false
    }

    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Top
    ) {
        FilledTonalButton(
            onClick = {
                addFileLauncher.launch("*/*")
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 16.dp, 16.dp, 0.dp)
        ) {
            Text(text= "Seleccionar nuevo")
        }
        FilledTonalButton(
            onClick = {catalogoEsVisible.value = true},
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp, 8.dp, 16.dp, 0.dp)
        ) {
            Text(text = "Buscar en catálogo")
        }
        ListaObjetosRecientes(objetosVistosRecientemente.value, context, objetoEliminado)
    }
}

@Composable
private fun managedActivityResultLauncher(
    context: Context,
    objetosVistosRecientemente: MutableState<List<String>>
): ManagedActivityResultLauncher<String, Uri?> {
    val addFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {

            val inputStream = context.contentResolver.openInputStream(it)

            val stringBuilder = StringBuilder()
            inputStream?.use { stream ->
                val reader = BufferedReader(InputStreamReader(stream))
                var line: String?
                while (reader.readLine().also { line = it } != null) {
                    stringBuilder.append(line)
                }
            }

            val fileText = stringBuilder.toString()

            val corutinaLanzarVistaPrevia = CoroutineScope(Dispatchers.Default).launch {
                lanzarVistaPrevia(context, fileText)
            }

            corutinaLanzarVistaPrevia.invokeOnCompletion { causa ->
                if(causa == null) {
                    objetosVistosRecientemente.value = buscarObjetosVistosRecientemente(context)
                } else {
                    // hacer algo por si la corutina falla
                }
            }

        }
    }
    return addFileLauncher
}

fun lanzarVistaPreviaObjetoReciente(context: Context, nombreObjeto3dObj: String) {
    var intentVisualizarEn3d = Intent(context, ArCoreActivity::class.java)
    intentVisualizarEn3d.putExtra("nombreArchivoObjeto3dObj", nombreObjeto3dObj);
    context.startActivity(intentVisualizarEn3d)
}