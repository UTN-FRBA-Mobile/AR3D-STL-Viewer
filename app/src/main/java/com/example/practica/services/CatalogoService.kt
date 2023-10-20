package com.example.practica.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Objeto3d(
    var name: String,
    var img: String
)

interface CatalogoApiService {
    @GET("/catalogo")
    suspend fun getObjetos3d(): List<Objeto3d>

    @GET("/catalogo")
    suspend fun getObjetoPorNombre(@Query("name") name: String): String
}

fun catalogoApiService(): CatalogoApiService {
    val BASE_URL = "https://2d352252-1d8a-4be2-bda5-de2fc9b57cd7.mock.pstmn.io"

    val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(CatalogoApiService::class.java)
}
