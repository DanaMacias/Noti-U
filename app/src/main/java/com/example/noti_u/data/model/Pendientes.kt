package com.example.noti_u.data.model

data class Pendientes(
    val idPendientes: String = "",
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val estado: Boolean = false,
    val materiaEstudianteIdMateriaEstudiante: String = "",
    val materiaIdMateria: String = ""
)
