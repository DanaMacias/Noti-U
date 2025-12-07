package com.example.noti_u.ui.screens

import android.app.DatePickerDialog
import androidx.activity.ComponentActivity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.viewmodel.PerfilViewModel
import java.util.Calendar

class EditarPerfilActivity : BaseLanguageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                EditarPerfilScreen()
            }
        }
    }
}

@Composable
fun EditarPerfilScreen(
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val user = viewModel.userData.collectAsState().value

    if (user == null) {
        Text("Cargando...")
        return
    }

    var nombre by remember { mutableStateOf(user.nombre) }
    var correo by remember { mutableStateOf(user.correo) }
    var telefono by remember { mutableStateOf(user.telefono) }
    var area by remember { mutableStateOf(user.area) }
    var institucion by remember { mutableStateOf(user.institucion) }

    var periodo by remember { mutableStateOf(user.periodo) }
    var fechaInicio by remember { mutableStateOf(user.fechaInicio) }
    var fechaFin by remember { mutableStateOf(user.fechaFin) }
    var duracion by remember { mutableStateOf(user.duracion) }

    fun calcularDuracion() {
        if (fechaInicio.isNotEmpty() && fechaFin.isNotEmpty()) {
            duracion = "Desde $fechaInicio hasta $fechaFin"
        }
    }

    fun abrirDatePicker(onDateSelected: (String) -> Unit) {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            context,
            { _, y, m, d -> onDateSelected("$d/${m + 1}/$y") },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {

        Text("Editar Perfil", fontSize = 28.sp, fontWeight = FontWeight.Bold)

        Spacer(Modifier.height(20.dp))

        // === CAMPOS BÁSICOS ===
        CampoEditable("Nombre", nombre) { nombre = it }
        CampoEditable("Correo", correo) { correo = it }
        CampoEditable("Teléfono", telefono) { telefono = it }
        CampoEditable("Área", area) { area = it }
        CampoEditable("Institución", institucion) { institucion = it }

        Divider()
        Text("Periodo Académico", fontWeight = FontWeight.Bold, fontSize = 18.sp)

        CampoEditable("Periodo (ej. 2025-1)", periodo) { periodo = it }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fecha inicio: $fechaInicio")
            Button(onClick = {
                abrirDatePicker {
                    fechaInicio = it
                    calcularDuracion()
                }
            }) { Text("Elegir") }
        }


        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Fecha fin: $fechaFin")
            Button(onClick = {
                abrirDatePicker {
                    fechaFin = it
                    calcularDuracion()
                }
            }) { Text("Elegir") }
        }

        Spacer(Modifier.height(8.dp))
        Text("Duración: $duracion", fontWeight = FontWeight.Medium)

        Spacer(Modifier.height(25.dp))


        Button(
            onClick = {
                val updatedUser = user.copy(
                    nombre = nombre,
                    correo = correo,
                    telefono = telefono,
                    area = area,
                    institucion = institucion,
                    periodo = periodo,
                    fechaInicio = fechaInicio,
                    fechaFin = fechaFin,
                    duracion = duracion
                )

                viewModel.updateUser(updatedUser) { success ->
                    if (success) (context as? ComponentActivity)?.finish()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(Color(0xFFFFA726))
        ) {
            Text("Guardar", color = Color.Black, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun CampoEditable(titulo: String, valor: String, onChange: (String) -> Unit) {
    Text(titulo, fontWeight = FontWeight.Medium)
    BasicTextField(
        value = valor,
        onValueChange = onChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(16.dp))
            .padding(12.dp)
    )
    Spacer(Modifier.height(10.dp))
}
