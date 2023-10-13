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
    val BASE_URL = "https://cc9c5264-8d0c-4160-9104-4da34d073a89.mock.pstmn.io"

    val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(CatalogoApiService::class.java)
}
