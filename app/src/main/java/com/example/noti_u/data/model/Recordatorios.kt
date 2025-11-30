package com.example.noti_u.data.model

data class Recordatorios(
    val idRecordatorios: Int = 0,
    val fecha: String = "",
    val hora: String = "",
    val nombre: String = "",
    val descripcion: String = "",
    val notificar: String = "",
    val materiaEstudianteIdMateriaEstudiante: Int = 0
)