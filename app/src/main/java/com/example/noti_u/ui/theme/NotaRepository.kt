package com.example.noti_u

import androidx.compose.runtime.mutableStateListOf

data class Nota(val titulo: String, val descripcion: String)

object NotaRepository {
    val notas = mutableStateListOf<Nota>()

    fun agregarNota(titulo: String, descripcion: String) {
        if (titulo.isNotBlank() && descripcion.isNotBlank()) {
            notas.add(Nota(titulo, descripcion))
        }
    }
}
