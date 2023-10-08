package com.example.practica.views

import android.content.Context
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Home(context: Context, catalogoEsVisible: MutableState<Boolean>) {
    Button(
        onClick = {},
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Text(text= "Seleccionar nuevo")
    }
    Button(
        onClick = {catalogoEsVisible.value = true},
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp)
    ) {
        Text(text = "Buscar en catálogo")
    }
}