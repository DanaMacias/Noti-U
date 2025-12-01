package com.example.noti_u.data.model

data class Notas(
    var id: String = "",       // id generado por Firebase
    val titulo: String = "",
    val descripcion: String = "",
    val userId: String = ""    // id del usuario dueño de la nota
)
