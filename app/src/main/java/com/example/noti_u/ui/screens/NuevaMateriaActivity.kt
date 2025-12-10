package com.example.noti_u.ui.screens

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import com.example.noti_u.ui.viewmodel.MateriaViewModel
import java.util.*

class NuevaMateriaActivity : BaseLanguageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val materiaId = intent.getStringExtra("materia_id")
        setContent {
            NotiUTheme {
                NuevaMateriaScreen(materiaId = materiaId)
            }
        }
    }
}

// Clase auxiliar para manejar la UI vs Base de Datos
data class DiaConfig(
    val nombreRes: Int,  // ID del string (R.string.lunes) -> Cambia con el idioma
    val llaveBD: String  // Llave fija para Firebase ("Lunes") -> NUNCA cambia
)

@Composable
fun NuevaMateriaScreen(
    viewModel: MateriaViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    materiaId: String? = null
) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    var nombre by remember { mutableStateOf("") }
    var salon by remember { mutableStateOf("") }
    var enlace by remember { mutableStateOf("") }
    var colorSeleccionado by remember { mutableStateOf<Color?>(null) }

    // Usamos las LLAVES FIJAS ("Lunes", "Martes") para guardar los datos
    val diasSeleccionados = remember { mutableStateMapOf<String, Boolean>() }
    val horaInicio = remember { mutableStateMapOf<String, String>() }
    val horaFin = remember { mutableStateMapOf<String, String>() }

    var isLoading by remember { mutableStateOf(materiaId != null) }
    val guardado = viewModel.guardadoExitoso.collectAsState()

    // --- LISTA MAESTRA DE DÍAS ---
    // Mapeamos: Lo que se ve (R.string...) <-> Lo que se guarda ("String Fijo")
    val listaDiasConfig = listOf(
        DiaConfig(R.string.lunes, "Lunes"),
        DiaConfig(R.string.martes, "Martes"),
        DiaConfig(R.string.miercoles, "Miércoles"), // Usamos tilde si tu ViewModel la espera
        DiaConfig(R.string.jueves, "Jueves"),
        DiaConfig(R.string.viernes, "Viernes"),
        DiaConfig(R.string.sabado, "Sábado"),       // Usamos tilde si tu ViewModel la espera
        DiaConfig(R.string.domingo, "Domingo")
    )

    // Cargar datos (Edit Mode)
    LaunchedEffect(materiaId) {
        if (materiaId != null) {
            viewModel.obtenerMateriaPorId(materiaId) { materia ->
                if (materia != null) {
                    nombre = materia.nombre
                    salon = materia.salon ?: ""
                    enlace = materia.enlace ?: ""
                    try { colorSeleccionado = Color(android.graphics.Color.parseColor(materia.color)) } catch (e: Exception) { }

                    // Cargamos los datos usando las llaves de la BD
                    materia.dias.forEach { (k, v) -> diasSeleccionados[k] = v }
                    materia.horaInicio.forEach { (k, v) -> horaInicio[k] = v }
                    materia.horaFin.forEach { (k, v) -> horaFin[k] = v }
                }
                isLoading = false
            }
        }
    }

    if (guardado.value) {
        LaunchedEffect(Unit) {
            Toast.makeText(context, context.getString(R.string.materia_guardada), Toast.LENGTH_SHORT).show()
            viewModel.resetGuardado()
            (context as? ComponentActivity)?.finish()
        }
    }

    val colores = listOf(
        Color(0xFFFFF176), Color(0xFFFF8A65), Color(0xFFFFB300),
        Color(0xFFD1C4E9), Color(0xFF80DEEA), Color(0xFFA5D6A7), Color(0xFF4DB6AC)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // ... Header ... (Igual que antes)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.logo), contentDescription = null, modifier = Modifier.size(80.dp))
                    // ... (Menú perfil igual) ...
                }

                Spacer(modifier = Modifier.height(10.dp))
                Divider(color = Color.Black, thickness = 1.dp)

                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    buttonAnimation(drawableId = R.drawable.atras, modifier = Modifier.size(32.dp)) { (context as? ComponentActivity)?.finish() }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (materiaId == null) stringResource(R.string.agregar_editar_materia) else stringResource(R.string.editar_materia),
                        fontWeight = FontWeight.Bold, fontSize = 22.sp, color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Inputs
                Text(text = stringResource(R.string.nombre_materia), fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    value = nombre, onValueChange = { nombre = it },
                    placeholder = { Text(stringResource(R.string.placeholder_nombre_materia)) },
                    shape = RoundedCornerShape(50.dp), modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(12.dp))
                Text(text = stringResource(R.string.seleccione_horario), fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(8.dp))

                // --- BUCLE CORREGIDO ---
                listaDiasConfig.forEach { diaConfig ->

                    // 1. Nombre Visual (Depende del idioma del cel)
                    val nombreVisual = stringResource(diaConfig.nombreRes)

                    // 2. Llave Interna (Siempre Español: "Lunes", "Martes"...)
                    val llave = diaConfig.llaveBD

                    val isChecked = diasSeleccionados[llave] == true

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = { checked ->
                                diasSeleccionados[llave] = checked
                            }
                        )
                        Text(text = nombreVisual, modifier = Modifier.weight(1f))

                        if (isChecked) {
                            Button(onClick = {
                                val cal = Calendar.getInstance()
                                TimePickerDialog(context, { _, h, m ->
                                    horaInicio[llave] = "%02d:%02d".format(h, m)
                                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                            }) { Text(horaInicio[llave] ?: stringResource(R.string.hora_inicio)) }

                            Spacer(modifier = Modifier.width(4.dp))

                            Button(onClick = {
                                val cal = Calendar.getInstance()
                                TimePickerDialog(context, { _, h, m ->
                                    horaFin[llave] = "%02d:%02d".format(h, m)
                                }, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show()
                            }) { Text(horaFin[llave] ?: stringResource(R.string.hora_fin)) }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Salon y Enlace
                Text(stringResource(R.string.salon_opcional))
                OutlinedTextField(value = salon, onValueChange = { salon = it }, placeholder = { Text(stringResource(R.string.placeholder_salon)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp))

                Spacer(modifier = Modifier.height(8.dp))
                Text(stringResource(R.string.enlace_opcional))
                OutlinedTextField(value = enlace, onValueChange = { enlace = it }, placeholder = { Text(stringResource(R.string.placeholder_enlace)) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(50.dp))

                Spacer(modifier = Modifier.height(16.dp))

                // Color
                Text(text = stringResource(R.string.color_opcional))
                Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    colores.forEach { color ->
                        Box(
                            modifier = Modifier.size(40.dp).background(color, CircleShape)
                                .border(width = if (color == colorSeleccionado) 3.dp else 0.dp, color = Color.Black, shape = CircleShape)
                                .clickable { colorSeleccionado = color }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                Button(
                    onClick = {
                        val colorHex = String.format("#%06X", (colorSeleccionado ?: Color(0xFFFFB300)).toArgb() and 0xFFFFFF)

                        val materia = Materia(
                            id = materiaId ?: "",
                            nombre = nombre,
                            dias = diasSeleccionados.toMap(), // Guarda con llaves fijas ("Lunes", etc)
                            horaInicio = horaInicio.toMap(),
                            horaFin = horaFin.toMap(),
                            color = colorHex,
                            salon = salon.ifBlank { null },
                            enlace = enlace.ifBlank { null }
                        )
                        viewModel.guardarMateria(materia)
                    },
                    modifier = Modifier.fillMaxWidth(0.6f)
                ) {
                    Text(if (materiaId == null) stringResource(R.string.guardar) else stringResource(R.string.actualizar))
                }
            }
        }
    }
}