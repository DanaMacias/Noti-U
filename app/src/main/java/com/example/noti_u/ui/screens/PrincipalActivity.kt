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
import androidx.compose.material3.*
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
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia
import com.example.noti_u.data.model.Pendientes
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.base.BaseMenuActivity
import com.example.noti_u.ui.viewmodel.PrincipalViewModel

class PrincipalActivity : BaseMenuActivity() {
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
fun PrincipalScreen(viewModel: PrincipalViewModel = viewModel()) {
    val pendientesHoy by viewModel.pendientesHoy.collectAsState()
    val pendientesManana by viewModel.pendientesManana.collectAsState()
    val materiasHoy by viewModel.materiasHoy.collectAsState()
    val materiasManana by viewModel.materiasManana.collectAsState()

    // Estados para el diálogo de detalle
    var pendienteSeleccionado by remember { mutableStateOf<Pendientes?>(null) }
    var materiaSeleccionada by remember { mutableStateOf<Pair<Materia, String>?>(null) }

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
                            // CHANGED: stringResource
                            Text(
                                text = stringResource(R.string.no_hay_recordatorios),
                                fontWeight = FontWeight.Medium,
                                fontSize = 16.sp,
                                color = Color.DarkGray,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    } else {
                        // Pendientes - Hoy
                        if (pendientesHoy.isNotEmpty()) {
                            // CHANGED: stringResource
                            item { Text(stringResource(R.string.pendientes_hoy), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(pendientesHoy) { p ->
                                RecordatorioBox(
                                    titulo = stringResource(R.string.formato_pendiente, p.titulo.ifBlank { stringResource(R.string.sin_titulo) }),
                                    subtitulo = p.descripcion.ifBlank { stringResource(R.string.sin_descripcion) },
                                    fecha = stringResource(R.string.formato_vence, p.fecha),
                                    onClick = {
                                        pendienteSeleccionado = p
                                        materiaSeleccionada = null
                                    }
                                )
                            }
                        }

                        // Pendientes - Mañana
                        if (pendientesManana.isNotEmpty()) {
                            // CHANGED: stringResource
                            item { Spacer(modifier = Modifier.height(8.dp)); Text(stringResource(R.string.pendientes_manana), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(pendientesManana) { p ->
                                RecordatorioBox(
                                    titulo = stringResource(R.string.formato_pendiente, p.titulo.ifBlank { stringResource(R.string.sin_titulo) }),
                                    subtitulo = p.descripcion.ifBlank { stringResource(R.string.sin_descripcion) },
                                    fecha = stringResource(R.string.formato_vence, p.fecha),
                                    onClick = {
                                        pendienteSeleccionado = p
                                        materiaSeleccionada = null
                                    }
                                )
                            }
                        }

                        // Materias - Hoy
                        if (materiasHoy.isNotEmpty()) {
                            // CHANGED: stringResource
                            item { Spacer(modifier = Modifier.height(8.dp)); Text(stringResource(R.string.clases_hoy), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(materiasHoy) { (materia, horario) ->
                                RecordatorioBox(
                                    titulo = stringResource(R.string.formato_clase, materia.nombre.ifBlank { stringResource(R.string.materia_default) }),
                                    subtitulo = listOfNotNull(
                                        stringResource(R.string.formato_horario, horario),
                                        materia.salon?.let { stringResource(R.string.formato_salon, it) }
                                    ).joinToString(" · "),
                                    fecha = stringResource(R.string.hoy),
                                    onClick = {
                                        materiaSeleccionada = Pair(materia, horario)
                                        pendienteSeleccionado = null
                                    }
                                )
                            }
                        }

                        // Materias - Mañana
                        if (materiasManana.isNotEmpty()) {
                            // CHANGED: stringResource
                            item { Spacer(modifier = Modifier.height(8.dp)); Text(stringResource(R.string.clases_manana), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, color = Color.Black) }
                            items(materiasManana) { (materia, horario) ->
                                RecordatorioBox(
                                    titulo = stringResource(R.string.formato_clase, materia.nombre.ifBlank { stringResource(R.string.materia_default) }),
                                    subtitulo = listOfNotNull(
                                        stringResource(R.string.formato_horario, horario),
                                        materia.salon?.let { stringResource(R.string.formato_salon, it) }
                                    ).joinToString(" · "),
                                    fecha = stringResource(R.string.manana),
                                    onClick = {
                                        materiaSeleccionada = Pair(materia, horario)
                                        pendienteSeleccionado = null
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.paginas_recomendadas),
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
                        text = p.titulo.ifBlank { stringResource(R.string.sin_titulo) },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.formato_descripcion, p.descripcion.ifBlank { stringResource(R.string.sin_descripcion) }),
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.formato_fecha, p.fecha),
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    val estadoTexto = if (p.estado) stringResource(R.string.completado) else stringResource(R.string.pendiente_estado)
                    Text(
                        text = stringResource(R.string.formato_estado, estadoTexto),
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
                        Text(stringResource(R.string.cerrar), color = Color.Black)
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
                        text = m.nombre.ifBlank { stringResource(R.string.materia_default) },
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = stringResource(R.string.formato_horario, horario),
                        fontSize = 16.sp,
                        color = Color.DarkGray
                    )

                    if (!m.salon.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.formato_salon, m.salon),
                            fontSize = 16.sp,
                            color = Color.DarkGray
                        )
                    }

                    if (!m.enlace.isNullOrBlank()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = stringResource(R.string.formato_enlace, m.enlace),
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
                        Text(stringResource(R.string.cerrar), color = Color.Black)
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