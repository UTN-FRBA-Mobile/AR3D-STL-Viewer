package com.example.practica.views

import android.content.Context
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.practica.services.Objeto3d
import com.example.practica.utils.convertirBase64ABitMap
import com.example.practica.utils.hayConexionAInternet
import com.example.practica.utils.lanzarVistaPrevia
import com.example.practica.viewmodel.CatalogoViewModel

@Composable
fun Catalogo(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = LocalContext.current
    textoTopBar.value = "Catálogo"

    val catalogoViewModel: CatalogoViewModel = viewModel()
    val catalogo by catalogoViewModel.catalogo.observeAsState(emptyList())
    val error by catalogoViewModel.error.observeAsState(false)
    val isLoadingCatalogo by catalogoViewModel.isLoading.observeAsState(true)

    val verSpinnerCargandoCatalogo = remember { mutableStateOf(isLoadingCatalogo) }
    val verPopUpError = remember { mutableStateOf(error) }

    LaunchedEffect(catalogo) {
        catalogoViewModel.getCatalogo()
        verSpinnerCargandoCatalogo.value = isLoadingCatalogo
    }

    LaunchedEffect(error) {
        verPopUpError.value = error
        verSpinnerCargandoCatalogo.value = isLoadingCatalogo
    }

    if(verSpinnerCargandoCatalogo.value) {
        Spinner()
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            catalogo.let {
                items(it.size) { index ->
                    CardObjeto3d(navController, context, it[index])
                }
            }
        }
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
fun CardObjeto3d(navController: NavHostController, context: Context, objeto3d: Objeto3d, nombreObjetoArg: String = "") {
    val objeto3dObj = remember { mutableStateOf(nombreObjetoArg) }
    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }

    LaunchedEffect(objeto3dObj.value) {
        lanzarVistaPrevia(context, objeto3dObj.value, errorLanzarVistaPrevia)
        objeto3dObj.value = ""
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
                    onClick = {
                        objeto3dObj.value = objeto3d.name
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp, 0.dp, 16.dp, 16.dp)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(text = "Previsualizar")
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
            dialogTitle = if(hayConexionAInternet(context)) "Error" else "Sin internet!"
        )
    }
}