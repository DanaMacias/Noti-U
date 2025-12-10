package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.data.model.Recordatorios
import com.example.noti_u.data.repository.MateriaRepository
import com.example.noti_u.data.repository.PendientesRepository
import com.example.noti_u.data.repository.RecordatoriosRepository
import com.example.noti_u.utils.FirebaseDataSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters
import java.util.Locale

class CalendarioViewModel : ViewModel() {

    private val materiaRepo = MateriaRepository()
    private val pendientesRepo = PendientesRepository()
    private val recordatoriosRepo = RecordatoriosRepository()
    private val userId = FirebaseDataSource.auth.currentUser?.uid ?: ""


    private var todasLasMaterias: List<Materia> = emptyList()
    private var todosLosPendientes: List<Pendientes> = emptyList()
    private var todosLosRecordatorios: List<Recordatorios> = emptyList()

    private var mapaMaterias: Map<String, Materia> = emptyMap()

    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada

    private val _lunesSemanaVisible = MutableStateFlow(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY)))
    val lunesSemanaVisible: StateFlow<LocalDate> = _lunesSemanaVisible

    private val _materiasDelDia = MutableStateFlow<List<Materia>>(emptyList())
    val materiasDelDia: StateFlow<List<Materia>> = _materiasDelDia

    private val _pendientesDelDia = MutableStateFlow<List<Pendientes>>(emptyList())
    val pendientesDelDia: StateFlow<List<Pendientes>> = _pendientesDelDia

    private val _recordatoriosDelDia = MutableStateFlow<List<Recordatorios>>(emptyList())
    val recordatoriosDelDia: StateFlow<List<Recordatorios>> = _recordatoriosDelDia

    fun obtenerMateriaDePendiente(idMateria: String): Materia? = mapaMaterias[idMateria]

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            todasLasMaterias = materiaRepo.obtenerMaterias()
            mapaMaterias = todasLasMaterias.associateBy { it.id }

            launch {
                pendientesRepo.obtenerPendientes().collect { lista ->
                    todosLosPendientes = lista
                    filtrarDatos(_fechaSeleccionada.value)
                }
            }

            if (userId.isNotEmpty()) {
                recordatoriosRepo.getRecordatoriosListener(userId) { lista ->
                    todosLosRecordatorios = lista
                    filtrarDatos(_fechaSeleccionada.value)
                }
            }
            filtrarDatos(_fechaSeleccionada.value)
        }
    }

    fun cambiarSemana(avanzar: Boolean) {
        val dias = if (avanzar) 7L else -7L
        _lunesSemanaVisible.value = _lunesSemanaVisible.value.plusDays(dias)
    }

    fun seleccionarFecha(fecha: LocalDate) {
        _fechaSeleccionada.value = fecha
        filtrarDatos(fecha)
    }

    private fun filtrarDatos(fecha: LocalDate) {
        val nombreDiaFirebase = obtenerNombreDiaFirebase(fecha)
        _materiasDelDia.value = todasLasMaterias.filter {
            it.dias[nombreDiaFirebase] == true
        }.sortedBy { it.horaInicio[nombreDiaFirebase] ?: "23:59" }

        val formatoSimple = DateTimeFormatter.ofPattern("d/M/yyyy")
        val formatoCeros = DateTimeFormatter.ofPattern("dd/MM/yyyy")

        val fechaStrSimple = fecha.format(formatoSimple)
        val fechaStrCeros = fecha.format(formatoCeros)

        _pendientesDelDia.value = todosLosPendientes.filter { p ->
            p.fecha == fechaStrSimple || p.fecha == fechaStrCeros
        }

        _recordatoriosDelDia.value = todosLosRecordatorios.filter { r ->
            r.fecha == fechaStrSimple || r.fecha == fechaStrCeros
        }.sortedBy { it.hora }
    }

    private fun obtenerNombreDiaFirebase(date: LocalDate): String {
        return when (date.dayOfWeek) {
            DayOfWeek.MONDAY -> "Lunes"
            DayOfWeek.TUESDAY -> "Martes"
            DayOfWeek.WEDNESDAY -> "Miércoles"
            DayOfWeek.THURSDAY -> "Jueves"
            DayOfWeek.FRIDAY -> "Viernes"
            DayOfWeek.SATURDAY -> "Sábado"
            DayOfWeek.SUNDAY -> "Domingo"
        }
    }
}