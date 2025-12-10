package com.example.noti_u.ui.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.R
import com.example.noti_u.ui.base.BaseMenuActivity
import com.example.noti_u.data.model.Materia as MateriaModel
import com.example.noti_u.ui.viewmodel.MateriaViewModel

// Extensión para convertir String Hex a Color
fun String.toComposeColor(): Color {
    return try {
        Color(android.graphics.Color.parseColor(this))
    } catch (e: Exception) {
        Color(0xFFFFB300)
    }
}

class HorariosActivity : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        HorarioScreen(
            onNuevaMateriaClick = {
                // Al crear nueva, no pasamos ID
                val intent = Intent(this, NuevaMateriaActivity::class.java)
                startActivity(intent)
            },
            onCalendarioClick = {
                startActivity(Intent(this, CalendarioActivity::class.java))
            }
        )
    }

    @Composable
    fun HorarioScreen(
        onNuevaMateriaClick: () -> Unit,
        onCalendarioClick: () -> Unit,
        viewModel: MateriaViewModel = viewModel()
    ) {
        val context = LocalContext.current

        // Cargar materias al iniciar
        LaunchedEffect(Unit) {
            viewModel.cargarMaterias()
        }

        val materias by viewModel.materias.collectAsState()

        // Estado para manejar la ventana emergente
        var materiaSeleccionada by remember { mutableStateOf<MateriaUI?>(null) }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Spacer(modifier = Modifier.height(30.dp))

                // LISTA DE MATERIAS
                materias.forEach { materia ->
                    val materiaUI = materia.toMateriaUI(context)

                    MateriaCard(
                        materia = materiaUI,
                        onClick = { materiaSeleccionada = materiaUI }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

                // BOTÓN "AGREGAR MATERIA"
                Button(
                    onClick = onNuevaMateriaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(70.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.mas),
                            contentDescription = stringResource(R.string.cd_agregar_materia),
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = stringResource(id = R.string.nueva_materia),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // BOTÓN "IR AL CALENDARIO"
                Button(
                    onClick = onCalendarioClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text(
                        stringResource(id = R.string.calendario_titulo),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // DIÁLOGO DETALLE DE MATERIA
        if (materiaSeleccionada != null) {
            Dialog(onDismissRequest = { materiaSeleccionada = null }) {

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.95f)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // Título (Nombre Materia)
                        Text(
                            text = materiaSeleccionada!!.nombre,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Horario
                        Text(
                            text = materiaSeleccionada!!.horario,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            textAlign = TextAlign.Center
                        )

                        // Salón / Enlace
                        if (materiaSeleccionada!!.salonEnlace.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = materiaSeleccionada!!.salonEnlace,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- BOTONES DE ACCIÓN (EDITAR / ELIMINAR / CERRAR) ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // 1. ELIMINAR
                            Button(
                                onClick = {
                                    viewModel.eliminarMateria(materiaSeleccionada!!.id)
                                    materiaSeleccionada = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                                modifier = Modifier.weight(1f).padding(end = 4.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                Text(
                                    stringResource(R.string.eliminar),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }

                            // 2. EDITAR
                            Button(
                                onClick = {
                                    val intent = Intent(context, NuevaMateriaActivity::class.java)
                                    intent.putExtra("materia_id", materiaSeleccionada!!.id)
                                    context.startActivity(intent)
                                    materiaSeleccionada = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)),
                                modifier = Modifier.weight(1f).padding(horizontal = 4.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                Text(
                                    stringResource(R.string.editar),
                                    color = Color.White,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }

                            // 3. CERRAR
                            Button(
                                onClick = { materiaSeleccionada = null },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                                modifier = Modifier.weight(1f).padding(start = 4.dp),
                                contentPadding = PaddingValues(horizontal = 4.dp)
                            ) {
                                Text(
                                    stringResource(R.string.cerrar),
                                    color = Color.Black,
                                    fontSize = 13.sp,
                                    maxLines = 1
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    // --- MODELO UI INTERNO ---
    data class MateriaUI(
        val id: String,
        val nombre: String,
        val horario: String,
        val salonEnlace: String,
        val color: Color
    )

    // --- MAPEO DE MODELO ---
    private fun MateriaModel.toMateriaUI(context: Context): MateriaUI {
        // Mapeo de nombres de días en español a traducidos
        val diasTraducidos = mapOf(
            "Lunes" to context.getString(R.string.lunes),
            "Martes" to context.getString(R.string.martes),
            "Miércoles" to context.getString(R.string.miercoles),
            "Jueves" to context.getString(R.string.jueves),
            "Viernes" to context.getString(R.string.viernes),
            "Sábado" to context.getString(R.string.sabado),
            "Domingo" to context.getString(R.string.domingo)
        )

        val horarioString = dias.filter { it.value }.keys.joinToString(separator = "\n") { dia ->
            val diaTraducido = diasTraducidos[dia] ?: dia
            val inicio = horaInicio[dia] ?: ""
            val fin = horaFin[dia] ?: ""
            "$diaTraducido $inicio-$fin"
        }

        val detalles = mutableListOf<String>()
        if (!salon.isNullOrBlank()) {
            detalles.add(context.getString(R.string.salon_label, salon))
        }
        if (!enlace.isNullOrBlank()) {
            detalles.add(context.getString(R.string.enlace_label, enlace))
        }

        val composeColor = this.color.toComposeColor()

        return MateriaUI(
            id = id,
            nombre = nombre,
            horario = horarioString,
            salonEnlace = detalles.joinToString(" / "),
            color = composeColor
        )
    }

    // --- TARJETA VISUAL ---
    @Composable
    fun MateriaCard(materia: MateriaUI, onClick: () -> Unit) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = materia.color,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(70.dp)
                .clickable { onClick() }
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = materia.nombre,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1C1C1C)
                    )
                    if (materia.salonEnlace.isNotEmpty()) {
                        Text(
                            text = materia.salonEnlace,
                            fontSize = 10.sp,
                            color = Color(0xFF1C1C1C),
                            fontWeight = FontWeight.Light
                        )
                    }
                }
                Text(
                    text = materia.horario,
                    textAlign = TextAlign.End,
                    color = Color(0xFF1C1C1C),
                    fontSize = 14.sp
                )
            }
        }
    }
}