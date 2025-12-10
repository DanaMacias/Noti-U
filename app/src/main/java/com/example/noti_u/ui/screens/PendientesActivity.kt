package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.ui.viewmodel.PendientesViewModel
import androidx.compose.material3.Surface
import androidx.compose.ui.draw.clip
import com.example.noti_u.ui.base.BaseMenuActivity

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
    // Recolectamos el estado de la lista
    val pendientes by vm.pendientes.collectAsState()
    val materiasMap by vm.materiasMap.collectAsState()

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
                    // CHANGED: stringResource
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
                            onEliminar = {
                                vm.eliminarPendiente(pendiente.idPendientes)
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
            containerColor = Color.White, // Fondo Blanco
            contentColor = Color.Black    // Ícono Negro (o el color que prefieras para contraste)
        ) {
            Image(
                painter = painterResource(id = R.drawable.agregar),
                contentDescription = stringResource(R.string.agregar_pendiente),
                modifier = Modifier.size(45.dp)
            )
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
    onEliminar: () -> Unit
) {
    var isPendiente by remember { mutableStateOf(estado) }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp)),
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

// Helper: convierte Materia? -> Color
fun materiaToColor(materia: Materia?): Color {
    return try {
        if (materia?.color.isNullOrEmpty()) {
            Color(0xFFFFF9C4) // default claro
        } else {
            // Acepta formatos "#RRGGBB" o "RRGGBB"
            val hex = materia!!.color.removePrefix("#")
            val colorInt = android.graphics.Color.parseColor("#$hex")
            Color(colorInt)
        }
    } catch (e: Exception) {
        Color(0xFFFFF9C4)
    }
}