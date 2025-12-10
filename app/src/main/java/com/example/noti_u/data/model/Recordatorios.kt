package com.example.noti_u.data.model

data class Recordatorios(
    var id: String = "",
    val fecha: String = "",
    val hora: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val notificar: Boolean = false,
    val userId: String = ""
)