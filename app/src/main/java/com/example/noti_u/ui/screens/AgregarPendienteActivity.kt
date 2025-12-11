package com.example.noti_u.ui.screens

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
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
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.data.model.Materia
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import com.example.noti_u.ui.viewmodel.AgregarPendienteViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class AgregarPendienteActivity : BaseLanguageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val pendienteId = intent.getStringExtra("pendiente_id") // Recibimos el ID
        setContent {
            NotiUTheme {
                AgregarPendienteScreen(pendienteId = pendienteId)
            }
        }
    }
}

@Composable
fun AgregarPendienteScreen(
    viewModel: AgregarPendienteViewModel = viewModel(),
    pendienteId: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // Estados del formulario
    var titulo by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var materiaSeleccionadaId by remember { mutableStateOf("") }
    var fechaSeleccionada by remember { mutableStateOf("") }

    // Lógica para cargar datos si es EDICIÓN
    LaunchedEffect(pendienteId) {
        if (pendienteId != null) {
            // NOTA: Asumo que en tu ViewModel implementarás una función similar a 'obtenerPendientePorId'
            // Si no la tienes, deberás crearla. Aquí muestro cómo se usaría:
            viewModel.obtenerPendientePorId(pendienteId) { pendiente ->
                if (pendiente != null) {
                    titulo = pendiente.titulo
                    descripcion = pendiente.descripcion
                    materiaSeleccionadaId = pendiente.materiaIdMateria
                    fechaSeleccionada = pendiente.fecha
                }
            }
        }
    }

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(
        context,
        { _: DatePicker, y: Int, m: Int, d: Int ->
            fechaSeleccionada = "$d/${m + 1}/$y"
        },
        year, month, day
    )

    val guardado by viewModel.guardadoExitoso.collectAsState()
    val materias by viewModel.materias.collectAsState()

    val errorMsg = stringResource(R.string.error_titulo_materia)
    val successMsg = stringResource(R.string.pendiente_guardado)

    LaunchedEffect(guardado) {
        if (guardado) {
            (context as? ComponentActivity)?.finish()
            viewModel.reiniciarEstado()
        }
    }

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

                // --- HEADER ---
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = stringResource(R.string.cd_logo),
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
                                    val intent = Intent(context, MainActivity::class.java)
                                    context.startActivity(intent)
                                    (context as? ComponentActivity)?.finish()
                                }
                            )
                        }
                    }
                }
                Divider(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp))

                // --- TITLE ROW ---
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { (context as? ComponentActivity)?.finish() }) {
                        Image(
                            painter = painterResource(id = R.drawable.atras),
                            contentDescription = stringResource(R.string.cd_volver),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (pendienteId == null) stringResource(R.string.agregar_pendiente) else stringResource(R.string.editar_pendiente),
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp,
                        color = Color.Black
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // --- INPUT TITULO ---
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.pendiente_label),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = titulo,
                        onValueChange = { titulo = it },
                        placeholder = { Text(stringResource(R.string.titulo_placeholder), color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- MATERIAS ---
                Text(
                    text = stringResource(R.string.seleccione_materia),
                    fontWeight = FontWeight.Medium,
                    fontSize = 18.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    materias.forEach { materia ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.clickable { materiaSeleccionadaId = materia.id }
                        ) {
                            RadioButton(
                                selected = materiaSeleccionadaId == materia.id,
                                onClick = { materiaSeleccionadaId = materia.id },
                                colors = RadioButtonDefaults.colors(
                                    selectedColor = Color.Black, // Radio seleccionado negro
                                    unselectedColor = Color.Gray
                                )
                            )
                            Text(text = materia.nombre, color = Color.Black, modifier = Modifier.weight(1f))
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(modifier = Modifier.size(14.dp).background(materiaColorSafe(materia), RoundedCornerShape(2.dp)))
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- FECHA ---
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.fecha_entrega),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    OutlinedTextField(
                        value = if (fechaSeleccionada.isEmpty()) stringResource(R.string.seleccionar_fecha) else fechaSeleccionada,
                        onValueChange = { },
                        readOnly = true,
                        trailingIcon = {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = stringResource(R.string.cd_calendario),
                                tint = Color.Black
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                            .clickable { datePickerDialog.show() },
                        enabled = false,
                        colors = OutlinedTextFieldDefaults.colors(
                            disabledTextColor = Color.Black,
                            disabledBorderColor = Color.Transparent,
                            disabledPlaceholderColor = Color.Black,
                            disabledLeadingIconColor = Color.Black,
                            disabledTrailingIconColor = Color.Black
                        ),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // --- DESCRIPCION ---
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = stringResource(R.string.descripcion_label),
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    OutlinedTextField(
                        value = descripcion,
                        onValueChange = { descripcion = it },
                        placeholder = { Text(stringResource(R.string.descripcion_placeholder), color = Color.Gray) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .border(1.dp, Color.Black, RoundedCornerShape(8.dp)),
                        textStyle = TextStyle(fontSize = 16.sp, color = Color.Black),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color.Transparent,
                            unfocusedBorderColor = Color.Transparent,
                            cursorColor = Color.Black
                        )
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // --- BOTON GUARDAR/ACTUALIZAR ---
                Button(
                    onClick = {
                        if (titulo.isBlank() || materiaSeleccionadaId.isBlank() || fechaSeleccionada.isEmpty()) {
                            scope.launch { snackbarHostState.showSnackbar(errorMsg) }
                        } else {
                            if (pendienteId != null) {
                                // Modo Edición: Asegúrate de tener este método en el ViewModel
                                viewModel.actualizarPendiente(pendienteId, titulo, descripcion, materiaSeleccionadaId, fechaSeleccionada)
                            } else {
                                // Modo Creación
                                viewModel.guardar(titulo, descripcion, materiaSeleccionadaId, fechaSeleccionada)
                            }
                            scope.launch { snackbarHostState.showSnackbar(successMsg) }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF5B800)),
                    shape = RoundedCornerShape(50),
                    modifier = Modifier.width(150.dp).height(45.dp)
                ) {
                    Text(
                        if (pendienteId == null) stringResource(R.string.guardar) else stringResource(R.string.actualizar),
                        color = Color.Black,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

fun materiaColorSafe(m: Materia): Color {
    return try {
        val hex = m.color.removePrefix("#")
        val colorInt = android.graphics.Color.parseColor("#$hex")
        Color(colorInt)
    } catch (e: Exception) {
        Color(0xFFBDBDBD)
    }
}