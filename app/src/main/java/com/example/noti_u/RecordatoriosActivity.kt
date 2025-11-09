package com.example.noti_u

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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
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
                RecordatoriosListScreen(onAgregarNuevo = { navController.navigate("formulario") })
            }
            composable("formulario") {
                FormularioRecordatorioScreen(onVolver = { navController.popBackStack() })
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
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
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
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                buttonAnimation(
                    drawableId = R.drawable.perfil,
                    modifier = Modifier.size(50.dp)
                ) { /* TODO: acción perfil */ }
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .size(150.dp)
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        "Agregar nuevo recordatorio",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp,
                        color = DarkTextPrimary,
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    buttonAnimation(

                        drawableId = R.drawable.mas,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(top = 5.dp)
                    ) { onAgregarNuevo() }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        "Recordatorio de ejemplo",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = DarkTextPrimary
                    )
                    Text(
                        "Tipo: Estudio",
                        fontSize = 14.sp,
                        color = DarkTextSecondary,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        "10/12/2025 - 15:00",
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
    var hora by remember { mutableStateOf("Seleccionar hora") }
    var fecha by remember { mutableStateOf("Seleccionar fecha") }

    val context = LocalContext.current
    val calendar = Calendar.getInstance()

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
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

                buttonAnimation(
                    drawableId = R.drawable.perfil,
                    modifier = Modifier.size(40.dp)
                ) { /* TODO: acción perfil */ }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
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
                        "Fecha y hora",
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
                                "Fecha: $fecha",
                                fontSize = 16.sp,
                                color = DarkTextSecondary,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                "Hora: $hora",
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
                label = {
                    Text(
                        "Nombre",
                        color = DarkTextLight,
                        fontWeight = FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(color = DarkTextPrimary, fontWeight = FontWeight.Bold)
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = {
                    Text(
                        "Descripción",
                        color = DarkTextLight,
                        fontWeight = FontWeight.Medium
                    )
                },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                textStyle = TextStyle(color = DarkTextSecondary, fontWeight = FontWeight.Medium)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notificar, onCheckedChange = { notificar = it })
                Text("Notificar", color = DarkTextSecondary, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVolver,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text(
                    "Guardar",
                    color = DarkTextPrimary,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
