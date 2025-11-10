package com.example.noti_u

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.theme.NotiUTheme

class RegistroActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF9F6EE)
                ) {
                    RegistroScreen(onRegistrarClick = {

                        finish()
                    })
                }
            }
        }
    }
}

@Composable
fun RegistroScreen(onRegistrarClick: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var area by remember { mutableStateOf("") }
    var institucion by remember { mutableStateOf("") }

    val textColor = Color(0xFF212121)


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

        Text(
            text = "Información Personal",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomField("Nombre", nombre) { nombre = it }
        CustomField("Correo", correo) { correo = it }
        CustomField("Número de Teléfono", telefono) { telefono = it }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Información Académica",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = textColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomField("Área de estudio", area) { area = it }
        CustomField("Institución educativa", institucion) { institucion = it }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onRegistrarClick,
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(
                text = "Registrar",
                color = textColor,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}


@Composable
fun CustomField(label: String, value: String, onChange: (String) -> Unit) {
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
            textStyle = LocalTextStyle.current.copy(color = textColor),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = textColor,
                unfocusedBorderColor = textColor.copy(alpha = 0.5f),
                cursorColor = textColor
            )
        )
        Spacer(modifier = Modifier.height(12.dp))
    }
}
