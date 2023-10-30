package com.example.practica.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.practica.R
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.buscarObjetosVistosRecientementeEnOrdenUltimaVisualizacion
import com.example.practica.utils.buscarNombreArchivo
import com.example.practica.utils.hayConexionAInternet

@Composable
fun Home(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = LocalContext.current
    val objetosVistosRecientemente = remember { mutableStateOf<List<String>>(emptyList()) }
    val objetoEliminado = remember { mutableStateOf(false) }
    val addFileLauncher = managedActivityResultLauncher(context, navController)
    textoTopBar.value = stringResource(id = R.string.bienvenido)


    LaunchedEffect(1, objetoEliminado.value) {
        objetosVistosRecientemente.value = buscarObjetosVistosRecientementeEnOrdenUltimaVisualizacion(context)
        objetoEliminado.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
    ) {
        Card {
            Text(
                text = stringResource(id = R.string.subir_archivo_stl),
                Modifier.padding(16.dp)
            )
        }
        ElevatedButton(
            onClick = { addFileLauncher.launch("*/*") },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.buscar_arhivo))
        }
        Button(
            onClick = {
                if (hayConexionAInternet(context)) {
                    navController.navigate("catalogo")
                } else {
                    navController.navigate("sinConexionInternet/reintentarIrACatalogo")
                }
            },
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            Text(text = stringResource(id = R.string.catalogo))
        }
        ListaObjetosRecientes(objetosVistosRecientemente.value, context, objetoEliminado)
    }

    MensajeToast(
        texto = stringResource(id = R.string.sin_conexion),
        Color.Red,
        esVisible = { !hayConexionAInternet(context = context) }
    )
}

@Composable
private fun managedActivityResultLauncher(context: Context, navController: NavHostController): ManagedActivityResultLauncher<String, Uri?> {
    val addFileLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        if (it != null) {
            val fileText = buscarNombreArchivo(context, it)
            navController.navigate("confirmarstl/${fileText}")
        }
    }
    return addFileLauncher
}

fun lanzarVistaPreviaObjetoReciente(context: Context, nombreObjeto3dObj: String) {
    var intentVisualizarEn3d = Intent(context, ArCoreActivity::class.java)
    intentVisualizarEn3d.putExtra("nombreArchivoObjeto3dObj", nombreObjeto3dObj);
    context.startActivity(intentVisualizarEn3d)
}