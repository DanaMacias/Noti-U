package com.example.noti_u

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.theme.NotiUTheme

class PrincipalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PrincipalScreen(
                        onGoToRecordatorios = {
                            startActivity(Intent(this, RecordatoriosActivity::class.java))
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun PrincipalScreen(onGoToRecordatorios: () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // ---------- Logo centrado ----------
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(400.dp) // ✅ Tamaño aumentado del logo
                    .padding(top = 60.dp, bottom = 30.dp)
            )

            // ---------- Recordatorio de ejemplo ----------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF8DD8E1), RoundedCornerShape(20.dp))
                    .padding(20.dp)
            ) {
                Column {
                    Text("Recordatorio de ejemplo", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Tipo: Proyecto final", fontSize = 13.sp)
                    Text("Fecha: 22/01/2025", fontSize = 12.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ---------- Páginas recomendadas ----------
            Text(
                "Páginas Recomendadas",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
                    .background(Color(0xFF5B8D8D), RoundedCornerShape(20.dp))
            )

            Spacer(modifier = Modifier.height(12.dp))

            // ---------- Botón Ir a Recordatorios ----------
            Button(
                onClick = onGoToRecordatorios,
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(top = 32.dp)
            ) {
                Text("Ir a Recordatorios", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}
