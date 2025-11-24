package com.example.noti_u.ui.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noti_u.R

import com.example.noti_u.ui.theme.buttonAnimation
import java.util.*

val DarkTextPrimary = Color(0xFF1C1C1C)
val DarkTextSecondary = Color(0xFF2F2F2F)
val DarkTextLight = Color(0xFF4A4A4A)

class RecordatoriosActivity : BaseMenuActivity() {

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "lista",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("lista") {
                RecordatoriosListScreen(
                    onAgregarNuevo = { navController.navigate("formulario") }
                )
            }
            composable("formulario") {
                FormularioRecordatorioScreen(
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

@Composable
fun RecordatoriosListScreen(onAgregarNuevo: () -> Unit) {
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

            Text(
                text = stringResource(R.string.recordatorios_titulo),
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

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        stringResource(R.string.agregar_nuevo_recordatorio),
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

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        stringResource(R.string.recordatorio_ejemplo),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkTextPrimary
                    )
                    Text(
                        stringResource(R.string.tipo_estudio),
                        fontSize = 14.sp,
                        color = DarkTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        stringResource(R.string.recordatorio_1_fecha),
                        fontSize = 13.sp,
                        color = DarkTextLight,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
fun FormularioRecordatorioScreen(onVolver: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var notificar by remember { mutableStateOf(false) }
    val placeholderHora = stringResource(R.string.placeholder_hora)
    val placeholderFecha = stringResource(R.string.placeholder_fecha)

    var hora by remember { mutableStateOf(placeholderHora) }
    var fecha by remember { mutableStateOf(placeholderFecha) }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

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

                Text(
                    text = stringResource(R.string.agregar_recordatorio_titulo),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 17.sp,
                    color = Color.Black
                )
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
                    Text(
                        stringResource(R.string.fecha_y_hora),
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
                            Text(
                                stringResource(R.string.fecha_seleccionada, fecha),
                                fontSize = 16.sp,
                                color = DarkTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                stringResource(R.string.hora_placeholder, hora),
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
                                    { _, h, m -> hora = String.format("%02d:%02d", h, m) },
                                    calendar.get(Calendar.HOUR_OF_DAY),
                                    calendar.get(Calendar.MINUTE),
                                    true
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
                label = { Text(stringResource(R.string.nombre_label)) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = DarkTextPrimary, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text(stringResource(R.string.descripcion_label_2)) },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                textStyle = TextStyle(color = DarkTextSecondary, fontWeight = FontWeight.Medium)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notificar, onCheckedChange = { notificar = it })
                Text(stringResource(R.string.notificar), color = DarkTextSecondary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVolver,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    stringResource(R.string.guardar),
                    color = DarkTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
