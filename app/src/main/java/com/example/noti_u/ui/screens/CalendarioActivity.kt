package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.data.model.Recordatorios
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import com.example.noti_u.ui.viewmodel.CalendarioViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class CalendarioActivity : BaseLanguageActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                CalendarioScreen()
            }
        }
    }
}

@Composable
fun CalendarioScreen(
    viewModel: CalendarioViewModel = viewModel()
) {

    // ---------------------------------------

    // ESTADOS DEL VIEWMODEL (el resto sigue igual)

    // ESTADOS DEL VIEWMODEL
    val fechaSeleccionada by viewModel.fechaSeleccionada.collectAsState()
    val lunesSemanaVisible by viewModel.lunesSemanaVisible.collectAsState()

    // Listas de datos
    val materiasDelDia by viewModel.materiasDelDia.collectAsState()
    val pendientesDelDia by viewModel.pendientesDelDia.collectAsState()
    val recordatoriosDelDia by viewModel.recordatoriosDelDia.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // Corrección: Usamos Locale.getDefault() o explícito para evitar errores
    val mesFormatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale("es", "ES"))

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // --- HEADER ---
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.logo),
                    modifier = Modifier.size(80.dp)
                )

                Box {
                    buttonAnimation(
                        drawableId = R.drawable.perfil,
                        modifier = Modifier.size(40.dp)
                    ) { expanded = !expanded }

                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        DropdownMenuItem(text = { Text(stringResource(R.string.ver_perfil)) }, onClick = { expanded = false; context.startActivity(Intent(context, PerfilActivity::class.java)) })
                        DropdownMenuItem(text = { Text(stringResource(R.string.editar_perfil)) }, onClick = { expanded = false; context.startActivity(Intent(context, EditarPerfilActivity::class.java)) })
                        DropdownMenuItem(text = { Text(stringResource(R.string.cerrar_sesion)) }, onClick = { expanded = false; context.startActivity(Intent(context, MainActivity::class.java)); (context as? ComponentActivity)?.finish() })
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                buttonAnimation(drawableId = R.drawable.atras, modifier = Modifier.size(32.dp)) { (context as? ComponentActivity)?.finish() }
                Text(
                    text = stringResource(R.string.calendario_titulo),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    color = Color.Black
                )
                buttonAnimation(drawableId = R.drawable.editar, modifier = Modifier.size(32.dp)) { }
            }

            Divider(modifier = Modifier.fillMaxWidth().padding(top = 6.dp, bottom = 10.dp), color = Color.Black, thickness = 1.dp)

            // --- NAVEGACIÓN SEMANAL ---

            // Corrección: Formateo seguro del texto del mes
            val tituloMes = lunesSemanaVisible.format(mesFormatter)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

            Text(
                text = tituloMes,
                fontSize = 16.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { viewModel.cambiarSemana(false) }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Anterior", tint = Color.Black)
                }

                Row(modifier = Modifier.weight(1f), horizontalArrangement = Arrangement.SpaceAround) {
                    for (i in 0 until 7) {
                        val diaActual = lunesSemanaVisible.plusDays(i.toLong())
                        val esSeleccionado = diaActual == fechaSeleccionada
                        DiaItem(fecha = diaActual, esSeleccionado = esSeleccionado, onClick = { viewModel.seleccionarFecha(diaActual) })
                    }
                }

                IconButton(onClick = { viewModel.cambiarSemana(true) }) {
                    Icon(Icons.Default.ArrowForward, contentDescription = "Siguiente", tint = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // --- LISTA ---

            if (materiasDelDia.isEmpty() && pendientesDelDia.isEmpty() && recordatoriosDelDia.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No hay actividades para este día", color = Color.Gray, fontSize = 16.sp)
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(bottom = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // MATERIAS
                    if (materiasDelDia.isNotEmpty()) {
                        item { SeccionHeader("Clases") }
                        items(materiasDelDia) { materia ->
                            MateriaSemanaCard(materia, fechaSeleccionada)
                        }
                    }

                    // PENDIENTES
                    if (pendientesDelDia.isNotEmpty()) {
                        item { SeccionHeader("Entregas y Pendientes") }
                        items(pendientesDelDia) { pendiente ->
                            val materiaAsociada = viewModel.obtenerMateriaDePendiente(pendiente.materiaIdMateria)
                            PendienteCalendarioCard(pendiente, materiaAsociada)
                        }
                    }

                    // RECORDATORIOS
                    if (recordatoriosDelDia.isNotEmpty()) {
                        item { SeccionHeader("Recordatorios Personales") }
                        items(recordatoriosDelDia) { recordatorio ->
                            RecordatorioCalendarioCard(recordatorio)
                        }
                    }
                }
            }
        }
    }
}

// --------------------------------------------------------
// COMPONENTES DE UI
// --------------------------------------------------------

@Composable
fun SeccionHeader(titulo: String) {
    Text(
        text = titulo,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        color = Color.Gray,
        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
    )
}

@Composable
fun PendienteCalendarioCard(pendiente: Pendientes, materia: Materia?) {
    val colorFondo = try {
        if (!materia?.color.isNullOrEmpty()) Color(android.graphics.Color.parseColor(materia!!.color))
        else Color(0xFFD1C4E9)
    } catch (e: Exception) { Color(0xFFD1C4E9) }

    Surface(
        shape = RoundedCornerShape(12.dp),
        color = colorFondo.copy(alpha = 0.3f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Corrección: Usamos Iconos nativos en vez de R.drawable
            val icono = if (pendiente.estado) Icons.Default.Check else Icons.Default.Notifications
            val colorIcono = if (pendiente.estado) Color(0xFF4CAF50) else Color(0xFFF44336)

            Icon(
                imageVector = icono,
                contentDescription = null,
                tint = colorIcono,
                modifier = Modifier.size(24.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = pendiente.titulo.ifBlank { materia?.nombre ?: "Pendiente" },
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                if (materia != null) {
                    Text(text = materia.nombre, fontSize = 12.sp, color = Color.DarkGray)
                }
                if (pendiente.descripcion.isNotEmpty()) {
                    Text(text = pendiente.descripcion, fontSize = 12.sp, color = Color.Gray, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun RecordatorioCalendarioCard(recordatorio: Recordatorios) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFF8DD8E1).copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = recordatorio.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Text(text = recordatorio.descripcion, fontSize = 12.sp, color = Color.DarkGray, maxLines = 1)
            }
            Text(
                text = recordatorio.hora,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = Color.Black
            )
        }
    }
}

@Composable
fun DiaItem(fecha: LocalDate, esSeleccionado: Boolean, onClick: () -> Unit) {
    // Corrección: Formateo de letras de día seguro
    val diaSemanaLetra = fecha.format(DateTimeFormatter.ofPattern("EEE", Locale("es", "ES")))
        .take(1)
        .uppercase(Locale.getDefault())

    val numeroDia = fecha.dayOfMonth.toString()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .background(if (esSeleccionado) Color(0xFFFFB300) else Color.Transparent)
            .padding(8.dp)
    ) {
        Text(text = diaSemanaLetra, fontSize = 12.sp, color = if (esSeleccionado) Color.Black else Color.Gray)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = numeroDia, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = if (esSeleccionado) Color.Black else Color.Black)
    }
}

@Composable
fun MateriaSemanaCard(materia: Materia, fecha: LocalDate) {
    // Definimos el día base en español
    val diaBase = when (fecha.dayOfWeek.value) {
        1 -> "Lunes" 2 -> "Martes" 3 -> "Miércoles" 4 -> "Jueves"
        5 -> "Viernes" 6 -> "Sábado" 7 -> "Domingo" else -> "Lunes"
    }

    // Buscamos la llave del mapa ignorando mayúsculas y tildes
    val keyCorrecta = materia.horaInicio.keys.find { key ->
        key.lowercase().replace("á","a").replace("é","e").replace("í","i").replace("ó","o") ==
                diaBase.lowercase().replace("á","a").replace("é","e").replace("í","i").replace("ó","o")
    } ?: diaBase

    val horaInicio = materia.horaInicio[keyCorrecta] ?: "--:--"
    val horaFin = materia.horaFin[keyCorrecta] ?: "--:--"
    val colorMateria = try { Color(android.graphics.Color.parseColor(materia.color)) } catch (e: Exception) { Color(0xFFFFB300) }

    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {
        Column(horizontalAlignment = Alignment.End, modifier = Modifier.width(60.dp).padding(end = 8.dp, top = 8.dp)) {
            Text(text = horaInicio, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = horaFin, fontSize = 12.sp, color = Color.Gray)
        }
        Surface(shape = RoundedCornerShape(16.dp), color = colorMateria.copy(alpha = 0.2f), modifier = Modifier.weight(1f)) {
            Row(modifier = Modifier.fillMaxSize()) {
                Box(modifier = Modifier.fillMaxHeight().width(6.dp).background(colorMateria))
                Column(modifier = Modifier.padding(12.dp).fillMaxSize(), verticalArrangement = Arrangement.Center) {
                    Text(text = materia.nombre, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color.Black)
                    if (!materia.salon.isNullOrEmpty()) Text(text = "Salón: ${materia.salon}", fontSize = 14.sp, color = Color.DarkGray)
                }
            }
        }
    }

}