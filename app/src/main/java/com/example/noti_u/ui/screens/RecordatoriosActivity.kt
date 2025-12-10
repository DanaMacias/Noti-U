package com.example.noti_u.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noti_u.R
import com.example.noti_u.ui.theme.buttonAnimation
import com.example.noti_u.ui.viewmodel.RecordatoriosViewModel
import com.example.noti_u.utils.FirebaseDataSource
import java.util.*
import androidx.compose.runtime.DisposableEffect
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.base.BaseMenuActivity

val DarkTextPrimary = Color(0xFF1C1C1C)
val DarkTextSecondary = Color(0xFF2F2F2F)
val DarkTextLight = Color(0xFF4A4A4A)

class RecordatoriosActivity : BaseMenuActivity() {

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        val viewModel: RecordatoriosViewModel = viewModel()
        val context = LocalContext.current

        // Create notification channel on start
        LaunchedEffect(Unit) {
            com.example.noti_u.utils.NotificationHelper.createNotificationChannel(context)
        }

        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("lista") {
                RecordatoriosListScreen(
                    viewModel = viewModel,
                    onAgregarNuevo = { navController.navigate("formulario") },
                    onEditarRecordatorio = { recordatorioId ->
                        navController.navigate("editar/$recordatorioId")
                    }
                )
            }
            composable("formulario") {
                FormularioRecordatorioScreen(
                    viewModel = viewModel,
                    onVolver = { navController.popBackStack() }
                )
            }
            composable("editar/{recordatorioId}") { backStackEntry ->
                val recordatorioId = backStackEntry.arguments?.getString("recordatorioId") ?: ""
                EditarRecordatorioScreen(
                    viewModel = viewModel,
                    recordatorioId = recordatorioId,
                    onVolver = { navController.popBackStack() }
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }
}

/* ---------------------------------------------------
            RECORDATORIOS LIST
 ---------------------------------------------------*/

@Composable
fun RecordatoriosListScreen(
    viewModel: RecordatoriosViewModel,
    onAgregarNuevo: () -> Unit,
    onEditarRecordatorio: (String) -> Unit
) {
    val userId = FirebaseDataSource.auth.currentUser!!.uid
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.cargarRecordatorios(userId)
        viewModel.iniciarVerificacionNotificaciones(context, userId)
    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.detenerVerificacionNotificaciones()
        }
    }

    val recordatorios = viewModel.recordatorios

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = stringResource(R.string.cd_recordatorios_fondo),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // CHANGED: stringResource
            Text(
                text = stringResource(R.string.recordatorios_titulo_top),
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = DarkTextPrimary
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            // CHANGED: stringResource logic
            Text(
                text = if (recordatorios.isEmpty())
                    stringResource(R.string.no_hay_recordatorios)
                else
                    stringResource(R.string.cantidad_recordatorios, recordatorios.size),
                fontSize = 16.sp,
                color = DarkTextSecondary,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    // CHANGED: stringResource
                    Text(
                        stringResource(R.string.agregar_nuevo_recordatorio_btn),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = DarkTextPrimary,
                        textAlign = TextAlign.Center
                    )

                    buttonAnimation(
                        drawableId = R.drawable.mas,
                        modifier = Modifier.size(30.dp)
                    ) { onAgregarNuevo() }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                itemsIndexed(recordatorios) { _, recordatorio ->
                    RecordatorioCard(
                        recordatorio = recordatorio,
                        onClick = { onEditarRecordatorio(recordatorio.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun RecordatorioCard(
    recordatorio: com.example.noti_u.data.model.Recordatorios,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp))
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Column {
            Text(
                recordatorio.nombre,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkTextPrimary,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                recordatorio.descripcion,
                fontSize = 14.sp,
                color = DarkTextSecondary,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // CHANGED: stringResource for format
                Text(
                    stringResource(R.string.formato_fecha_simple, recordatorio.fecha),
                    fontSize = 13.sp,
                    color = DarkTextLight,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    stringResource(R.string.formato_hora_simple, recordatorio.hora),
                    fontSize = 13.sp,
                    color = DarkTextLight,
                    fontWeight = FontWeight.Medium
                )
            }
            if (recordatorio.notificar) {
                Spacer(modifier = Modifier.height(4.dp))
                // CHANGED: stringResource
                Text(
                    stringResource(R.string.notificacion_activada),
                    fontSize = 12.sp,
                    color = Color(0xFF1976D2),
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

/* ---------------------------------------------------
        FORMULARIO AGREGAR RECORDATORIO
 ---------------------------------------------------*/

@Composable
fun FormularioRecordatorioScreen(
    viewModel: RecordatoriosViewModel,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var notificar by remember { mutableStateOf(false) }

    var hora by remember { mutableStateOf("--:-- --") }
    var fecha by remember { mutableStateOf("--/--/----") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val userId = FirebaseDataSource.auth.currentUser!!.uid

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = stringResource(R.string.cd_recordatorios_fondo),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.size(50.dp)
                ) { onVolver() }

                // CHANGED: stringResource
                Text(
                    text = stringResource(R.string.agregar_recordatorio_titulo_top),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 17.sp,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.size(50.dp))
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    // CHANGED: stringResource
                    Text(
                        stringResource(R.string.fecha_y_hora_titulo),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = DarkTextPrimary
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Column {
                            // CHANGED: stringResource
                            Text(
                                stringResource(R.string.formato_fecha_simple, fecha),
                                fontSize = 16.sp,
                                color = DarkTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                stringResource(R.string.formato_hora_simple, hora),
                                fontSize = 16.sp,
                                color = DarkTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Row {
                            buttonAnimation(
                                drawableId = R.drawable.calendario,
                                modifier = Modifier.size(20.dp)
                            ) {
                                val datePicker = DatePickerDialog(
                                    context,
                                    { _, y, m, d -> fecha = "$d/${m + 1}/$y" },
                                    calendar.get(Calendar.YEAR),
                                    calendar.get(Calendar.MONTH),
                                    calendar.get(Calendar.DAY_OF_MONTH)
                                )
                                datePicker.show()
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            buttonAnimation(
                                drawableId = R.drawable.reloj,
                                modifier = Modifier.size(20.dp)
                            ) {
                                val timePicker = TimePickerDialog(
                                    context,
                                    { _, h, m ->
                                        val amPm = if (h < 12) "AM" else "PM"
                                        val hour12 = when {
                                            h == 0 -> 12
                                            h > 12 -> h - 12
                                            else -> h
                                        }
                                        hora = String.format("%02d:%02d %s", hour12, m, amPm)
                                    },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    false  // false = 12h format
                                )
                                timePicker.show()
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                // CHANGED: stringResource
                label = { Text(stringResource(R.string.label_nombre_recordatorio)) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = DarkTextPrimary, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                // CHANGED: stringResource
                label = { Text(stringResource(R.string.label_descripcion_recordatorio)) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                textStyle = TextStyle(color = DarkTextSecondary, fontWeight = FontWeight.Medium)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notificar, onCheckedChange = { notificar = it })
                // CHANGED: stringResource
                Text(
                    stringResource(R.string.label_notificar),
                    color = DarkTextSecondary,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && fecha != "--/--/----" && hora != "--:-- --") {
                        viewModel.agregarRecordatorio(
                            context = context,
                            fecha = fecha,
                            hora = hora,
                            nombre = nombre,
                            descripcion = descripcion,
                            notificar = notificar,
                            userId = userId,
                            onSuccess = { onVolver() }
                        )
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                // CHANGED: stringResource
                Text(
                    stringResource(R.string.btn_guardar_recordatorio),
                    color = DarkTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

/* ---------------------------------------------------
        FORMULARIO EDITAR RECORDATORIO
 ---------------------------------------------------*/

@Composable
fun EditarRecordatorioScreen(
    viewModel: RecordatoriosViewModel,
    recordatorioId: String,
    onVolver: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var notificar by remember { mutableStateOf(false) }
    var hora by remember { mutableStateOf("") }
    var fecha by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val userId = FirebaseDataSource.auth.currentUser!!.uid

    // Load data
    LaunchedEffect(recordatorioId) {
        val recordatorio = viewModel.consultarRecordatorio(userId, recordatorioId)
        if (recordatorio != null) {
            nombre = recordatorio.nombre
            descripcion = recordatorio.descripcion
            notificar = recordatorio.notificar
            hora = recordatorio.hora
            fecha = recordatorio.fecha
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = stringResource(R.string.cd_recordatorios_fondo),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = DarkTextPrimary
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    buttonAnimation(
                        drawableId = R.drawable.atras,
                        modifier = Modifier.size(50.dp)
                    ) { onVolver() }

                    // CHANGED: stringResource
                    Text(
                        text = stringResource(R.string.editar_recordatorio_titulo),
                        fontWeight = FontWeight.Bold,
                        fontStyle = FontStyle.Italic,
                        fontSize = 17.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.size(50.dp))
                }

                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 6.dp, bottom = 10.dp),
                    color = Color.Black,
                    thickness = 1.dp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xFF8DD8E1), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    Column {
                        // CHANGED: stringResource
                        Text(
                            stringResource(R.string.fecha_y_hora_titulo),
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = DarkTextPrimary
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {

                            Column {
                                // CHANGED: stringResource
                                Text(
                                    stringResource(R.string.texto_fecha, fecha),
                                    fontSize = 16.sp,
                                    color = DarkTextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                                Text(
                                    stringResource(R.string.texto_hora, hora),
                                    fontSize = 16.sp,
                                    color = DarkTextSecondary,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Row {
                                buttonAnimation(
                                    drawableId = R.drawable.calendario,
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    val datePicker = DatePickerDialog(
                                        context,
                                        { _, y, m, d -> fecha = "$d/${m + 1}/$y" },
                                        calendar.get(Calendar.YEAR),
                                        calendar.get(Calendar.MONTH),
                                        calendar.get(Calendar.DAY_OF_MONTH)
                                    )
                                    datePicker.show()
                                }

                                Spacer(modifier = Modifier.width(8.dp))

                                buttonAnimation(
                                    drawableId = R.drawable.reloj,
                                    modifier = Modifier.size(20.dp)
                                ) {
                                    val timePicker = TimePickerDialog(
                                        context,
                                        { _, h, m ->
                                            val amPm = if (h < 12) "AM" else "PM"
                                            val hour12 = when {
                                                h == 0 -> 12
                                                h > 12 -> h - 12
                                                else -> h
                                            }
                                            hora = String.format("%02d:%02d %s", hour12, m, amPm)
                                        },
                                        calendar.get(Calendar.HOUR_OF_DAY),
                                        calendar.get(Calendar.MINUTE),
                                        false  // false = 12h format
                                    )
                                    timePicker.show()
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    // CHANGED: stringResource
                    label = { Text(stringResource(R.string.label_nombre_recordatorio)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth(),
                    textStyle = TextStyle(color = DarkTextPrimary, fontWeight = FontWeight.Bold)
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    // CHANGED: stringResource
                    label = { Text(stringResource(R.string.label_descripcion_recordatorio)) },
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    textStyle = TextStyle(color = DarkTextSecondary, fontWeight = FontWeight.Medium)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(checked = notificar, onCheckedChange = { notificar = it })
                    // CHANGED: stringResource
                    Text(
                        stringResource(R.string.label_notificar),
                        color = DarkTextSecondary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.eliminarRecordatorio(context, userId, recordatorioId) {
                                onVolver()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        // CHANGED: stringResource
                        Text(
                            stringResource(R.string.eliminar),
                            color = Color.White,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            if (nombre.isNotBlank() && fecha.isNotBlank() && hora.isNotBlank()) {
                                viewModel.editarRecordatorio(
                                    context = context,
                                    recordatorioId = recordatorioId,
                                    fecha = fecha,
                                    hora = hora,
                                    nombre = nombre,
                                    descripcion = descripcion,
                                    notificar = notificar,
                                    userId = userId,
                                    onSuccess = { onVolver() }
                                )
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp)
                    ) {
                        // CHANGED: stringResource
                        Text(
                            stringResource(R.string.btn_guardar_recordatorio),
                            color = DarkTextPrimary,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}