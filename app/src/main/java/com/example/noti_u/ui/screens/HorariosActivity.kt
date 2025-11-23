package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.R

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
        onCalendarioClick: () -> Unit
    ) {
        Box(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Spacer(modifier = Modifier.Companion.weight(1f))

                    Text(
                        text = "Materias",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Companion.Bold,
                        textAlign = TextAlign.Companion.Center,
                        color = DarkTextColor
                    )



                    Spacer(modifier = Modifier.Companion.weight(1f))
                }

                Divider(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 10.dp),
                    color = Color.Companion.Black,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.Companion.height(40.dp))


                Button(
                    onClick = onNuevaMateriaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.Companion.fillMaxWidth(0.8f)
                ) {
                    Row(
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Nueva Materia", fontWeight = FontWeight.Companion.Bold)
                        Spacer(modifier = Modifier.Companion.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.mas),
                            contentDescription = "Agregar materia",
                            tint = Color.Companion.Black,
                            modifier = Modifier.Companion.size(20.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.Companion.height(12.dp))


                Button(
                    onClick = onCalendarioClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = androidx.compose.foundation.shape.RoundedCornerShape(50.dp),
                    modifier = Modifier.Companion.fillMaxWidth(0.5f)
                ) {
                    Text(
                        "Calendario",
                        color = Color.Companion.Black,
                        fontWeight = FontWeight.Companion.Bold
                    )
                }
            }
        }
    }

    data class Materia(val nombre: String, val horario: String, val color: Color)

    @Composable
    fun MateriaCard(materia: Materia) {
        Surface(
            shape = androidx.compose.foundation.shape.RoundedCornerShape(16.dp),
            color = materia.color,
            modifier = Modifier.Companion
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Row(
                modifier = Modifier.Companion.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Text(
                    text = materia.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Companion.Bold,
                    color = Color(0xFF1C1C1C)
                )
                Text(
                    text = materia.horario,
                    textAlign = TextAlign.Companion.End,
                    color = Color(0xFF1C1C1C),
                    fontSize = 14.sp
                )
            }
        }
    }
}