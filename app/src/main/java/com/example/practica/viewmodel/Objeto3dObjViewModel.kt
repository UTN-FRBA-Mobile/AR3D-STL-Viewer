package com.example.practica.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.practica.services.catalogoApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Objeto3dObjViewModel : ViewModel() {
    private val objeto3dObjLiveData = MutableLiveData<String>()
    val objeto3d: LiveData<String> = objeto3dObjLiveData

    private val errorLiveData = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> = errorLiveData

    private val isLoadingLiveData = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = isLoadingLiveData

    fun getObjeto3dObj(nombreObjeto: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = catalogoApiService().getObjetoPorNombre(nombreObjeto)
                objeto3dObjLiveData.postValue(response)
                isLoadingLiveData.postValue(false)
            } catch (e: Exception) {
                errorLiveData.postValue(true)
                isLoadingLiveData.postValue(false)
            }
        }
    }

}