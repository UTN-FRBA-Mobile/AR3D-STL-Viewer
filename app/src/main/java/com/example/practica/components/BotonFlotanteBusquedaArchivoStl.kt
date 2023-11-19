package com.example.practica.components

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.practica.R
import com.google.android.material.color.utilities.MaterialDynamicColors.primary

@Composable
fun BotonFlotanteBusquedaArchivoStl(
    addFileLauncher: ManagedActivityResultLauncher<String, Uri?>
) {
    ExtendedFloatingActionButton(
        modifier = Modifier
            .padding(bottom = 16.dp),
        onClick = { addFileLauncher.launch("*/*") }
    ) {
        Icon(
            Icons.Default.Search,
            contentDescription = stringResource(id = R.string.busqueda_catalogo)
        )
        Text(text = "Buscar archivo .stl")
    }
}