package com.example.noti_u

import android.app.DatePickerDialog
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation

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
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF4E1))
                .padding(horizontal = 20.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(120.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                val context = LocalContext.current

                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.size(36.dp)
                ) {
                    (context as? ComponentActivity)?.finish()
                }

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "Editar Perfil",
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp,
                    color = Color.Black
                )
            }


            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            Text(
                text = "Datos de usuario",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Nombre:", fontWeight = FontWeight.Medium, color = Color.Black)
            BasicTextField(
                value = nombre,
                onValueChange = { nombre = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text("Correo Electrónico:", fontWeight = FontWeight.Medium, color = Color.Black)
            BasicTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(12.dp)
            )

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp),
                color = Color.Black,
                thickness = 1.dp
            )

            // --- Periodo Académico ---
            Text(
                text = "Periodo Académico",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Año o periodo:", fontWeight = FontWeight.Medium, color = Color.Black)
            BasicTextField(
                value = periodo,
                onValueChange = { periodo = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(20.dp))
                    .padding(12.dp)
            )

            Spacer(modifier = Modifier.height(15.dp))

            // --- Selección de fecha ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Selecciona la fecha:", fontWeight = FontWeight.Medium, color = Color.Black)
                buttonAnimation(
                    drawableId = R.drawable.calendario,
                    modifier = Modifier.size(36.dp)
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

            Spacer(modifier = Modifier.height(8.dp))
            if (fecha.isNotEmpty()) {
                Text("Fecha seleccionada: $fecha", color = Color.Black)
            }

            Spacer(modifier = Modifier.height(25.dp))

            // --- Botón Guardar ---
            Button(
                onClick = { /* guardar cambios */ },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(45.dp)
            ) {
                Text("Guardar", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}