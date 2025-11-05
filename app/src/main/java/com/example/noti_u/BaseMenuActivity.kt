package com.example.noti_u

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.noti_u.ui.theme.BarraLateralDesplegable
import com.example.noti_u.ui.theme.NotiUTheme

abstract class BaseMenuActivity : ComponentActivity() {

    @Composable
    abstract fun PantallaContenido(innerPadding: PaddingValues)

    fun setPantallaConMenu() {

        val currentActivity = this

        setContent {
            NotiUTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    var selectedTab by remember { mutableStateOf<String?>(null) }
                    val context = LocalContext.current

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFAF3E0))
                    ) {
                        // 🔸 Barra lateral
                        BarraLateralDesplegable(
                            selectedTab = selectedTab,
                            onSelect = { selectedTab = it },
                            onNavigate = { destino ->
                                when (destino) {
                                    "Horarios" -> if (currentActivity !is HorariosActivity)
                                        context.startActivity(Intent(context, HorariosActivity::class.java))
                                    "Pendientes" -> if (currentActivity !is PendientesActivity)
                                        context.startActivity(Intent(context, PendientesActivity::class.java))
                                    "Recordatorios" -> if (currentActivity !is RecordatoriosActivity)
                                        context.startActivity(Intent(context, RecordatoriosActivity::class.java))
                                    "Notas" -> if (currentActivity !is NotasActivity)
                                        context.startActivity(Intent(context, NotasActivity::class.java))
                                }
                            }
                        )

                        // 🔸 Contenido específico de cada pantalla
                        Box(modifier = Modifier.fillMaxSize()) {
                            PantallaContenido(PaddingValues())
                        }
                    }
                }
            }
        }
    }
}
