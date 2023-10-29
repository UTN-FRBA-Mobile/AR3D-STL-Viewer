package com.example.practica.views

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.buscarObjetosVistosRecientementeEnOrdenUltimaVisualizacion
import com.example.practica.utils.hayConexionAInternet
import com.example.practica.utils.lanzarVistaPrevia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader

@Composable
fun Home(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = LocalContext.current
    val objetosVistosRecientemente = remember { mutableStateOf<List<String>>(emptyList()) }
    val objetoEliminado = remember { mutableStateOf(false) }
    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }

    val addFileLauncher =
        managedActivityResultLauncher(context, objetosVistosRecientemente, errorLanzarVistaPrevia, navController)
    textoTopBar.value = "Bienvenido"

    LaunchedEffect(1, objetoEliminado.value) {
        objetosVistosRecientemente.value =
            buscarObjetosVistosRecientementeEnOrdenUltimaVisualizacion(context)
        objetoEliminado.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
    ) {
        Card {
            Text(
                text = "Subí tu archivo .STL o buscá en nuestro catálogo para verlo en realidad aumentada",
                Modifier.padding(16.dp)
            )
        }
        Button(
            onClick = { addFileLauncher.launch("*/*") },
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Buscar archivo")
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
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Catálogo")
        }
        ListaObjetosRecientes(objetosVistosRecientemente.value, context, objetoEliminado)
    }
    MensajeSinConexionAInternet()
}


@Composable
private fun managedActivityResultLauncher(
    context: Context,
    objetosVistosRecientemente: MutableState<List<String>>,
    errorLanzarVistaPrevia: MutableState<Boolean>,
    navController: NavHostController
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