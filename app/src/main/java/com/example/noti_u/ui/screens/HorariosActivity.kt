package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia as MateriaModel
import com.example.noti_u.ui.viewmodel.MateriaViewModel
import androidx.lifecycle.viewmodel.compose.viewModel


fun String.toComposeColor(): Color {


    val hexString = if (this.startsWith("#ff") && this.length > 9) {

        "#" + this.substring(3, 9)
    } else if (this.startsWith("#") && this.length >= 7) {
        this.substring(0, 7)
    } else {
        "#FFB300"
    }

    return try {
        Color(android.graphics.Color.parseColor(hexString))
    } catch (e: IllegalArgumentException) {
        Color(0xFFFFB300)
    }
}
// ------------------------------------------------------------------------

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
                    MateriaCard(materia = materiaUI)
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
                    Text(
                        text = stringResource(id = R.string.nueva_materia),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
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
    }

    //esto toca moverlo

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
        if (!salon.isNullOrBlank()) {
            detalles.add("Salón: $salon")
        }
        if (!enlace.isNullOrBlank()) {
            detalles.add("Enlace: $enlace")
        }
        val salonEnlaceString = detalles.joinToString(separator = " / ")


        val composeColor = this.color.toComposeColor()

        return MateriaUI(
            nombre = this.nombre,
            horario = horarioString,
            salonEnlace = salonEnlaceString,
            color = composeColor
        )
    }

    @Composable
    fun MateriaCard(materia: MateriaUI) {
        Surface(
            shape = RoundedCornerShape(10.dp),
            color = materia.color,
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .height(70.dp)
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