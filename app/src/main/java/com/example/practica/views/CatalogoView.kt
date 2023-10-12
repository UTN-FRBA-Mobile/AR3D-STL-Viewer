package com.example.practica.views

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.ui.platform.LocalContext
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.practica.arcore.ArCoreActivity
import com.example.practica.repository.existeElArchivo
import com.example.practica.repository.guardarArchivoEnAlmacenamientoExterno
import com.example.practica.services.Objeto3d
import com.example.practica.services.catalogoApiService


@Composable
fun Catalogo(textoTopBar: MutableState<String>) {
    val context = LocalContext.current
    val objetos3d = remember { mutableStateOf<List<Objeto3d>?>(null) }
    val cargandoCatalogo = remember { mutableStateOf(true) }
    textoTopBar.value = "Catálogo"

    LaunchedEffect(1) {
        objetos3d.value = catalogoApiService().getObjetos3d()
        cargandoCatalogo.value = false
    }

    if(cargandoCatalogo.value) {
        Spinner()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            objetos3d.value?.let {
                items(it.size) { index ->
                    CardObjeto3d(context,it[index])
                }
            }
        }
    }
}

@Composable
fun CardObjeto3d(context: Context, objeto3d: Objeto3d) {
    val objeto3dObj = remember { mutableStateOf<String>("") }

    LaunchedEffect(objeto3dObj.value) {
        lanzarVistaPrevia(context, objeto3dObj.value)
    }

    Card(
        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        Text(
            text = objeto3d.name,
            fontSize = 20.sp,
            modifier = Modifier
                .padding(16.dp),
            textAlign = TextAlign.Center
        )
        Box() {
            Column(
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Image(
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    bitmap = convertirBase64ABitMap(objeto3d.img),
                    contentDescription = "contentDescription"
                )
                Button(
                    onClick = { objeto3dObj.value = objeto3d.name },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Previsualizar")
                }
            }
        }
    }
}

suspend fun lanzarVistaPrevia(
    context: Context,
    nombreObjeto3dObj: String
) {
    if(nombreObjeto3dObj != "") {
        var nombreArchivoObjeto3dObj = "${nombreObjeto3dObj}.obj"

        if(!existeElArchivo(context, nombreArchivoObjeto3dObj)) {
            var objResponse: String = catalogoApiService().getObjetoPorNombre(nombreObjeto3dObj)
            guardarArchivoEnAlmacenamientoExterno(context, nombreArchivoObjeto3dObj, objResponse)
        }

        var intentVisualizarEn3d = Intent(context, ArCoreActivity::class.java)
        intentVisualizarEn3d.putExtra("nombreArchivoObjeto3dObj", nombreArchivoObjeto3dObj);
        context.startActivity(intentVisualizarEn3d)
    }
}

fun convertirBase64ABitMap(base64: String): ImageBitmap {
    val decodedString: ByteArray = Base64.decode(base64, Base64.DEFAULT)
    return BitmapFactory
        .decodeByteArray(decodedString, 0, decodedString.size)
        .asImageBitmap()
}