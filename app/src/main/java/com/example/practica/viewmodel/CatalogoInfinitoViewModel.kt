package com.example.practica.viewmodel

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.practica.services.Objeto3d
import com.example.practica.services.catalogoApiService

class CatalogoInfinitoViewModel(busquedaObjeto3dViewModel: BusquedaObjeto3dViewModel) : PagingSource<Int, Objeto3d>() {

    val textoABuscar = busquedaObjeto3dViewModel.textoABuscar.value?: ""

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Objeto3d> {
        return try {
            val numeroPagina  = params.key ?: 0
            val objetos3d = catalogoApiService().getObjetos3d(numeroPagina, textoABuscar)
            val prevKey = if (numeroPagina > 0) numeroPagina - 1 else null
            val nextKey = if (objetos3d.isNotEmpty()) numeroPagina + 1 else null

            LoadResult.Page(objetos3d, prevKey = prevKey, nextKey = nextKey)
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, Objeto3d>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}