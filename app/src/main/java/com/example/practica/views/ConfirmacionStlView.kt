package com.example.practica.views

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.practica.utils.buscarNombreArchivo
import com.example.practica.utils.lanzarVistaPrevia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ConfirmarStl(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = navController.context
    textoTopBar.value = "Confirmación archivo STL"

    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }
    val argumentos = navController.currentBackStackEntryAsState().value?.arguments
    val argumentoFileName = argumentos?.getString("fileName").toString()

    val fileName = remember { mutableStateOf("") }
    val addFileLauncher = managedActivityResultLauncher(context, fileName)

    LaunchedEffect(argumentoFileName) {
        fileName.value = argumentoFileName
    }

    val cargandoStl = remember { mutableStateOf(false) }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Row {
                Text(
                    text = "Seleccionaste el archivo",
                    Modifier.padding(start = 16.dp, top= 16.dp, end = 0.dp, bottom = 16.dp)
                )
                Text(
                    text = "${fileName.value}",
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 4.dp, 16.dp)
                )
            }
        }
        Column {
            ElevatedButton(
                onClick = { addFileLauncher.launch("*/*") },
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Buscar otro archivo")
            }
            Button(
                onClick = {
                    confirmarYVisualizarObjeto(cargandoStl, context, fileName, errorLanzarVistaPrevia)
                },
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .fillMaxWidth()
            ) {
                if (cargandoStl.value) SpinnerButton()
                else Text(text = "Confirmar")
            }
        }
    }

    PopUp(
        verPopUp = errorLanzarVistaPrevia,
        onConfirmation = {
            errorLanzarVistaPrevia.value = false
        },
        "Ok",
        dialogText = "Error al previsualizar el objeto, volvé a intentar",
        dialogTitle = "Error"
    )
}

private fun confirmarYVisualizarObjeto(
    cargandoStl: MutableState<Boolean>,
    context: Context,
    fileName: MutableState<String>,
    errorLanzarVistaPrevia: MutableState<Boolean>
) {
    cargandoStl.value = true

    val regex = Regex(" \\(\\d+\\)\\.stl|\\.stl| \\(\\d+\\)")
    val nombreObjeto = fileName.value.replace(regex, "")

    val corutinaLanzarVistaPrevia = CoroutineScope(Dispatchers.Default).launch {
        lanzarVistaPrevia(context, nombreObjeto, errorLanzarVistaPrevia)
    }
    corutinaLanzarVistaPrevia.invokeOnCompletion { causa ->
        if (causa == null) {
            cargandoStl.value = false
        } else {
            errorLanzarVistaPrevia.value = true
        }
    }
}

@SuppressLint("Range")
@Composable
private fun managedActivityResultLauncher(context: Context, fileName: MutableState<String>): ManagedActivityResultLauncher<String, Uri?> {
    val addFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            fileName.value = buscarNombreArchivo(context, it)
        }
    }
    return addFileLauncher
}