package com.example.noti_u

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import kotlinx.coroutines.launch

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
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFFAF3E0))
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()), // 🔹 Permite desplazarse
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

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
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
                            text = "Agregar/Editar",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Pendiente:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = titulo,
                            onValueChange = { titulo = it },
                            placeholder = { Text("Título", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Seleccione la materia:",
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        materias.forEach { (nombre, color) ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                RadioButton(
                                    selected = materiaSeleccionada == nombre,
                                    onClick = { materiaSeleccionada = nombre },
                                    colors = RadioButtonDefaults.colors(
                                        selectedColor = color,
                                        unselectedColor = Color.Gray
                                    )
                                )
                                Text(nombre, color = Color.Black)
                                Spacer(modifier = Modifier.width(6.dp))
                                Box(
                                    modifier = Modifier
                                        .size(14.dp)
                                        .background(color, shape = RoundedCornerShape(2.dp))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "Descripción:",
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            placeholder = { Text("Agrega una descripción", color = Color.Gray) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

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
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .width(150.dp)
                            .height(45.dp)
                    ) {
                        Text("Guardar", color = Color.Black, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
