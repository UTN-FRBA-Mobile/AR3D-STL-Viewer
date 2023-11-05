package com.example.practica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BusquedaObjeto3dViewModel : ViewModel() {
    private val textoABuscarLiveData = MutableLiveData<String>()
    val textoABuscar: LiveData<String> = textoABuscarLiveData

    fun setTextoABuscar(texto: String) {
        textoABuscarLiveData.postValue(texto)
    }
}