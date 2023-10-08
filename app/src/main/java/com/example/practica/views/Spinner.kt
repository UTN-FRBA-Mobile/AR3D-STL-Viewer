package com.example.practica.views

import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Spinner() {
    CircularProgressIndicator(
        modifier = Modifier.width(48.dp)
    )
}
