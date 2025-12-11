package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.ui.base.BaseMenuActivity
import com.example.noti_u.ui.viewmodel.PendientesViewModel

class PendientesActivity : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        val vm: PendientesViewModel = viewModel()
        PendientesScreen(vm = vm)
    }
}

@Composable
fun PendientesScreen(vm: PendientesViewModel) {
    val context = LocalContext.current
    val pendientes by vm.pendientes.collectAsState()
    val materiasMap by vm.materiasMap.collectAsState()

    // Estado para la ventana emergente
    var pendienteSeleccionado by remember { mutableStateOf<Pendientes?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (pendientes.isEmpty()) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = stringResource(R.string.no_hay_pendientes),
                        fontSize = 18.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(pendientes, key = { it.idPendientes }) { pendiente ->
                        val materia = materiasMap[pendiente.materiaIdMateria]
                        val titulo = pendiente.titulo.ifBlank { materia?.nombre ?: stringResource(R.string.sin_titulo) }
                        val descripcion = pendiente.descripcion
                        val fecha = pendiente.fecha
                        val cardColor = materiaToColor(materia)

                        PendienteCard(
                            titulo = titulo,
                            descripcion = descripcion,
                            fecha = fecha,
                            color = cardColor,
                            estado = pendiente.estado,
                            onCambioEstado = { nuevoEstado ->
                                vm.cambiarEstadoPendiente(pendiente, nuevoEstado)
                            },
                            // Al hacer click en la tarjeta, abrimos el diálogo
                            onClick = {
                                pendienteSeleccionado = pendiente
                            }
                        )
                    }
                }
            }
        }

        FloatingActionButton(
            onClick = {
                val intent = Intent(context, AgregarPendienteActivity::class.java)
                context.startActivity(intent)
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Color.White,
            contentColor = Color.Black
        ) {
            Image(
                painter = painterResource(id = R.drawable.agregar),
                contentDescription = stringResource(R.string.agregar_pendiente),
                modifier = Modifier.size(45.dp)
            )
        }

        // --- DIÁLOGO EMERGENTE (POPUP) ---
        if (pendienteSeleccionado != null) {
            val p = pendienteSeleccionado!!
            val mat = materiasMap[p.materiaIdMateria]
            val tituloDisplay = p.titulo.ifBlank { mat?.nombre ?: stringResource(R.string.sin_titulo) }

            Dialog(onDismissRequest = { pendienteSeleccionado = null }) {
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
                        // Título
                        Text(
                            text = tituloDisplay,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Descripción
                        if (p.descripcion.isNotBlank()) {
                            Text(
                                text = p.descripcion,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                        }

                        // Fecha
                        Text(
                            text = stringResource(R.string.fecha_entrega) + ": " + p.fecha,
                            fontSize = 16.sp,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Medium
                        )

                        // Nombre Materia
                        if (mat != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = mat.nombre,
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // --- BOTONES (ELIMINAR / EDITAR / CERRAR) ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            // 1. ELIMINAR
                            Button(
                                onClick = {
                                    vm.eliminarPendiente(p.idPendientes)
                                    pendienteSeleccionado = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)), // Rojo
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
                                    val intent = Intent(context, AgregarPendienteActivity::class.java)
                                    // Pasamos el ID para editar
                                    intent.putExtra("pendiente_id", p.idPendientes)
                                    context.startActivity(intent)
                                    pendienteSeleccionado = null
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF42A5F5)), // Azul
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
                                onClick = { pendienteSeleccionado = null },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)), // Amarillo
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
}

@Composable
fun PendienteCard(
    titulo: String,
    descripcion: String,
    fecha: String,
    color: Color,
    estado: Boolean,
    onCambioEstado: (Boolean) -> Unit,
    onClick: () -> Unit // Nuevo parámetro para el click general
) {
    var isPendiente by remember(estado) { mutableStateOf(estado) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .clickable { onClick() }, // Hacemos click en toda la tarjeta
        color = color
    ) {
        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    titulo,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f)
                )

                RadioButton(
                    selected = isPendiente,
                    onClick = {
                        isPendiente = !isPendiente
                        onCambioEstado(isPendiente)
                    },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = Color(0xFF673AB7),
                        unselectedColor = Color.DarkGray
                    )
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = descripcion,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.weight(1f),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = fecha,
                    fontSize = 13.sp,
                    color = Color.Black.copy(alpha = 0.8f),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

fun materiaToColor(materia: Materia?): Color {
    return try {
        if (materia?.color.isNullOrEmpty()) {
            Color(0xFFFFF9C4)
        } else {
            val hex = materia!!.color.removePrefix("#")
            val colorInt = android.graphics.Color.parseColor("#$hex")
            Color(colorInt)
        }
    } catch (e: Exception) {
        Color(0xFFFFF9C4)
    }
}