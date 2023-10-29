package com.example.practica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica.services.Objeto3d
import com.example.practica.services.catalogoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CatalogoViewModel : ViewModel() {
    private val catalogoLiveData = MutableLiveData<List<Objeto3d>>()
    val catalogo: LiveData<List<Objeto3d>> = catalogoLiveData

    private val errorLiveData = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = errorLiveData

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = isLoadingLiveData

    fun getCatalogo() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = catalogoApiService().getObjetos3d()
                catalogoLiveData.postValue(response)
                isLoadingLiveData.postValue(false)
            } catch (e: Exception) {
                errorLiveData.postValue(true)
                isLoadingLiveData.postValue(false)
            }
        }
    }

}

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