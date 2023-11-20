package com.example.practica.services

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

data class Objeto3d(
    var name: String,
    var imgUrl: String
)

interface CatalogoApiService {
    @GET("/catalogo")
    suspend fun getObjetos3d(@Query("page") page: Int, @Query("search") search: String = ""): List<Objeto3d>

    @GET("/catalogo")
    suspend fun getObjetoPorNombre(@Query("name") name: String): String

    @GET("/catalogo/stl")
    suspend fun getArchivoStlPorNombre(@Query("name") name: String): String
}

fun catalogoApiService(): CatalogoApiService {
    val BASE_URL = "https://8kqmz.wiremockapi.cloud/"

    val retrofit = Retrofit
        .Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    return retrofit.create(CatalogoApiService::class.java)
}
