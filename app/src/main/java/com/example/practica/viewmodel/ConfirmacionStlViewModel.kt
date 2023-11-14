package com.example.practica.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConfirmacionStlViewModel: ViewModel() {
    private val nombreArchivoStlLiveData = MutableLiveData<String>()
    val nombreArchivoStl: LiveData<String> = nombreArchivoStlLiveData

    fun setNombreArchivoStl(texto: String) {
        nombreArchivoStlLiveData.postValue(texto)
    }

}