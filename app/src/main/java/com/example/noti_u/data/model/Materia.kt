package com.example.noti_u.data.model



data class Materia(
    val id: String = "",
    val nombre: String = "",
    val dias: Map<String, Boolean> = emptyMap(),
    val horaInicio: Map<String, String> = emptyMap(),
    val horaFin: Map<String, String> = emptyMap(),
    val color: String = "#FFB300",
    val salon: String? = null,
    val enlace: String? = null
)
