package com.example.noti_u.ui.base

import android.content.Intent
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.ui.screens.*
import com.example.noti_u.ui.theme.BarraLateralDesplegable
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation

abstract class BaseMenuActivity : BaseLanguageActivity() {

    @Composable
    abstract fun PantallaContenido(innerPadding: PaddingValues)

    fun setPantallaConMenu() {
        setContent {
            NotiUTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    // Estado para saber qué pestaña está seleccionada visualmente
                    var selectedTab by remember { mutableStateOf<String?>(null) }
                    val context = LocalContext.current

                    // Obtenemos los strings traducidos para comparar en la navegación
                    val txtHorarios = stringResource(R.string.menu_horarios)
                    val txtPendientes = stringResource(R.string.menu_pendientes)
                    val txtRecordatorios = stringResource(R.string.menu_recordatorios)
                    val txtNotas = stringResource(R.string.menu_notas)

                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFFAF3E0))
                    ) {

                        // --- BARRA LATERAL ---
                        BarraLateralDesplegable(
                            selectedTab = selectedTab,
                            onSelect = { selectedTab = it },
                            onNavigate = { destinoTexto ->
                                // Comparamos el texto que viene del clic con el recurso actual
                                when (destinoTexto) {
                                    txtHorarios -> navegarA(HorariosActivity::class.java)
                                    txtPendientes -> navegarA(PendientesActivity::class.java)
                                    txtRecordatorios -> navegarA(RecordatoriosActivity::class.java)
                                    txtNotas -> navegarA(NotasActivity::class.java)
                                }
                            }
                        )

                        // --- CONTENIDO PRINCIPAL ---
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(Color(0xFFFAF3E0))
                        ) {

                            // --- BARRA SUPERIOR (LOGO, HOME, PERFIL) ---
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(0xFFFAF3E0))
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {

                                // Botón Home
                                buttonAnimation(
                                    drawableId = R.drawable.home,
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    navegarA(PrincipalActivity::class.java)
                                }

                                // Logo Central
                                Image(
                                    painter = painterResource(id = R.drawable.logo),
                                    contentDescription = stringResource(R.string.cd_logo),
                                    modifier = Modifier.size(80.dp)
                                )

                                // Menú Perfil
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
                                            text = { Text(stringResource(R.string.menu_ver_perfil)) },
                                            onClick = {
                                                expanded = false
                                                navegarA(PerfilActivity::class.java)
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.menu_editar_perfil)) },
                                            onClick = {
                                                expanded = false
                                                navegarA(EditarPerfilActivity::class.java)
                                            }
                                        )

                                        DropdownMenuItem(
                                            text = { Text(stringResource(R.string.menu_cerrar_sesion)) },
                                            onClick = {
                                                expanded = false
                                                // Cerrar sesión y volver al login/main
                                                val intent = Intent(context, MainActivity::class.java)
                                                // Flags para limpiar la pila de actividades
                                                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)
                                                finish()
                                            }
                                        )
                                    }
                                }
                            }

                            // --- CONTENIDO DE LA ACTIVIDAD HIJA ---
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

    /**
     * Función auxiliar para manejar la navegación entre actividades
     * Evita recargar la actividad si ya estamos en ella.
     */
    private fun navegarA(claseDestino: Class<*>) {
        if (this::class.java != claseDestino) {
            val intent = Intent(this, claseDestino)
            startActivity(intent)
            // Animación suave de transición
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)

        }
    }
}