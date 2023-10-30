package com.example.practica.views

import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
    val context = LocalContext.current
    textoTopBar.value = "Confirmación archivo STL"

    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }
    val argumentos = navController.currentBackStackEntryAsState().value?.arguments
    val fileName = remember { mutableStateOf("") }

    val addFileLauncher = managedActivityResultLauncher(context, fileName)

    LaunchedEffect(argumentos?.getString("fileName")) {
        fileName.value = argumentos?.getString("fileName").toString()
    }

    val cargandoStl = remember { mutableStateOf(false) }

    if(cargandoStl.value){
        Spinner()
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Card(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Seleccionaste el archivo ${fileName.value}.stl",
                        Modifier.padding(16.dp)
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
                        cargandoStl.value = true
                        val corutinaLanzarVistaPrevia = CoroutineScope(Dispatchers.Default).launch {
                            lanzarVistaPrevia(context, fileName.value, errorLanzarVistaPrevia)
                        }
                        corutinaLanzarVistaPrevia.invokeOnCompletion { causa ->
                            if(causa == null) {
                                cargandoStl.value = false
                            } else {
                                errorLanzarVistaPrevia.value = true
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Text(text = "Confirmar")
                }
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

@Composable
private fun managedActivityResultLauncher(context: Context, fileName: MutableState<String>): ManagedActivityResultLauncher<String, Uri?> {
    val addFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            fileName.value = buscarNombreArchivo(context, it)
        }
    }
    return addFileLauncher
}