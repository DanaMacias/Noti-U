package com.example.noti_u.ui.screens

import android.R
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.R as AppR
import com.example.noti_u.ui.viewmodel.PrincipalViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import androidx.compose.ui.window.Dialog


class PrincipalActivity :  BaseMenuActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }
    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        PrincipalScreen()
    }
}
@Composable
fun PrincipalScreen(viewModel: PrincipalViewModel = androidx.lifecycle.viewmodel.compose.viewModel()) {
    val pendientesHoy by viewModel.pendientesHoy.collectAsState()
    val pendientesManana by viewModel.pendientesManana.collectAsState()
    val materiasHoy by viewModel.materiasHoy.collectAsState()
    val materiasManana by viewModel.materiasManana.collectAsState()

    // Estados para el diálogo de detalle
    var pendienteSeleccionado by remember { mutableStateOf<Pendientes?>(null) }
    var materiaSeleccionada by remember { mutableStateOf<Pair<Materia, String>?>(null) }
    var dialogTitle by remember { mutableStateOf("") }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
        ) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    val todasVacias = pendientesHoy.isEmpty() && pendientesManana.isEmpty() &&
                            materiasHoy.isEmpty() && materiasManana.isEmpty()
                    if (todasVacias) {
                        item {
                            Text(
                                text = "No hay recordatorios para hoy o mañana",
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        // Pendientes - Hoy
                        if (pendientesHoy.isNotEmpty()) {
                            item { Text("Pendientes - Hoy", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(pendientesHoy) { p ->
                                RecordatorioBox(
                                    titulo = "Pendiente: ${p.titulo.ifBlank { "Sin título" }}",
                                    subtitulo = p.descripcion.ifBlank { "Sin descripción" },
                                    fecha = "Vence: ${p.fecha}",
                                    onClick = {
                                        pendienteSeleccionado = p
                                        materiaSeleccionada = null
                                        dialogTitle = "Detalle pendiente"
                                    }
                                )
                            }
                        }

                        // Pendientes - Mañana
                        if (pendientesManana.isNotEmpty()) {
                            item { Spacer(modifier = Modifier.height(8.dp)); Text("Pendientes - Mañana", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(pendientesManana) { p ->
                                RecordatorioBox(
                                    titulo = "Pendiente: ${p.titulo.ifBlank { "Sin título" }}",
                                    subtitulo = p.descripcion.ifBlank { "Sin descripción" },
                                    fecha = "Vence: ${p.fecha}",
                                    onClick = {
                                        pendienteSeleccionado = p
                                        materiaSeleccionada = null
                                        dialogTitle = "Detalle pendiente"
                                    }
                                )
                            }
                        }

                        // Materias - Hoy
                        if (materiasHoy.isNotEmpty()) {
                            item { Spacer(modifier = Modifier.height(8.dp)); Text("Clases - Hoy", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(materiasHoy) { (materia, horario) ->
                                RecordatorioBox(
                                    titulo = "Clase: ${materia.nombre.ifBlank { "Materia" }}",
                                    subtitulo = listOfNotNull("Horario: $horario", materia.salon?.let { "Salón: $it" }).joinToString(" · "),
                                    fecha = "Hoy",
                                    onClick = {
                                        materiaSeleccionada = Pair(materia, horario)
                                        pendienteSeleccionado = null
                                        dialogTitle = "Detalle clase"
                                    }
                                )
                            }
                        }

                        // Materias - Mañana
                        if (materiasManana.isNotEmpty()) {
                            item { Spacer(modifier = Modifier.height(8.dp)); Text("Clases - Mañana", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(materiasManana) { (materia, horario) ->
                                RecordatorioBox(
                                    titulo = "Clase: ${materia.nombre.ifBlank { "Materia" }}",
                                    subtitulo = listOfNotNull("Horario: $horario", materia.salon?.let { "Salón: $it" }).joinToString(" · "),
                                    fecha = "Mañana",
                                    onClick = {
                                        materiaSeleccionada = Pair(materia, horario)
                                        pendienteSeleccionado = null
                                        dialogTitle = "Detalle clase"
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(AppR.string.paginas_recomendadas),
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
                    .background(Color(0xFF5B8D8D), RoundedCornerShape(20.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Diálogo para pendiente
    if (pendienteSeleccionado != null) {
        val p = pendienteSeleccionado!!

        Dialog(onDismissRequest = { pendienteSeleccionado = null }) {

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
                        text = p.titulo.ifBlank { "Sin título" },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Descripción: ${p.descripcion.ifBlank { "Sin descripción" }}",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Fecha: ${p.fecha}",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Estado: ${if (p.estado) "Completado" else "Pendiente"}",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { pendienteSeleccionado = null },
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


    // Diálogo para materia
    if (materiaSeleccionada != null) {
        val (m, horario) = materiaSeleccionada!!

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
                        text = m.nombre.ifBlank { "Materia" },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "Horario: $horario",
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    if (!m.salon.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Salón: ${m.salon}",
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }

                    if (!m.enlace.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Enlace: ${m.enlace}",
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

@Composable
fun RecordatorioBox(
    titulo: String,
    subtitulo: String,
    fecha: String,
    onClick: () -> Unit = {}
) {
    val displaySub = remember(subtitulo) {
        if (subtitulo.length > 16) subtitulo.take(16) + "…" else subtitulo
    }
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8DD8E1), RoundedCornerShape(16.dp))
            .padding(16.dp)
            .clickable { onClick() }
    ) {
        Column {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = displaySub,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(fecha, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
        }
    }
}
