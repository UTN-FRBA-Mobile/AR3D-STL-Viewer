package com.example.practica.views

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.practica.R
import com.example.practica.components.BotonFlotanteBusquedaArchivoStl
import com.example.practica.components.PopUp
import com.example.practica.components.SpinnerButton
import com.example.practica.utils.buscarNombreArchivo
import com.example.practica.utils.lanzarVistaPrevia
import com.example.practica.viewmodel.ConfirmacionStlViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ConfirmarStl(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = navController.context
    textoTopBar.value = stringResource(id = R.string.confirmar_archivo)

    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }
    val argumentos = navController.currentBackStackEntryAsState().value?.arguments
    val argumentoFileName = argumentos?.getString("fileName").toString()

    val confirmacionStlViewModel: ConfirmacionStlViewModel = viewModel()
    val nombreArchivo = confirmacionStlViewModel.nombreArchivoStl.observeAsState("")
    val addFileLauncher = managedActivityResultLauncher(context, confirmacionStlViewModel)

    LaunchedEffect(argumentoFileName) {
        confirmacionStlViewModel.setNombreArchivoStl(argumentoFileName)
    }

    val cargandoStl = remember { mutableStateOf(false) }

    Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row {
                    Text(
                        text = stringResource(id = R.string.archivo_seleccionado) + " ${nombreArchivo.value}",
                        Modifier.padding(start = 16.dp, top = 16.dp, end = 0.dp, bottom = 16.dp)
                    )
                }
            }
            Button(
                onClick = {
                    confirmarYVisualizarObjeto(
                        cargandoStl,
                        context,
                        nombreArchivo.value,
                        errorLanzarVistaPrevia
                    )
                },
                modifier = Modifier
                    .padding(bottom = 16.dp, top = 12.dp)
                    .fillMaxWidth()
            ) {
                if (cargandoStl.value) SpinnerButton()
                else {
                    Icon(
                        Icons.Default.Done,
                        contentDescription = stringResource(id = R.string.busqueda_catalogo)
                    )
                    Text(text = stringResource(id = R.string.confirmar))
                }
            }
        }
        Box(
            modifier = Modifier.align(Alignment.End)
        ) {
            BotonFlotanteBusquedaArchivoStl(addFileLauncher)
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
    fileName: String,
    errorLanzarVistaPrevia: MutableState<Boolean>
) {
    cargandoStl.value = true

    val regex = Regex(" \\(\\d+\\)\\.stl|\\.stl| \\(\\d+\\)")
    val nombreObjeto = fileName.replace(regex, "")

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
private fun managedActivityResultLauncher(context: Context, confirmacionStlViewModel: ConfirmacionStlViewModel): ManagedActivityResultLauncher<String, Uri?> {
    val addFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            confirmacionStlViewModel.setNombreArchivoStl(buscarNombreArchivo(context, it))
        }
    }
    return addFileLauncher
}