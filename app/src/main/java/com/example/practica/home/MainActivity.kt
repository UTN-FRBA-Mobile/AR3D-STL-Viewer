package com.example.practica.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.practica.main.AppScaffold
import com.example.practica.services.Capitulo
import com.example.practica.services.CapitulosPorPagina
import com.example.practica.services.apiService
import retrofit2.HttpException
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.example.practica.arcore.ArCoreActivity


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
    val capitulo = remember {mutableStateOf(1)}
    AppScaffold() {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 75.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        val intent = Intent(context, ArCoreActivity::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text(text= "Realidad aumentada")
                }
                Button(
                    onClick = {capitulo.value = capitulo.value + 1},
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp)

                ) {
                    Text(text = "Siguiente")
                }
                MiComposable(capitulo.value)
            }
        }

    }
}
@Composable
fun MiComposable(page: Int) {
    val data = remember { mutableStateOf<CapitulosPorPagina?>(null) }

    LaunchedEffect(page) {
        try {
            data.value = apiService().getCapitulos(page)
        } catch (e: HttpException) {
            data.value = CapitulosPorPagina()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize()
    ) {
        data.value?.results?.let {
            items(it.size) { index ->
                OutlinedCardExample(it[index])
            }
        }
    }
}

@Composable
fun OutlinedCardExample(capitulo: Capitulo) {
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        modifier = Modifier
            .size(width = 450.dp, height = 140.dp)
            .padding(10.dp)
    ) {
        listOf(capitulo.name, capitulo.episode).map { unTexto ->
            Text(
                text = unTexto,
                modifier = Modifier
                    .padding(16.dp),
                textAlign = TextAlign.Center,
            )
        }
    }
}