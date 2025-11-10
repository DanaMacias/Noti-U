package com.example.noti_u

import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import java.util.*

class NuevaMateriaActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                NuevaMateriaScreen()
            }
        }
    }
}

@Composable
fun NuevaMateriaScreen() {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var nombre by remember { mutableStateOf("") }

    val dias = listOf("Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo")
    val colores = listOf(
        Color(0xFFFFF176),
        Color(0xFFFF8A65),
        Color(0xFFFFB300),
        Color(0xFFD1C4E9),
        Color(0xFF80DEEA),
        Color(0xFFA5D6A7),
        Color(0xFF4DB6AC)
    )

    var colorSeleccionado by remember { mutableStateOf<Color?>(null) }
    val diasSeleccionados = remember { mutableStateMapOf<String, Boolean>() }
    val horaInicio = remember { mutableStateMapOf<String, String>() }
    val horaFin = remember { mutableStateMapOf<String, String>() }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),

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
                    modifier = Modifier.size(80.dp)
                )

                Box {
                    buttonAnimation(
                        drawableId = R.drawable.perfil,
                        modifier = Modifier.size(40.dp)
                    ) { expanded = !expanded }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {
                        DropdownMenuItem(
                            text = { Text("Ver perfil") },
                            onClick = {
                                expanded = false
                                val intent = Intent(context, PerfilActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Editar perfil") },
                            onClick = {
                                expanded = false
                                val intent = Intent(context, EditarPerfilActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text("Cerrar sesión") },
                            onClick = {
                                expanded = false
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.finish()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(
                modifier = Modifier.fillMaxWidth(),
                color = Color.Black,
                thickness = 1.dp
            )


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.size(32.dp)
                ) {
                    (context as? ComponentActivity)?.finish()
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = "Agregar / Editar Materia",
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🔹 Campo de nombre
            Text(
                text = "Nombre:",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text("Ingrese nombre de la materia", color = Color.Gray) },
                textStyle = LocalTextStyle.current.copy(color = Color.Black),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = Color.Gray,
                    focusedBorderColor = Color(0xFFBBDEFB)
                )
            )

            Spacer(modifier = Modifier.height(12.dp))

            // 🔹 Horario
            Text(
                text = "Seleccione el horario:",
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            dias.forEach { dia ->
                var checked by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            diasSeleccionados[dia] = it
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = Color(0xFFBBDEFB),
                            uncheckedColor = Color.DarkGray
                        )
                    )

                    Text(
                        text = dia,
                        modifier = Modifier.weight(1f),
                        fontSize = 16.sp
                    )

                    if (checked) {
                        Button(
                            onClick = {
                                val cal = Calendar.getInstance()
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        horaInicio[dia] = String.format("%02d:%02d", hour, minute)
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB)),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                text = horaInicio[dia] ?: "Inicio",
                                color = Color.Black,
                                fontSize = 13.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                val cal = Calendar.getInstance()
                                TimePickerDialog(
                                    context,
                                    { _, hour, minute ->
                                        horaFin[dia] = String.format("%02d:%02d", hour, minute)
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFBBDEFB)),
                            shape = RoundedCornerShape(50.dp),
                            modifier = Modifier.height(40.dp)
                        ) {
                            Text(
                                text = horaFin[dia] ?: "Fin",
                                color = Color.Black,
                                fontSize = 13.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Colores
            Text(
                text = "Color (Opcional):",
                fontStyle = FontStyle.Italic,
                fontSize = 16.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                colores.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .background(color, CircleShape)
                            .border(
                                width = if (color == colorSeleccionado) 3.dp else 0.dp,
                                color = if (color == colorSeleccionado) Color.Black else Color.Transparent,
                                shape = CircleShape
                            )
                            .clickable { colorSeleccionado = color }
                    )
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 🔹 Botón Guardar
            Button(
                onClick = { (context as? ComponentActivity)?.finish() },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth(0.6f)
                    .height(50.dp)
            ) {
                Text(
                    "Guardar",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 16.sp
                )
            }
        }
    }
}
