package com.example.practica.services

import android.graphics.Paint.Cap
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

data class Capitulo(
    var id: String,
    var name: String,
    var episode: String
)

data class CapitulosPorPagina(
    var results: List<Capitulo> = emptyList()
)

interface ApiService {
    @GET("episode/{capitulo}")
    suspend fun getCapitulo(@Path("capitulo") capitulo: Int): Capitulo

    @GET("episode")
    suspend fun getCapitulos(@Query("page") page: Int): CapitulosPorPagina
}

fun apiService(): ApiService {
    val BASE_URL = "https://rickandmortyapi.com/api/"

    val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(ApiService::class.java)
}
