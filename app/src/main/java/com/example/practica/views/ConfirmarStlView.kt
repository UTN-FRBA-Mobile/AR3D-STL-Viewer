package com.example.practica.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.practica.utils.lanzarVistaPrevia
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun ConfirmarStl(navController: NavHostController, textoTopBar: MutableState<String>) {
    val context = LocalContext.current
    textoTopBar.value = "Confirmacion"

    val errorLanzarVistaPrevia = remember { mutableStateOf(false) }
    val argumentos = navController.currentBackStackEntryAsState().value?.arguments
    val fileName = argumentos?.getString("fileName") ?: ""

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(start = 16.dp, top = 16.dp, end = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Seleccionaste el archivo:",
                    Modifier.padding(16.dp)
                )
                Text(text = "${fileName}.stl",
                    Modifier.padding(16.dp)
                    )
            }
        }
        Button(
            onClick = {
                val corutinaLanzarVistaPrevia = CoroutineScope(Dispatchers.Default).launch {
                    lanzarVistaPrevia(context, fileName, errorLanzarVistaPrevia)
                }
                corutinaLanzarVistaPrevia.invokeOnCompletion { causa ->
                    if(causa == null) {

                    } else {
                        errorLanzarVistaPrevia.value = true
                    }
                }
            },
            modifier = Modifier
                .padding(top = 12.dp)
                .fillMaxWidth()
        ) {
            Text(text = "Confirmar")
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