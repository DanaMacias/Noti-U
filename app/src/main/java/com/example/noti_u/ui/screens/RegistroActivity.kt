package com.example.noti_u.ui.screens

import android.app.DatePickerDialog
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
import com.example.noti_u.data.model.User
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.viewmodel.RegistroViewModel
import java.util.Calendar

class RegistroActivity : BaseLanguageActivity() {
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

    // --- ESTADOS DE DATOS ---
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var institucion by remember { mutableStateOf("") }

    // Nuevos campos académicos
    var periodo by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }
    var duracion by remember { mutableStateOf("") }

    // Estado para la Alerta
    var mostrarAlertaPeriodo by remember { mutableStateOf(false) }
    var mensajeError by remember { mutableStateOf("") }

    // --- HELPERS PARA FECHAS ---
    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    fun calcularDuracion() {
        if (fechaInicio.isNotEmpty() && fechaFin.isNotEmpty()) {
            val formato = context.getString(R.string.formato_duracion)
            duracion = String.format(formato, fechaInicio, fechaFin)
        }
    }

    val datePickerDialogInicio = DatePickerDialog(context, { _, y, m, d ->
        fechaInicio = "$d/${m + 1}/$y"
        calcularDuracion()
    }, year, month, day)

    val datePickerDialogFin = DatePickerDialog(context, { _, y, m, d ->
        fechaFin = "$d/${m + 1}/$y"
        calcularDuracion()
    }, year, month, day)

    // --- LÓGICA DE REGISTRO ---
    fun realizarRegistro() {
        // Creamos el objeto User con TODOS los campos
        val user = User(
            nombre = nombre,
            correo = correo.trim(),
            contrasena = "", // No guardamos la contraseña en texto plano en la BD por seguridad
            telefono = telefono,
            area = area,
            institucion = institucion,
            periodo = periodo,
            fechaInicio = fechaInicio,
            fechaFin = fechaFin,
            duracion = duracion
        )
        // CAMBIO: Llamamos al método actualizado del ViewModel
        viewModel.register(correo.trim(), contrasena.trim(), user)
    }

    // --- MANEJO DE RESPUESTA ---
    val registroExitosoMsg = stringResource(R.string.registro_exitoso)
    val errorDesconocidoMsg = stringResource(R.string.error_desconocido)

    LaunchedEffect(registerState) {
        registerState?.onSuccess {
            Toast.makeText(context, registroExitosoMsg, Toast.LENGTH_SHORT).show()
            (context as? ComponentActivity)?.finish()
        }?.onFailure { exception ->
            mensajeError = exception.message ?: errorDesconocidoMsg
            Toast.makeText(context, mensajeError, Toast.LENGTH_LONG).show()
        }
    }

    // --- DIÁLOGO DE ADVERTENCIA ---
    if (mostrarAlertaPeriodo) {
        AlertDialog(
            onDismissRequest = { mostrarAlertaPeriodo = false },
            title = { Text(stringResource(R.string.atencion_titulo)) },
            text = { Text(stringResource(R.string.advertencia_periodo_vacio)) },
            confirmButton = {
                Button(
                    onClick = {
                        mostrarAlertaPeriodo = false
                        realizarRegistro() // El usuario decidió continuar sin fechas
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300))
                ) {
                    Text(stringResource(R.string.continuar), color = Color.Black)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { mostrarAlertaPeriodo = false }) {
                    Text(stringResource(R.string.cancelar))
                }
            }
        )
    }

    // --- UI PRINCIPAL ---
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(20.dp))

        Image(
            painter = painterResource(id = R.drawable.iniciousuario),
            contentDescription = stringResource(R.string.cd_icono_usuario),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // 1. INFORMACIÓN PERSONAL
        Text(
            stringResource(R.string.titulo_info_personal),
            fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121)
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomField(stringResource(R.string.label_nombre), nombre) { nombre = it }
        CustomField(stringResource(R.string.label_correo), correo, KeyboardType.Email) { correo = it }
        CustomField(stringResource(R.string.label_contrasena), contrasena, KeyboardType.Password) { contrasena = it }
        CustomField(stringResource(R.string.label_telefono), telefono, KeyboardType.Phone) { telefono = it }

        Spacer(modifier = Modifier.height(24.dp))

        // 2. INFORMACIÓN ACADÉMICA
        Text(
            stringResource(R.string.titulo_info_academica),
            fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF212121)
        )
        Spacer(modifier = Modifier.height(16.dp))

        CustomField(stringResource(R.string.label_area), area) { area = it }
        CustomField(stringResource(R.string.label_institucion), institucion) { institucion = it }

        Spacer(modifier = Modifier.height(16.dp))

        // 3. PERIODO ACADÉMICO (NUEVO)
        Text(
            stringResource(R.string.periodo_academico),
            fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color(0xFF212121)
        )
        Spacer(modifier = Modifier.height(8.dp))

        CustomField(stringResource(R.string.periodo_ejemplo), periodo) { periodo = it }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedButton(
                onClick = { datePickerDialogInicio.show() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (fechaInicio.isEmpty()) stringResource(R.string.fecha_inicio) else fechaInicio, fontSize = 12.sp)
            }

            OutlinedButton(
                onClick = { datePickerDialogFin.show() },
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(if (fechaFin.isEmpty()) stringResource(R.string.fecha_fin) else fechaFin, fontSize = 12.sp)
            }
        }

        if(duracion.isNotEmpty()){
            Text(text = duracion, fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(top = 4.dp))
        }

        Spacer(modifier = Modifier.height(32.dp))

        // --- BOTÓN REGISTRAR ---
        Button(
            onClick = {
                when {
                    nombre.isBlank() || correo.isBlank() || contrasena.isBlank() ->
                        mensajeError = context.getString(R.string.error_campos_vacios)

                    contrasena.length < 6 ->
                        mensajeError = context.getString(R.string.error_contrasena_corta)

                    else -> {
                        mensajeError = ""
                        val periodoIncompleto = periodo.isBlank() || fechaInicio.isBlank() || fechaFin.isBlank()

                        if (periodoIncompleto) {
                            mostrarAlertaPeriodo = true
                        } else {
                            realizarRegistro()
                        }
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier.fillMaxWidth().height(50.dp)
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
    // CAMBIO 1: Usamos negro absoluto en lugar de gris oscuro
    val textColor = Color.Black

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = textColor, // El título ahora será negro puro
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold, // Aumenté un poco el peso para que resalte más
            modifier = Modifier.padding(bottom = 4.dp)
        )

        OutlinedTextField(
            value = value,
            onValueChange = onChange,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            colors = OutlinedTextFieldDefaults.colors(
                // CAMBIO 2: Configuramos explícitamente el color del texto interno
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,

                // Colores de los bordes y cursor
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor, // Quité la transparencia para que el borde se vea más negro también
                cursorColor = textColor,

                // Opcional: Asegura que el fondo no afecte el contraste
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent
            ),
            singleLine = true
        )
        Spacer(modifier = Modifier.height(12.dp))
    }

}