package com.example.noti_u.ui.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import java.util.*

class NuevaMateriaActivity :  BaseLanguageActivity() {
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

    val dias = listOf(
        stringResource(R.string.lunes),
        stringResource(R.string.martes),
        stringResource(R.string.miercoles),
        stringResource(R.string.jueves),
        stringResource(R.string.viernes),
        stringResource(R.string.sabado),
        stringResource(R.string.domingo)
    )

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
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.logo),
                    modifier = Modifier.size(80.dp)
                )

                Box {
                    buttonAnimation(
                        drawableId = R.drawable.perfil,
                        modifier = Modifier.size(40.dp)
                    ) { expanded = !expanded }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.ver_perfil)) },
                            onClick = {
                                expanded = false
                                context.startActivity(Intent(context, PerfilActivity::class.java))
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.editar_perfil)) },
                            onClick = {
                                expanded = false
                                context.startActivity(Intent(context, EditarPerfilActivity::class.java))
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.cerrar_sesion)) },
                            onClick = {
                                expanded = false
                                context.startActivity(Intent(context, MainActivity::class.java))
                                (context as? ComponentActivity)?.finish()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Divider(color = Color.Black, thickness = 1.dp)

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.size(32.dp)
                ) { (context as? ComponentActivity)?.finish() }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.agregar_editar_materia),
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.nombre_materia),
                fontWeight = FontWeight.Bold
            )

            OutlinedTextField(
                value = nombre,
                onValueChange = { nombre = it },
                placeholder = { Text(stringResource(R.string.placeholder_nombre_materia)) },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = stringResource(R.string.seleccione_horario),
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            dias.forEach { dia ->
                var checked by remember { mutableStateOf(false) }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Checkbox(
                        checked = checked,
                        onCheckedChange = {
                            checked = it
                            diasSeleccionados[dia] = it
                        }
                    )

                    Text(text = dia, modifier = Modifier.weight(1f))

                    if (checked) {
                        Button(
                            onClick = {
                                val cal = Calendar.getInstance()
                                TimePickerDialog(
                                    context,
                                    { _, hora, minuto ->
                                        horaInicio[dia] = "%02d:%02d".format(hora, minuto)
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            }
                        ) { Text(horaInicio[dia] ?: stringResource(R.string.hora_inicio)) }

                        Spacer(modifier = Modifier.width(4.dp))

                        Button(
                            onClick = {
                                val cal = Calendar.getInstance()
                                TimePickerDialog(
                                    context,
                                    { _, hora, minuto ->
                                        horaFin[dia] = "%02d:%02d".format(hora, minuto)
                                    },
                                    cal.get(Calendar.HOUR_OF_DAY),
                                    cal.get(Calendar.MINUTE),
                                    true
                                ).show()
                            }
                        ) { Text(horaFin[dia] ?: stringResource(R.string.hora_fin)) }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = stringResource(R.string.color_opcional),
                fontStyle = FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row {
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

            Button(
                onClick = { (context as? ComponentActivity)?.finish() },
                modifier = Modifier.fillMaxWidth(0.6f)
            ) {
                Text(stringResource(R.string.guardar))
            }
        }
    }
}
