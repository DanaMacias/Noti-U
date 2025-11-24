package com.example.noti_u.ui.screens

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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import kotlinx.coroutines.launch

class AgregarPendienteActivity :  BaseLanguageActivity() {

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

        // Strings que sí pueden llamarse aquí
        val errorTituloMateria = stringResource(R.string.error_titulo_materia)
        val pendienteGuardado = stringResource(R.string.pendiente_guardado)

        val materias = listOf(
            Pair(stringResource(R.string.materia1), Color(0xFFFDF187)),
            Pair(stringResource(R.string.materia2), Color(0xFFD2B6F0)),
            Pair(stringResource(R.string.materia3), Color(0xFFB8E2B2)),
            Pair(stringResource(R.string.materia4), Color(0xFFD6F0C4)),
            Pair(stringResource(R.string.materia5), Color(0xFFF0CCE1)),
            Pair(stringResource(R.string.materia6), Color(0xFF8BAE96)),
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
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    // ---------------------- HEADER ------------------------
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
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
                                onDismissRequest = { expanded = false },
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

                    Divider(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        color = Color.Black,
                        thickness = 1.dp
                    )

                    // ---------------------- TÍTULO ------------------------
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
                            text = stringResource(R.string.agregar_editar),
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            color = Color.Black
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    // ------------------ CAMPO TÍTULO ---------------------
                    Column(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = stringResource(R.string.pendiente_label),
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = titulo,
                            onValueChange = { titulo = it },
                            placeholder = {
                                Text(stringResource(R.string.titulo_placeholder), color = Color.Gray)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ---------------------- MATERIAS ------------------------
                    Text(
                        text = stringResource(R.string.seleccione_materia),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp
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
                                        .background(color, RoundedCornerShape(2.dp))
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // ------------------ DESCRIPCIÓN ---------------------
                    Column(modifier = Modifier.fillMaxWidth()) {

                        Text(
                            text = stringResource(R.string.descripcion_label),
                            fontWeight = FontWeight.Medium,
                            fontSize = 18.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        OutlinedTextField(
                            value = descripcion,
                            onValueChange = { descripcion = it },
                            placeholder = {
                                Text(stringResource(R.string.descripcion_placeholder), color = Color.Gray)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(80.dp)
                                .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                            textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    // ------------------- BOTÓN GUARDAR --------------------
                    Button(
                        onClick = {
                            if (titulo.isBlank() || materiaSeleccionada.isBlank()) {

                                scope.launch {
                                    snackbarHostState.showSnackbar(errorTituloMateria)
                                }

                            } else {
                                scope.launch {
                                    snackbarHostState.showSnackbar(pendienteGuardado)
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5B800)),
                        shape = RoundedCornerShape(50),
                        modifier = Modifier
                            .width(150.dp)
                            .height(45.dp)
                    ) {

                        Text(
                            stringResource(R.string.guardar),
                            color = Color.Black,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
