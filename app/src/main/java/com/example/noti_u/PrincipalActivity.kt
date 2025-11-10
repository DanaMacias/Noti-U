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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import com.example.noti_u.ui.theme.BarraLateralDesplegable

class PrincipalActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    PrincipalScreen()
                }
            }
        }
    }
}


@Composable
fun PrincipalScreen() {
    var selectedTab by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
    ) {

        BarraLateralDesplegable(
            selectedTab = selectedTab,
            onSelect = { selectedTab = it },
            onNavigate = { destino ->
                val activity = context as? ComponentActivity
                when (destino) {
                    "Horarios" -> {
                        val intent = Intent(context, HorariosActivity::class.java)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    "Pendientes" -> {
                        val intent = Intent(context, PendientesActivity::class.java)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    "Recordatorios" -> {
                        val intent = Intent(context, RecordatoriosActivity::class.java)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                    "Notas" -> {
                        val intent = Intent(context, NotasActivity::class.java)
                        activity?.startActivity(intent)
                        activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                    }
                }
            }
        )


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopEnd
            ) {
                val context = LocalContext.current

                buttonAnimation(
                    drawableId = R.drawable.perfil,
                    modifier = Modifier.size(40.dp)
                ) {
                    expanded = !expanded
                }

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .align(Alignment.TopEnd)
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
                            //limpiar datos
                            val intent = Intent(context, MainActivity::class.java)
                            context.startActivity(intent)
                            (context as? ComponentActivity)?.finish()
                        }
                    )
                }
            }

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(vertical = 16.dp)
            )


            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                RecordatorioBox("Proyecto final", "Proyecto • Materia", "22/01/2025")
                RecordatorioBox("Parcial segundo corte", "Examen • Materia", "22/01/2025")
                RecordatorioBox("Exposición", "Presentación • Materia", "22/01/2025")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Páginas Recomendadas",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .height(60.dp)
                    .background(Color(0xFF5B8D8D), RoundedCornerShape(20.dp))
                    .align(Alignment.CenterHorizontally)
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}


@Composable
fun RecordatorioBox(titulo: String, subtitulo: String, fecha: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF8DD8E1), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Column {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(subtitulo, fontSize = 14.sp, color = Color.Black)
                Text(fecha, fontSize = 14.sp, color = Color.Black, fontWeight = FontWeight.Medium)
            }
        }
    }
}
