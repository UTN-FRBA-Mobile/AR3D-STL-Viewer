package com.example.practica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica.services.catalogoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BusquedaArchivoStlViewModel : ViewModel() {
    private val archivoStlLiveData = MutableLiveData<ArchivoStl>()
    val archivoStl: LiveData<ArchivoStl> = archivoStlLiveData

    private val errorLiveData = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = errorLiveData

    fun getArchivoStl(nombre: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = catalogoApiService().getArchivoStlPorNombre(nombre)
                val archivoStl = ArchivoStl(nombre, response)
                archivoStlLiveData.postValue(archivoStl)
            } catch (e: Exception) {
                errorLiveData.postValue(true)
            }
        }
    }
}

class ArchivoStl(
    var nombre: String,
    var contenido: String
)