package com.example.noti_u.ui.screens

import android.R
import android.app.Activity
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.noti_u.MainActivity
import com.example.noti_u.ui.theme.BarraLateralDesplegable
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

abstract class BaseMenuActivity : ComponentActivity() {

    @Composable
    abstract fun PantallaContenido(innerPadding: PaddingValues)

    fun setPantallaConMenu() {
        val currentActivity = this

        setContent {
            NotiUTheme {
                Surface(modifier = Modifier.Companion.fillMaxSize()) {
                    var selectedTab by remember { mutableStateOf<String?>(null) }
                    val context = LocalContext.current

                    Row(
                        modifier = Modifier.Companion
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
                                        (context as? Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(
                                                R.anim.fade_in,
                                                R.anim.fade_out
                                            )
                                        }
                                    }

                                    "Pendientes" -> if (currentActivity !is PendientesActivity) {
                                        val intent = Intent(context, PendientesActivity::class.java)
                                        (context as? Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(
                                                R.anim.fade_in,
                                                R.anim.fade_out
                                            )
                                        }
                                    }

                                    "Recordatorios" -> if (currentActivity !is RecordatoriosActivity) {
                                        val intent =
                                            Intent(context, RecordatoriosActivity::class.java)
                                        (context as? Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(
                                                R.anim.fade_in,
                                                R.anim.fade_out
                                            )
                                        }
                                    }

                                    "Notas" -> if (currentActivity !is NotasActivity) {
                                        val intent = Intent(context, NotasActivity::class.java)
                                        (context as? Activity)?.apply {
                                            startActivity(intent)
                                            overridePendingTransition(
                                                R.anim.fade_in,
                                                R.anim.fade_out
                                            )
                                        }
                                    }
                                }
                            }

                        )


                        Column(
                            modifier = Modifier.Companion
                                .fillMaxSize()
                                .background(Color(0xFFFAF3E0))
                        ) {

                            Row(
                                modifier = Modifier.Companion
                                    .fillMaxWidth()
                                    .background(Color(0xFFFAF3E0))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.Companion.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val context = LocalContext.current


                                buttonAnimation(
                                    drawableId = com.example.noti_u.R.drawable.home,
                                    modifier = Modifier.Companion.size(40.dp)
                                ) {
                                    val currentActivity = context as? ComponentActivity
                                    if (currentActivity !is PrincipalActivity) {
                                        val intent = Intent(context, PrincipalActivity::class.java)
                                        context.startActivity(intent)
                                        currentActivity?.overridePendingTransition(
                                            R.anim.fade_in,
                                            R.anim.fade_out
                                        )
                                    }
                                }



                                Image(
                                    painter = painterResource(id = com.example.noti_u.R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.Companion.size(80.dp)
                                )


                                var expanded by remember { mutableStateOf(false) }

                                Box {
                                    buttonAnimation(
                                        drawableId = com.example.noti_u.R.drawable.perfil,
                                        modifier = Modifier.Companion.size(40.dp)
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
                                                val intent =
                                                    Intent(context, PerfilActivity::class.java)
                                                context.startActivity(intent)
                                                (context as? ComponentActivity)?.overridePendingTransition(
                                                    R.anim.fade_in,
                                                    R.anim.fade_out
                                                )
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text("Editar perfil") },
                                            onClick = {
                                                expanded = false
                                                val intent = Intent(
                                                    context,
                                                    EditarPerfilActivity::class.java
                                                )
                                                context.startActivity(intent)
                                                (context as? ComponentActivity)?.overridePendingTransition(
                                                    R.anim.fade_in,
                                                    R.anim.fade_out
                                                )
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text("Cerrar sesión") },
                                            onClick = {
                                                expanded = false
                                                //limpiar datos
                                                val intent =
                                                    Intent(context, MainActivity::class.java)
                                                context.startActivity(intent)
                                                (context as? ComponentActivity)?.finish()
                                            }
                                        )
                                    }
                                }
                            }



                            Box(
                                modifier = Modifier.Companion
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