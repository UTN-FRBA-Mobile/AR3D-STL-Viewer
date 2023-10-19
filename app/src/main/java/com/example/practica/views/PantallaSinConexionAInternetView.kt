package com.example.practica.views

import android.content.Context
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.practica.R
import com.example.practica.utils.hayConexionAInternet

@Composable
fun PantallaSinConexionAInternet(
    navController: NavHostController,
    textoTopBar: MutableState<String>
) {
    textoTopBar.value = "Sin internet"
    val context = LocalContext.current
    val argumentos = navController.currentBackStackEntryAsState().value?.arguments
    val accionPorArgumento = argumentos?.getString("accionReintento")

    Box() {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    Icons.Rounded.Warning,
                    contentDescription = stringResource(id = R.string.waring_sin_internet),
                    tint = Color.Gray,
                    modifier = Modifier.size(200.dp)
                )
                Text(
                    text = "Parece que no tenes acceso a internet"
                )
            }
            Button(
                onClick = { reintentar(context, navController, accionPorArgumento) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, bottom = 16.dp, end = 16.dp)
            ) {
                Text(text = "Reintentar")
            }
        }
    }
}

fun reintentar(context: Context, navController: NavController, accionPorArgumento: String?) {
    if (hayConexionAInternet(context)) {
        navController.popBackStack()
        if(accionPorArgumento == "reintentarIrACatalogo") {
            navController.navigate("catalogo")
        }
    }
}