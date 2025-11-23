package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import com.example.noti_u.ui.screens.AgregarPendienteActivity
import com.example.noti_u.R

class PendientesActivity : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        pendientePantalla()
    }

    @Composable
    fun pendientePantalla() {
        val pendientes = remember {
            mutableStateListOf(
                Pendiente("Proyecto final", "Materia", "22/01/2025", Color(0xFFD1B2F8), true),
                Pendiente("Estudiar", "Descripción", "22/01/2025", Color(0xFFFFFF99), false),
                Pendiente("Exposición", "Materia", "22/01/2025", Color(0xFFFF8A80), true),
                Pendiente("Proyecto integrador", "Materia", "22/01/2025", Color(0xFF80DEEA), false)
            )
        }

        Box(modifier = Modifier.Companion.fillMaxSize()) {
            Column(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.Companion.CenterHorizontally
            ) {
                LazyColumn(
                    modifier = Modifier.Companion
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pendientes) { pendiente ->
                        PendienteCard(
                            pendiente = pendiente,
                            onEstadoCambiado = { nuevoEstado ->
                                val index = pendientes.indexOf(pendiente)
                                if (index != -1) pendientes[index] =
                                    pendiente.copy(estado = nuevoEstado)
                            }
                        )
                    }
                }
            }

            FloatingActionButton(
                onClick = {
                    val intent =
                        Intent(this@PendientesActivity, AgregarPendienteActivity::class.java)
                    startActivity(intent)
                },
                modifier = Modifier.Companion
                    .align(Alignment.Companion.BottomEnd)
                    .padding(24.dp),
                containerColor = Color.Companion.White
            ) {
                Image(
                    painter = painterResource(id = R.drawable.agregar),
                    contentDescription = "Agregar pendiente",
                    modifier = Modifier.Companion.size(45.dp)
                )
            }
        }
    }

    @Composable
    fun PendienteCard(
        pendiente: Pendiente,
        onEstadoCambiado: (Boolean) -> Unit
    ) {
        var isPendiente by remember { mutableStateOf(pendiente.estado) }

        Box(
            modifier = Modifier.Companion
                .fillMaxWidth()
                .background(pendiente.color, RoundedCornerShape(20.dp))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier.Companion.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        pendiente.titulo,
                        fontWeight = FontWeight.Companion.Bold,
                        fontSize = 18.sp,
                        color = Color.Companion.Black,
                        modifier = Modifier.Companion.weight(1f)
                    )

                    RadioButton(
                        selected = isPendiente,
                        onClick = {
                            isPendiente = !isPendiente
                            onEstadoCambiado(isPendiente)
                        },
                        colors = RadioButtonDefaults.colors(
                            selectedColor = Color(0xFF673AB7),
                            unselectedColor = Color.Companion.DarkGray
                        )
                    )
                }

                Row(
                    modifier = Modifier.Companion.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Companion.CenterVertically
                ) {
                    Text(
                        text = pendiente.descripcion,
                        fontSize = 14.sp,
                        color = Color.Companion.Black,
                        modifier = Modifier.Companion.weight(1f),
                        maxLines = 2,
                        overflow = TextOverflow.Companion.Ellipsis
                    )

                    Spacer(modifier = Modifier.Companion.width(8.dp))

                    Text(
                        text = pendiente.fecha,
                        fontSize = 13.sp,
                        color = Color.Companion.Black.copy(alpha = 0.8f),
                        fontWeight = FontWeight.Companion.Medium
                    )
                }
            }
        }
    }


    data class Pendiente(
        val titulo: String,
        val descripcion: String,
        val fecha: String,
        val color: Color,
        val estado: Boolean
    )
}