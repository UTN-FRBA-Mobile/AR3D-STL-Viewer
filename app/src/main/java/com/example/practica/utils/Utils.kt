package com.example.practica.utils

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.Base64
import androidx.compose.runtime.MutableState
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.existeElArchivo
import com.example.practica.repository.guardarArchivoEnAlmacenamientoExterno
import com.example.practica.services.catalogoApiService
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