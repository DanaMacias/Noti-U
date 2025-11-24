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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.R
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.viewmodel.RegistroViewModel

class RegistroActivity :  BaseLanguageActivity() {
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
    val registroExitosoMsg = stringResource(R.string.registro_exitoso)
    val errorDesconocidoMsg = stringResource(R.string.error_desconocido)
    val errorContrasenaCortaMsg = stringResource(R.string.error_contrasena_corta)

    LaunchedEffect(registerState) {
        registerState?.onSuccess {
            Toast.makeText(context, registroExitosoMsg, Toast.LENGTH_SHORT)
                .show()
            (context as? ComponentActivity)?.finish()
        }?.onFailure { exception ->
            mensajeError = exception.message ?: errorDesconocidoMsg
            Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        Image(
            painter = painterResource(id = R.drawable.iniciousuario),
            contentDescription = stringResource(R.string.icono_usuario),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.titulo_info_personal),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF212121)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomField(stringResource(R.string.label_nombre), nombre) { nombre = it }
        CustomField(stringResource(R.string.label_correo), correo, KeyboardType.Email) { correo = it }
        CustomField(stringResource(R.string.label_contrasena), contrasena, KeyboardType.Password) { contrasena = it }
        CustomField(stringResource(R.string.label_telefono), telefono, KeyboardType.Phone) { telefono = it }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            stringResource(R.string.titulo_info_academica),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = Color(0xFF212121)
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomField(stringResource(R.string.label_area), area) { area = it }
        CustomField(stringResource(R.string.label_institucion), institucion) { institucion = it }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = {
                when {
                    contrasena.length < 6 ->
                        mensajeError = errorContrasenaCortaMsg

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
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                stringResource(R.string.boton_registrar),
                color = Color(0xFF212121),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (mensajeError.isNotBlank()) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(mensajeError, color = Color.Red, fontSize = 14.sp)
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
        Text(
            text = label,
            color = textColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
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
