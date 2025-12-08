package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia as MateriaModel
import com.example.noti_u.ui.viewmodel.MateriaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


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
                startActivity(Intent(this, NuevaMateriaActivity::class.java))
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

                materias.forEach { materia ->

                    val materiaUI = materia.toMateriaUI()

                    MateriaCard(
                        materia = materiaUI,
                        onClick = { materiaSeleccionada = materiaUI }
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                }

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
                            contentDescription = "Agregar materia",
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


        if (materiaSeleccionada != null) {
            Dialog(onDismissRequest = { materiaSeleccionada = null }) {

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    tonalElevation = 8.dp,
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .padding(16.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Text(
                            text = materiaSeleccionada!!.nombre,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        Text(
                            text = materiaSeleccionada!!.horario,
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )

                        if (materiaSeleccionada!!.salonEnlace.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = materiaSeleccionada!!.salonEnlace,
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = { materiaSeleccionada = null },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFFB300)
                            )
                        ) {
                            Text("Cerrar", color = Color.Black)
                        }
                    }
                }
            }
        }
    }

// TOCA ORGANIZAR

    data class MateriaUI(
        val nombre: String,
        val horario: String,
        val salonEnlace: String,
        val color: Color
    )

    private fun MateriaModel.toMateriaUI(): MateriaUI {

        val horarioString = dias.filter { it.value }.keys.joinToString(separator = "\n") { dia ->
            val inicio = horaInicio[dia]?.removeSuffix(":00") ?: ""
            val fin = horaFin[dia]?.removeSuffix(":00") ?: ""

            "$dia $inicio-$fin"
        }.replace(":", "")

        val detalles = mutableListOf<String>()
        if (!salon.isNullOrBlank()) detalles.add("Salón: $salon")
        if (!enlace.isNullOrBlank()) detalles.add("Enlace: $enlace")

        val composeColor = this.color.toComposeColor()

        return MateriaUI(
            nombre = nombre,
            horario = horarioString,
            salonEnlace = detalles.joinToString(" / "),
            color = composeColor
        )
    }

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

                Column(
                    modifier = Modifier.weight(1f)
                ) {
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
