package com.example.practica.repository

import android.content.Context
import java.io.File
import java.io.FileOutputStream

fun eliminarObjetoVistoRecientemente(nombreArchivo: String, context: Context): Boolean {
    val directorio = context.getExternalFilesDir(null)

    return File(directorio, nombreArchivo).delete()
}

fun existeElArchivo(context: Context, nombreArchivo: String): Boolean {
    val directorio = context.getExternalFilesDir(null)

    return File(directorio, nombreArchivo).isFile()
}

suspend fun guardarArchivoEnAlmacenamientoExterno(context: Context, nombreArchivo: String, contenido: String) {
    val directorio = context.getExternalFilesDir(null)

    val archivo = File(directorio, nombreArchivo)
    archivo.createNewFile()

    val outputStream = FileOutputStream(archivo)
    outputStream.write(contenido.toByteArray())
    outputStream.close()
}
fun buscarObjetosVistosRecientemente(context: Context): List<String> {
    val nombresDeArchivos = mutableListOf<String>()

    val directorio = context.getExternalFilesDir(null)

    if (directorio != null && directorio.exists() && directorio.isDirectory) {
        val archivos = directorio.listFiles()
        if (archivos != null) {
            for (archivo in archivos) {
                if (archivo.isFile) {
                    nombresDeArchivos.add(archivo.name)
                }
            }
        }
    }
    return nombresDeArchivos
}