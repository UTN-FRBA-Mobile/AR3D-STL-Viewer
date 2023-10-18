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

    val addFileLauncher = managedActivityResultLauncher(context, objetosVistosRecientemente, errorLanzarVistaPrevia)
    textoTopBar.value = "Bienvenido"

    LaunchedEffect(1, objetoEliminado.value) {
        objetosVistosRecientemente.value = buscarObjetosVistosRecientementeEnOrdenUltimaVisualizacion(context)
        objetoEliminado.value = false
    }

    Column(
        modifier = Modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        ) {
            BotonConDescripcion(
                { addFileLauncher.launch("*/*")},
                "Buscar archivo",
                "Busca en tu dispositivo un archivo con extensión .stl para visualizarlo en realidad aumentada."
            )
            BotonConDescripcion(
                {if (hayConexionAInternet(context)) {
                    navController.navigate("catalogo")
                } else {
                    navController.navigate("sinConexionInternet/reintentarIrACatalogo")
                }},
                "Catálogo",
                "Busca en nuestro catálogo de objetos 3D el que mas te guste y descarga su archivo .stl"
            )
            ListaObjetosRecientes(objetosVistosRecientemente.value, context, objetoEliminado)
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
    MensajeSinConexionAInternet()
}

@Composable
fun BotonConDescripcion(onClick: () -> Unit, textoBoton: String, descripcion: String) {
    Card(
        modifier = Modifier.padding(bottom = 8.dp)
    ) {
        Text(
            text = descripcion,
            Modifier.padding(16.dp)
        )
        Button(
            onClick = {onClick()},
            modifier = Modifier
                .padding(16.dp, 0.dp, 16.dp, 16.dp)
                .fillMaxWidth()
        ) {
            Text(text = textoBoton)
        }
    }
}

@Composable
private fun managedActivityResultLauncher(
    context: Context,
    objetosVistosRecientemente: MutableState<List<String>>,
    errorLanzarVistaPrevia: MutableState<Boolean>
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
                lanzarVistaPrevia(context, fileText, errorLanzarVistaPrevia)
            }

            corutinaLanzarVistaPrevia.invokeOnCompletion { causa ->
                if(causa == null) {
                    objetosVistosRecientemente.value = buscarObjetosVistosRecientementeEnOrdenUltimaVisualizacion(context)
                } else {
                    errorLanzarVistaPrevia.value = true
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