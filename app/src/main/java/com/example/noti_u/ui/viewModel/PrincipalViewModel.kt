package com.example.noti_u.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.data.repository.MateriaRepository
import com.example.noti_u.data.repository.PendientesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class PrincipalViewModel : ViewModel() {

    private val pendientesRepo = PendientesRepository()
    private val materiaRepo = MateriaRepository()

    // Ahora exponemos pendientes separados para hoy y mañana
    private val _pendientesHoy = MutableStateFlow<List<Pendientes>>(emptyList())
    val pendientesHoy: StateFlow<List<Pendientes>> = _pendientesHoy.asStateFlow()

    private val _pendientesManana = MutableStateFlow<List<Pendientes>>(emptyList())
    val pendientesManana: StateFlow<List<Pendientes>> = _pendientesManana.asStateFlow()

    // Materias separadas para hoy y mañana: Pair(Materia, horarioString)
    private val _materiasHoy = MutableStateFlow<List<Pair<Materia, String>>>(emptyList())
    val materiasHoy: StateFlow<List<Pair<Materia, String>>> = _materiasHoy.asStateFlow()

    private val _materiasManana = MutableStateFlow<List<Pair<Materia, String>>>(emptyList())
    val materiasManana: StateFlow<List<Pair<Materia, String>>> = _materiasManana.asStateFlow()

    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private val dayFormat = SimpleDateFormat("EEEE", Locale("es", "ES")) // nombre del día en español

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            // Cargamos materias (una vez)
            val materias = try { materiaRepo.obtenerMaterias() } catch (e: Exception) { emptyList() }

            // Calculamos materias para hoy y mañana
            _materiasHoy.value = calcularMateriasParaDia(materias, 0)
            _materiasManana.value = calcularMateriasParaDia(materias, 1)

            // Escuchamos pendientes en tiempo real
            pendientesRepo.obtenerPendientes().collect { listaPendientes ->
                val (hoy, manana) = procesarPendientesHoyYManana(listaPendientes)
                _pendientesHoy.value = hoy
                _pendientesManana.value = manana
            }
        }
    }

    // Calcula materias para dayOffset: 0 = hoy, 1 = mañana
    private fun calcularMateriasParaDia(materias: List<Materia>, dayOffset: Int): List<Pair<Materia, String>> {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, dayOffset)
        val fecha = cal.time
        val nombreDia = normalizeDayName(dayFormat.format(fecha)) // ej: "martes"

        val resultado = mutableListOf<Pair<Materia, String>>()

        materias.forEach { materia ->
            val matchingDayKey = materia.dias.keys.firstOrNull { key ->
                normalizeDayName(key) == nombreDia
            }

            val tieneClase = matchingDayKey?.let { materia.dias[it] } ?: false
            if (tieneClase) {
                val inicio = findMapValueByDayKey(materia.horaInicio, matchingDayKey) ?: "Hora no disponible"
                val fin = findMapValueByDayKey(materia.horaFin, matchingDayKey) ?: "Hora no disponible"
                val horario = "$inicio - $fin"
                resultado.add(Pair(materia, horario))
            }
        }

        return resultado
    }

    // Normaliza un nombre de día: quita mayúsculas y espacios
    private fun normalizeDayName(day: String?): String {
        return day?.trim()?.lowercase(Locale("es", "ES")) ?: ""
    }

    // Busca valor en el mapa usando key tal cual o comparando lowercase con tolerancia
    private fun findMapValueByDayKey(map: Map<String, String>, dayKey: String?): String? {
        if (dayKey == null) return null
        map[dayKey]?.let { return it }
        val found = map.entries.firstOrNull { (k, _) ->
            k.trim().lowercase(Locale("es", "ES")) == dayKey.trim().lowercase(Locale("es", "ES"))
        }
        if (found != null) return found.value
        val dayLower = dayKey.trim().lowercase(Locale("es", "ES")).split(" ").firstOrNull() ?: return null
        val found2 = map.entries.firstOrNull { (k, _) ->
            k.trim().lowercase(Locale("es", "ES")).split(" ").firstOrNull() == dayLower
        }
        return found2?.value
    }

    // Procesa pendientes en 2 listas: hoy (desde 00:00 hasta 23:59:59 de hoy) y mañana (desde 00:00 mañana hasta 23:59:59 mañana)
    private fun procesarPendientesHoyYManana(pendientes: List<Pendientes>): Pair<List<Pendientes>, List<Pendientes>> {
        val cal = Calendar.getInstance()
        // inicio hoy 00:00
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        val inicioHoy = cal.time.time

        // fin hoy 23:59:59
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val finHoy = cal.time.time

        // inicio mañana 00:00
        cal.add(Calendar.DAY_OF_YEAR, 1)
        cal.set(Calendar.HOUR_OF_DAY, 0)
        cal.set(Calendar.MINUTE, 0)
        cal.set(Calendar.SECOND, 0)
        val inicioManana = cal.time.time

        // fin mañana 23:59:59
        cal.set(Calendar.HOUR_OF_DAY, 23)
        cal.set(Calendar.MINUTE, 59)
        cal.set(Calendar.SECOND, 59)
        val finManana = cal.time.time

        val parsedList = pendientes
            .filter { !it.estado } // solo no completados
            .mapNotNull { pendiente ->
                try {
                    val parsed = dateFormat.parse(pendiente.fecha)
                    if (parsed == null) return@mapNotNull null
                    val time = parsed.time
                    pendiente to time
                } catch (e: Exception) {
                    null
                }
            }

        val hoyList = parsedList
            .filter { (_, time) -> time in inicioHoy..finHoy }
            .sortedBy { it.second }
            .map { it.first }

        val mananaList = parsedList
            .filter { (_, time) -> time in inicioManana..finManana }
            .sortedBy { it.second }
            .map { it.first }

        return Pair(hoyList, mananaList)
    }
}