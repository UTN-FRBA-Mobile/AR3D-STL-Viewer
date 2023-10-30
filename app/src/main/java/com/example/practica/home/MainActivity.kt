package com.example.practica.home

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.practica.main.AppScaffold
import com.example.practica.views.Catalogo
import com.example.practica.views.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.example.practica.views.ConfirmarStl
import com.example.practica.views.PantallaSinConexionAInternet
import com.example.practica.R

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun App(navController: NavHostController = rememberNavController()) {
    val greeting = stringResource(id = R.string.bienvenido)
    val textoTopBar = remember { mutableStateOf(greeting) }
    val rutaHome = "home"

    AppScaffold(textoTopBar = textoTopBar.value, navController) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
        ) {
            NavHost(navController = navController, startDestination = rutaHome) {
                composable(rutaHome) {
                    Home(navController, textoTopBar)
                }
                composable(route = "catalogo") {
                    Catalogo(navController, textoTopBar)
                }
                composable(route = "confirmarstl/{fileName}"){
                    ConfirmarStl(navController, textoTopBar)
                }
                composable(route = "sinConexionInternet/{accionReintento}") {
                    PantallaSinConexionAInternet(navController, textoTopBar)
                }
            }
        }
    }
}