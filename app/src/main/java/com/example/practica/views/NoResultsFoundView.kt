package com.example.practica.views

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun NoResultsFound(verTextNotFound: MutableState<Boolean>){
    if(verTextNotFound.value) {
        Text(
            text = "No results were found",
            modifier = Modifier.padding(16.dp, 100.dp,0.dp,0.dp)
        )
    }
}