package com.example.practica.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.util.Base64
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.navigation.NavHostController
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.existeElArchivo
import com.example.practica.repository.guardarArchivoEnAlmacenamientoExterno
import com.example.practica.services.catalogoApiService
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.lang.Exception

suspend fun lanzarVistaPrevia(
    context: Context,
    nombreObjeto3dObj: String,
    errorLanzarVistaPrevia: MutableState<Boolean>
) {
    if(nombreObjeto3dObj != "") {
        try {
            var nombreArchivoObjeto3dObj = "${nombreObjeto3dObj}.obj"

            if(!existeElArchivo(context, nombreArchivoObjeto3dObj)) {
                var objResponse: String = catalogoApiService().getObjetoPorNombre(nombreObjeto3dObj)
                guardarArchivoEnAlmacenamientoExterno(
                    context,
                    nombreArchivoObjeto3dObj,
                    objResponse
                )
            }
            var intentVisualizarEn3d = Intent(context, ArCoreActivity::class.java)
            intentVisualizarEn3d.putExtra("nombreArchivoObjeto3dObj", nombreArchivoObjeto3dObj);
            context.startActivity(intentVisualizarEn3d)
        } catch (e: Exception) {
            errorLanzarVistaPrevia.value = true
        }
    }
}

// El contenido del archivo contiene el nombre del mismo
fun buscarNombreArchivo(context: Context, it: Uri?): String {
    val inputStream = it?.let { it1 -> context.contentResolver.openInputStream(it1) }

    val stringBuilder = StringBuilder()
    inputStream?.use { stream ->
        val reader = BufferedReader(InputStreamReader(stream))
        var line: String?
        while (reader.readLine().also { line = it } != null) {
            stringBuilder.append(line)
        }
    }

    val fileName = stringBuilder.toString()
    return fileName
}

fun convertirBase64ABitMap(base64: String): ImageBitmap {
    val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory
        .decodeByteArray(decodedString, 0, decodedString.size)
        .asImageBitmap()
}

fun hayConexionAInternet(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val network = connectivityManager.activeNetwork
    val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

    return network != null && networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)!!
}
fun ordenarArchivos(files: Array<File>) {
    files.sortWith { unArchivo, otroArchivo ->
        val diferenciaEntreCreacionArchivos = unArchivo.lastModified() - otroArchivo.lastModified()
        when {
            diferenciaEntreCreacionArchivos < 0 -> 1
            diferenciaEntreCreacionArchivos > 0 -> -1
            else -> 0
        }
    }
}
