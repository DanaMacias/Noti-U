package com.example.noti_u

import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.noti_u.ui.theme.BarraLateralDesplegable
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation

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

                        BarraLateralDesplegable(
                            selectedTab = selectedTab,
                            onSelect = { selectedTab = it },
                            onNavigate = { destino ->
                                when (destino) {
                                    "Horarios" -> if (currentActivity !is HorariosActivity) {
                                        val intent = Intent(context, HorariosActivity::class.java)
                                        (context as? android.app.Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }
                                    }

                                    "Pendientes" -> if (currentActivity !is PendientesActivity) {
                                        val intent = Intent(context, PendientesActivity::class.java)
                                        (context as? android.app.Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }
                                    }

                                    "Recordatorios" -> if (currentActivity !is RecordatoriosActivity) {
                                        val intent = Intent(context, RecordatoriosActivity::class.java)
                                        (context as? android.app.Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }
                                    }

                                    "Notas" -> if (currentActivity !is NotasActivity) {
                                        val intent = Intent(context, NotasActivity::class.java)
                                        (context as? android.app.Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                                        }
                                    }
                                }
                            }

                        )


                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFAF3E0))
                        ) {

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFAF3E0))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val context = LocalContext.current


                                buttonAnimation(
                                    drawableId = R.drawable.home,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    val currentActivity = context as? ComponentActivity
                                    if (currentActivity !is PrincipalActivity) {
                                        val intent = Intent(context, PrincipalActivity::class.java)
                                        context.startActivity(intent)
                                        currentActivity?.overridePendingTransition(
                                            android.R.anim.fade_in,
                                            android.R.anim.fade_out
                                        )
                                    }
                                }



                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(80.dp)
                                )


                                var expanded by remember { mutableStateOf(false) }

                                Box {
                                    buttonAnimation(
                                        drawableId = R.drawable.perfil,
                                        modifier = Modifier.size(40.dp)
                                    ) {
                                        expanded = !expanded
                                    }

                                    DropdownMenu(
                                        expanded = expanded,
                                        onDismissRequest = { expanded = false },
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
                            }



                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(Color(0xFFFAF3E0))
                            ) {
                                PantallaContenido(PaddingValues())
                            }
                        }
                    }
                }
            }
        }
    }
}
