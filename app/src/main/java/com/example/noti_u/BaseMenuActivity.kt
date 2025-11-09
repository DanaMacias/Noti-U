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
                                    if (currentActivity !is PrincipalActivity) {
                                        context.startActivity(Intent(context, PrincipalActivity::class.java))
                                    }
                                }


                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = "Logo",
                                    modifier = Modifier.size(80.dp)
                                )


                                buttonAnimation(
                                    drawableId = R.drawable.perfil,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    // if (currentActivity !is PerfilActivity) {
                                    //     context.startActivity(Intent(context, PerfilActivity::class.java))
                                    // }
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
