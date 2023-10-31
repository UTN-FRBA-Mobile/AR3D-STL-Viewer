package com.example.practica.views

import android.content.ContentValues
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.SubcomposeAsyncImage
import com.example.practica.R
import com.example.practica.services.Objeto3d
import com.example.practica.utils.hayConexionAInternet
import com.example.practica.utils.lanzarVistaPrevia
import com.example.practica.viewmodel.BusquedaArchivoStlViewModel
import com.example.practica.viewmodel.CatalogoViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.OutputStream

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun Catalogo(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = LocalContext.current
    textoTopBar.value = stringResource(id = R.string.catalogo)

    val catalogoViewModel: CatalogoViewModel = viewModel()
    val catalogo by catalogoViewModel.catalogo.observeAsState(emptyList())
    val error by catalogoViewModel.error.observeAsState(false)
    val isLoadingCatalogo by catalogoViewModel.isLoading.observeAsState(true)
    val estadoAlGuardarArchivo = remember { mutableStateOf(EstadoAlGuardarArchivo()) }

    val verSpinnerCargandoCatalogo = remember { mutableStateOf(isLoadingCatalogo) }
    val verPopUpError = remember { mutableStateOf(error) }

    val busquedaArchivoStlViewModel: BusquedaArchivoStlViewModel = viewModel()
    val errorBusquedaArchivoStl by busquedaArchivoStlViewModel.error.observeAsState(false)
    val archivoStl by busquedaArchivoStlViewModel.archivoStl.observeAsState(null)

    LaunchedEffect(catalogo) {
        catalogoViewModel.getCatalogo()
        verSpinnerCargandoCatalogo.value = isLoadingCatalogo
    }

    LaunchedEffect(error) {
        verPopUpError.value = error
        verSpinnerCargandoCatalogo.value = isLoadingCatalogo
    }

    LaunchedEffect(archivoStl) {
        if(archivoStl != null) {
            guardarArchivoStl(archivoStl!!.nombre, archivoStl!!.contenido, estadoAlGuardarArchivo, context)
        }
    }

    if(verSpinnerCargandoCatalogo.value) {
        Spinner()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            catalogo.let {
                items(it.size) { index ->
                    CardObjeto3d(context, it[index], esUltimoElemento(index, catalogo), busquedaArchivoStlViewModel)
                }
            }
        }
        ToastConfirmacionDescargaArchivo(estadoAlGuardarArchivo, errorBusquedaArchivoStl)
    }

    PopUp(
        verPopUp = verPopUpError,
        onConfirmation = {
            verPopUpError.value = false
            navController.popBackStack()
        },
        "Ok",
        dialogText = "Error al obtener el catálogo, volvé a intentar",
        dialogTitle = "Error"
    )
}

@Composable
fun CardObjeto3d(
    context: Context,
    objeto3d: Objeto3d,
    ultimoElemento: Boolean = false,
    busquedaArchivoStlViewModel: BusquedaArchivoStlViewModel
) {
    val cargandoVistaPrevia = remember { mutableStateOf(false) }
    val objeto3dObj = remember { mutableStateOf("") }
    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }
    val verPopUpPreguntaGuardarArchivoStl = remember { mutableStateOf(false) }

    LaunchedEffect(objeto3dObj.value) {
        lanzarVistaPrevia(context, objeto3dObj.value, errorLanzarVistaPrevia)
        objeto3dObj.value = ""
        cargandoVistaPrevia.value = false
    }

    Card(
        colors = CardDefaults
            .cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        modifier = Modifier
            .then(modifierUltimoElementoCatalogo(ultimoElemento))
            .fillMaxWidth()
            .padding(16.dp, 16.dp, 16.dp, 0.dp)
    ) {
        Box() {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterStart)
            ) {
                SubcomposeAsyncImage(
                    modifier = Modifier
                        .size(256.dp)
                        .align(Alignment.CenterHorizontally),
                    model = objeto3d.imgUrl,
                    loading = {
                        Spinner()
                    },
                    contentDescription = null,
                )
                Text(
                    text = objeto3d.name,
                    fontSize = 22.sp,
                    modifier = Modifier
                        .padding(16.dp, 0.dp, 0.dp, 16.dp),
                    textAlign = TextAlign.Center
                )
                Divider(
                    modifier = Modifier
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                )
                Row(
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = {
                            cargandoVistaPrevia.value = true
                            objeto3dObj.value = objeto3d.name
                        },
                        modifier = Modifier
                            .padding(16.dp, 0.dp, 8.dp, 16.dp)
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        if(cargandoVistaPrevia.value) SpinnerButton()
                        else Text(text = stringResource(id = R.string.previsualizar))
                    }
                    ElevatedButton(
                        onClick = { verPopUpPreguntaGuardarArchivoStl.value = true },
                        modifier = Modifier
                            .padding(8.dp, 0.dp, 16.dp, 16.dp)
                            .weight(1f)
                            .align(Alignment.CenterVertically)
                    ) {
                        Text(text = stringResource(id = R.string.descargar_stl))
                    }
                }
            }
        }

        PopUp(
            verPopUp = verPopUpPreguntaGuardarArchivoStl,
            onConfirmation = {
                verPopUpPreguntaGuardarArchivoStl.value = false
                buscarArchivoStl(objeto3d.name, busquedaArchivoStlViewModel)
            },
            onDismissRequest = {
                verPopUpPreguntaGuardarArchivoStl.value = false
            },
            textoConfirmation = stringResource(id = R.string.si),
            dialogTitle = stringResource(id = R.string.descarga_stl),
            dialogText = stringResource(id = R.string.querer_guardar_stl) + "${objeto3d.name}.stl?",
            textoDismiss = stringResource(id = R.string.no),
        )
        PopUp(
            verPopUp = errorLanzarVistaPrevia,
            onConfirmation = {
                errorLanzarVistaPrevia.value = false
            },
            "Ok",
            dialogText = stringResource(id = R.string.error_al_previsualizar),
            dialogTitle = if(hayConexionAInternet(context)) "Error" else stringResource(id = R.string.sin_conexion)
        )
    }
}

@Composable
fun ToastConfirmacionDescargaArchivo(
    estadoAlGuardarArchivo: MutableState<EstadoAlGuardarArchivo>,
    errorBusquedaArchivoStl: Boolean
) {
    if(errorBusquedaArchivoStl) {
        MensajeToastErrorAlGuardarArchivo(estadoAlGuardarArchivo)
    }
    when(estadoAlGuardarArchivo.value.estado) {
        "GUARDADO" -> MensajeToast(
            texto = stringResource(id = R.string.archivo)+" ${estadoAlGuardarArchivo.value.nombreArchivo}.stl " + stringResource(id = R.string.guardado),
            MaterialTheme.colorScheme.secondary,
            onDismiss = {estadoAlGuardarArchivo.value = EstadoAlGuardarArchivo()}
        )
        "ERROR" -> MensajeToastErrorAlGuardarArchivo(estadoAlGuardarArchivo)
        "" -> {}
    }
}

@Composable
fun MensajeToastErrorAlGuardarArchivo(estadoAlGuardarArchivo: MutableState<EstadoAlGuardarArchivo>) {
    MensajeToast(
        texto = stringResource(id = R.string.error_descargando_archivo) + "${estadoAlGuardarArchivo.value.nombreArchivo}.stl!",
        Color.Red,
        onDismiss = {estadoAlGuardarArchivo.value = EstadoAlGuardarArchivo()}
    )
}

fun buscarArchivoStl(name: String, busquedaArchivoStlViewModel: BusquedaArchivoStlViewModel) {
    CoroutineScope(Dispatchers.Default).launch {
        busquedaArchivoStlViewModel.getArchivoStl(name)
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
fun guardarArchivoStl(name: String, contenidoArchivoStl: String, estadoAlGuardarArchivo: MutableState<EstadoAlGuardarArchivo>, context: Context) {
    try {
        val nombreArchivo = name + ".stl"
        val contentResolver = context.contentResolver
        val contentValues = ContentValues()
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
        contentValues.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)

        val externalContentUri = MediaStore.Downloads.EXTERNAL_CONTENT_URI
        val uri: Uri? = contentResolver.insert(externalContentUri, contentValues)

        uri?.let {
            val outputStream: OutputStream? = contentResolver.openOutputStream(uri)
            outputStream?.use { stream ->
                stream.write(contenidoArchivoStl.toByteArray())
                stream.flush()
            }
            estadoAlGuardarArchivo.value = EstadoAlGuardarArchivo("GUARDADO", name)
        } ?: run {
            estadoAlGuardarArchivo.value = EstadoAlGuardarArchivo("ERROR", name)
        }
    } catch (ex: Exception) {
        estadoAlGuardarArchivo.value = EstadoAlGuardarArchivo("ERROR", name)
    }
}

fun esUltimoElemento(index: Int, catalogo: List<Objeto3d>): Boolean {
    return index == catalogo.size - 1
}

fun modifierUltimoElementoCatalogo(ultimoElemento: Boolean): Modifier {
    return if (ultimoElemento) {
        Modifier.padding(bottom = 16.dp)
    } else {
        Modifier
    }
}

data class EstadoAlGuardarArchivo(val estado: String = "", val nombreArchivo: String = "")
