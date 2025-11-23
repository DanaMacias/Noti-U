package com.example.noti_u.ui.screens

import android.app.DatePickerDialog
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.R
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import java.util.Calendar
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class EditarPerfilActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                EditarPerfilActivityUI()
            }
        }
    }

    @Composable
    fun EditarPerfilActivityUI() {
        val context = LocalContext.current
        val calendar = Calendar.getInstance()

        var nombre by remember { mutableStateOf(TextFieldValue("")) }
        var correo by remember { mutableStateOf(TextFieldValue("")) }
        var periodo by remember { mutableStateOf(TextFieldValue("")) }
        var fecha by remember { mutableStateOf("") }

        Column(
            modifier = Modifier.Companion
                .fillMaxSize()
                .background(Color(0xFFFAF4E1))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.Companion.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.Companion.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.Companion.height(8.dp))

            Row(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.Companion.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                val context = LocalContext.current

                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.Companion.size(36.dp)
                ) {
                    (context as? ComponentActivity)?.finish()
                }

                Spacer(modifier = Modifier.Companion.width(12.dp))

                Text(
                    text = "Editar Perfil",
                    fontWeight = FontWeight.Companion.Bold,
                    fontStyle = FontStyle.Companion.Italic,
                    fontSize = 30.sp,
                    color = Color.Companion.Black
                )
            }


            Divider(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                color = Color.Companion.Black,
                thickness = 1.dp
            )

            Text(
                text = "Datos de usuario",
                fontWeight = FontWeight.Companion.Bold,
                fontSize = 16.sp,
                color = Color.Companion.Black,
                modifier = Modifier.Companion.align(Alignment.Companion.Start)
            )

            Spacer(modifier = Modifier.Companion.height(8.dp))

            Text("Nombre:", fontWeight = FontWeight.Companion.Medium, color = Color.Companion.Black)
            BasicTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .background(Color.Companion.White, RoundedCornerShape(20.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.Companion.height(10.dp))

            Text(
                "Correo Electrónico:",
                fontWeight = FontWeight.Companion.Medium,
                color = Color.Companion.Black
            )
            BasicTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .background(
                        Color.Companion.White,
                        androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                    )
                    .padding(12.dp)
            )

            Divider(
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = Color.Companion.Black,
                thickness = 1.dp
            )

            // --- Periodo Académico ---
            Text(
                text = "Periodo Académico",
                fontWeight = FontWeight.Companion.Bold,
                fontSize = 16.sp,
                color = Color.Companion.Black,
                modifier = Modifier.Companion.align(Alignment.Companion.Start)
            )

            Spacer(modifier = Modifier.Companion.height(8.dp))

            Text(
                "Año o periodo:",
                fontWeight = FontWeight.Companion.Medium,
                color = Color.Companion.Black
            )
            BasicTextField(
                value = periodo,
                onValueChange = { periodo = it },
                modifier = Modifier.Companion
                    .fillMaxWidth()
                    .background(
                        Color.Companion.White,
                        androidx.compose.foundation.shape.RoundedCornerShape(20.dp)
                    )
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.Companion.height(15.dp))

            // --- Selección de fecha ---
            Row(
                modifier = Modifier.Companion.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Companion.CenterVertically
            ) {
                Text(
                    "Selecciona la fecha:",
                    fontWeight = FontWeight.Companion.Medium,
                    color = Color.Companion.Black
                )
                buttonAnimation(
                    drawableId = R.drawable.calendario,
                    modifier = Modifier.Companion.size(36.dp)
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
            }

            Spacer(modifier = Modifier.Companion.height(8.dp))
            if (fecha.isNotEmpty()) {
                Text("Fecha seleccionada: $fecha", color = Color.Companion.Black)
            }

            Spacer(modifier = Modifier.Companion.height(25.dp))

            // --- Botón Guardar ---
            Button(
                onClick = { /* guardar cambios */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                shape = androidx.compose.foundation.shape.RoundedCornerShape(20.dp),
                modifier = Modifier.Companion
                    .fillMaxWidth(0.5f)
                    .height(45.dp)
            ) {
                Text(
                    "Guardar",
                    color = Color.Companion.Black,
                    fontWeight = FontWeight.Companion.Bold
                )
            }
        }
    }
}