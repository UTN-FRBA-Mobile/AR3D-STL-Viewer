package com.example.practica.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.MutableState
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.existeElArchivo
import com.example.practica.repository.guardarArchivoEnAlmacenamientoExterno
import com.example.practica.services.catalogoApiService
import java.io.File
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

@SuppressLint("Range")
fun buscarNombreArchivo(context: Context, it: Uri): String {
    val contentResolver = context.contentResolver
    val cursor = contentResolver.query(it, null, null, null, null)
    if (cursor != null && cursor.moveToFirst()) {
        val displayName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor.close()
        return displayName
    }
    return ""
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
