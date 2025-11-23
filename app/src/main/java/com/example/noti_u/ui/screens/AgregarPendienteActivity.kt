package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

class AgregarPendienteActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                AgregarPendienteScreen()
            }
        }
    }

    @Composable
    fun AgregarPendienteScreen() {
        var expanded by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }

        var titulo by remember { mutableStateOf("") }
        var descripcion by remember { mutableStateOf("") }
        var materiaSeleccionada by remember { mutableStateOf("") }

        val materias = listOf(
            Pair("Materia 1", Color(0xFFFDF187)),
            Pair("Materia 2", Color(0xFFD2B6F0)),
            Pair("Materia 3", Color(0xFFB8E2B2)),
            Pair("Materia 4", Color(0xFFD6F0C4)),
            Pair("Materia 5", Color(0xFFF0CCE1)),
            Pair("Materia 6", Color(0xFF8BAE96)),
        )

        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) }
        ) { paddingValues ->
            Box(
                modifier = Modifier.Companion
                    .fillMaxSize()
                    .background(Color(0xFFFAF3E0))
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.Companion
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // 🔹 Permite desplazarse
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
                            modifier = Modifier.Companion.size(80.dp)
                        )

                        Box {
                            buttonAnimation(
                                drawableId = R.drawable.perfil,
                                modifier = Modifier.Companion.size(40.dp)
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
                                        val intent =
                                            Intent(context, EditarPerfilActivity::class.java)
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

                    Divider(
                        modifier = Modifier.Companion
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = Color.Companion.Black,
                        thickness = 1.dp
                    )

                    Row(
                        modifier = Modifier.Companion.fillMaxWidth(),
                        verticalAlignment = Alignment.Companion.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        buttonAnimation(
                            drawableId = R.drawable.atras,
                            modifier = Modifier.Companion.size(32.dp)
                        ) {
                            (context as? ComponentActivity)?.finish()
                        }
                        Spacer(modifier = Modifier.Companion.width(8.dp))
                        Text(
                            text = "Agregar/Editar",
                            fontWeight = FontWeight.Companion.Bold,
                            fontSize = 22.sp,
                            color = Color.Companion.Black
                        )
                    }

                    Spacer(modifier = Modifier.Companion.height(18.dp))

                    Column(modifier = Modifier.Companion.fillMaxWidth()) {
                        Text(
                            text = "Pendiente:",
                            fontWeight = FontWeight.Companion.Medium,
                            fontSize = 18.sp,
                            color = Color.Companion.Black
                        )
                        Spacer(modifier = Modifier.Companion.height(6.dp))
                        OutlinedTextField(
                            value = titulo,
                            onValueChange = { titulo = it },
                            placeholder = { Text("Título", color = Color.Companion.Gray) },
                            modifier = Modifier.Companion
                                .fillMaxWidth()
                                .border(1.dp, Color.Companion.Black, RoundedCornerShape(8.dp)),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Companion.Black)
                        )
                    }
                    Spacer(modifier = Modifier.Companion.height(16.dp))

                    Text(
                        text = "Seleccione la materia:",
                        fontWeight = FontWeight.Companion.Medium,
                        fontSize = 18.sp,
                        color = Color.Companion.Black
                    )
                    Spacer(modifier = Modifier.Companion.height(8.dp))

                    Column(
                        modifier = Modifier.Companion.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        materias.forEach { (nombre, color) ->
                            Row(verticalAlignment = Alignment.Companion.CenterVertically) {
                                RadioButton(
                                    selected = materiaSeleccionada == nombre,
                                    onClick = { materiaSeleccionada = nombre },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = color,
                                        unselectedColor = Color.Companion.Gray
                                    )
                                )
                                Text(nombre, color = Color.Companion.Black)
                                Spacer(modifier = Modifier.Companion.width(6.dp))
                                Box(
                                    modifier = Modifier.Companion
                                        .size(14.dp)
                                        .background(
                                            color,
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(
                                                2.dp
                                            )
                                        )
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.Companion.height(16.dp))

                    Column(modifier = Modifier.Companion.fillMaxWidth()) {
                        Text(
                            text = "Descripción:",
                            fontWeight = FontWeight.Companion.Medium,
                            fontSize = 18.sp,
                            color = Color.Companion.Black
                        )
                        Spacer(modifier = Modifier.Companion.height(6.dp))
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            placeholder = {
                                Text(
                                    "Agrega una descripción",
                                    color = Color.Companion.Gray
                                )
                            },
                            modifier = Modifier.Companion
                                .fillMaxWidth()
                                .height(80.dp)
                                .border(
                                    1.dp,
                                    Color.Companion.Black,
                                    androidx.compose.foundation.shape.RoundedCornerShape(8.dp)
                                ),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Companion.Black)
                        )
                    }

                    Spacer(modifier = Modifier.Companion.height(24.dp))

                    Button(
                        onClick = {
                            if (titulo.isBlank() || materiaSeleccionada.isBlank()) {
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Por favor completa el título y selecciona una materia."
                                    )
                                }
                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar("Pendiente guardado correctamente.")
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5B800)),
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(50),
                        modifier = Modifier.Companion
                            .width(150.dp)
                            .height(45.dp)
                    ) {
                        Text(
                            "Guardar",
                            color = Color.Companion.Black,
                            fontWeight = FontWeight.Companion.Bold
                        )
                    }

                    Spacer(modifier = Modifier.Companion.height(16.dp))
                }
            }
        }
    }
}