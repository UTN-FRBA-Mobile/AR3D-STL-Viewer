package com.example.practica.home

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practica.main.AppScaffold
import com.example.practica.views.Catalogo
import com.example.practica.views.Home

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun App() {
    val context = LocalContext.current
    val catalogoEsVisible = remember {mutableStateOf(false)}
    val textoTopBar = remember { mutableStateOf("Bienvenido") }
    AppScaffold(textoTopBar = textoTopBar.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 64.dp)
        ) {
            Column() {
                if(catalogoEsVisible.value) {
                    Catalogo(context, textoTopBar)
                } else {
                    Home(context, catalogoEsVisible)
                }
            }
        }
    }
}