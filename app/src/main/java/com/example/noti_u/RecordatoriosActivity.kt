package com.example.noti_u

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noti_u.ui.theme.NotiUTheme
import java.util.*

class RecordatoriosActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    RecordatoriosNavHost()
                }
            }
        }
    }
}

@Composable
fun RecordatoriosNavHost() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "lista") {
        composable("lista") {
            RecordatoriosListScreen(onAgregarNuevo = { navController.navigate("formulario") })
        }
        composable("formulario") {
            FormularioRecordatorioScreen(onVolver = { navController.popBackStack() })
        }
    }
}

@Composable
fun RecordatoriosListScreen(onAgregarNuevo: () -> Unit) {
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
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(140.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp))
                    .clickable { onAgregarNuevo() },
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Agregar nuevo recordatorio", fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.agregar),
                        contentDescription = "Agregar",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Ejemplo de recordatorio
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text("Recordatorio de ejemplo", fontWeight = FontWeight.Bold)
                    Text("Tipo: Estudio", fontSize = 13.sp)
                    Text("10/12/2025 - 15:00", fontSize = 12.sp)
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
            // Botón volver
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.atras),
                    contentDescription = "Volver",
                    modifier = Modifier
                        .size(40.dp)
                        .clickable { onVolver() }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Bloque combinado de fecha y hora
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(16.dp))
                    .padding(16.dp)
            ) {
                Column {
                    Text(
                        "Fecha y hora",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Fecha: $fecha", fontSize = 16.sp, color = Color.Black)
                            Text("Hora: $hora", fontSize = 16.sp, color = Color.Black)
                        }

                        Row {
                            Image(
                                painter = painterResource(id = R.drawable.calendario),
                                contentDescription = "Seleccionar fecha",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        val datePicker = DatePickerDialog(
                                            context,
                                            { _, y, m, d -> fecha = "$d/${m + 1}/$y" },
                                            calendar.get(Calendar.YEAR),
                                            calendar.get(Calendar.MONTH),
                                            calendar.get(Calendar.DAY_OF_MONTH)
                                        )
                                        datePicker.show()
                                    }
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            Image(
                                painter = painterResource(id = R.drawable.calendario), //cambiar icono por un reloj
                                contentDescription = "Seleccionar hora",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clickable {
                                        val timePicker = TimePickerDialog(
                                            context,
                                            { _, h, m -> hora = String.format("%02d:%02d", h, m) },
                                            calendar.get(Calendar.HOUR_OF_DAY),
                                            calendar.get(Calendar.MINUTE),
                                            true
                                        )
                                        timePicker.show()
                                    }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(
                color = Color.Gray,
                thickness = 1.dp,
                modifier = Modifier.padding(vertical = 8.dp)
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = notificar, onCheckedChange = { notificar = it })
                Text("Notificar")
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onVolver,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(20.dp)
            ) {
                Text("Guardar", color = Color.Black)
            }
        }
    }
}
