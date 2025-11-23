package com.example.noti_u.ui.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel // Importante
import com.example.noti_u.R
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.viewmodel.RegistroViewModel // Importante

class RegistroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF9F6EE)
                ) {
                    RegistroScreen()
                }
            }
        }
    }
}

@Composable
fun RegistroScreen() {
    val context = LocalContext.current
    val viewModel: RegistroViewModel = viewModel()
    val registerState by viewModel.registerState.collectAsState()


    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var institucion by remember { mutableStateOf("") }

    var mensajeError by remember { mutableStateOf("") }


    LaunchedEffect(registerState) {
        registerState?.onSuccess {
            Toast.makeText(context, "Registro exitoso", Toast.LENGTH_SHORT).show()

            (context as? ComponentActivity)?.finish()
        }?.onFailure { exception ->
            mensajeError = exception.message ?: "Error desconocido"
            Toast.makeText(context, "Error: $mensajeError", Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.iniciousuario),
            contentDescription = "Icono de usuario",
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text("Información Personal", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
        Spacer(modifier = Modifier.height(16.dp))

        CustomField("Nombre", nombre) { nombre = it }
        CustomField("Correo", correo, keyboardType = KeyboardType.Email) { correo = it }
        CustomField("Contraseña", contrasena, keyboardType = KeyboardType.Password) { contrasena = it }
        CustomField("Número de Teléfono", telefono, keyboardType = KeyboardType.Phone) { telefono = it }

        Spacer(modifier = Modifier.height(24.dp))
        Text("Información Académica", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121))
        Spacer(modifier = Modifier.height(16.dp))

        CustomField("Área de estudio", area) { area = it }
        CustomField("Institución educativa", institucion) { institucion = it }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                // Validaciones simples en UI
                when {
                    contrasena.length < 6 -> mensajeError = "La contraseña debe tener al menos 6 caracteres"
                    else -> {
                        mensajeError = ""

                        viewModel.register(
                            nombre = nombre,
                            correo = correo.trim(),
                            telefono = telefono,
                            area = area,
                            institucion = institucion,
                            password = contrasena.trim()
                        )
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
        ) {
            Text("Registrar", color = Color(0xFF212121), fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        if (mensajeError.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(mensajeError, color = Color.Red, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun CustomField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onChange: (String) -> Unit
) {
    val textColor = Color(0xFF212121)
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, color = textColor, fontSize = 16.sp, fontWeight = FontWeight.Medium, modifier = Modifier.padding(bottom = 4.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            textStyle = LocalTextStyle.current.copy(color = textColor),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f),
                cursorColor = textColor
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}