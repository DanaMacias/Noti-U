package com.example.noti_u.data.model

data class Recordatorios(
    var id: String = "",              // id generado por Firebase
    val fecha: String = "",
    val hora: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val notificar: Boolean = false,   // Boolean en vez de String
    val userId: String = ""           // id del usuario dueño
)