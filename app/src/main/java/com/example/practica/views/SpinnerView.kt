package com.example.practica.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Spinner(modifier: Modifier) {
    val modifierFinal = modifier.then(Modifier.fillMaxSize())
    Column(
        modifier = modifierFinal,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(48.dp)
        )
    }
}

@Preview
@Composable
fun SpinnerButton() {
    CircularProgressIndicator(
        modifier = Modifier
            .padding(top = 0.dp, bottom = 0.dp)
            .size(23.dp),
        color = Color.White)
}
