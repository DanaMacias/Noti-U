package com.example.noti_u.ui.screens

import android.R.color.black
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.R
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.viewmodel.LoginViewModel

class LoginActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFFF9F6EE)
                ) {
                    LoginScreen()
                }
            }
        }
    }
}

@Composable
fun LoginScreen() {
    val viewModel: LoginViewModel = viewModel()
    val loginState by viewModel.loginState.collectAsState()

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val context = LocalContext.current
    val textColor = Color(0xFF212121)

    LaunchedEffect(loginState) {
        loginState?.onSuccess {
            Toast.makeText(context, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
            context.startActivity(Intent(context, PrincipalActivity::class.java))
        }?.onFailure {
            Toast.makeText(context, "Correo o contraseña incorrectos", Toast.LENGTH_LONG).show()
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = androidx.compose.ui.layout.ContentScale.Crop
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.iniciousuario),
                contentDescription = "Icono del usuario",
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("Correo electrónico", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Ingresa tu correo") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text("Contraseña", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Ingresa tu contraseña") },
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    when {
                        email.isBlank() && password.isBlank() -> {
                            Toast.makeText(context, "Debes llenar todos los campos", Toast.LENGTH_LONG).show()
                        }
                        email.isBlank() -> {
                            Toast.makeText(context, "El correo no puede estar vacío", Toast.LENGTH_LONG).show()
                        }
                        password.isBlank() -> {
                            Toast.makeText(context, "La contraseña no puede estar vacía", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            viewModel.login(email.trim(), password.trim())
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300))
            ) {
                Text("Iniciar", color = textColor, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Registrarse",
                fontSize = 16.sp,
                color = textColor.copy(alpha = 0.7f),
                modifier = Modifier.clickable {
                    context.startActivity(Intent(context, RegistroActivity::class.java))
                }
            )
        }
    }
}
