package com.example.noti_u.data.model

data class Pendientes(
    val idPendientes: Int = 0,
    val titulo: String = "",
    val descripcion: String = "",
    val fecha: String = "",
    val estado: String = "",
    val materiaEstudianteIdMateriaEstudiante: Int = 0,
    val materiaIdMateria: Int = 0
)