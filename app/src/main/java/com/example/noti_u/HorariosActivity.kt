package com.example.noti_u

import android.content.Intent
import android.os.Bundle
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.theme.buttonAnimation

class HorariosActivity : BaseMenuActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        HorarioScreen(
            onNuevaMateriaClick = {
                // startActivity(Intent(this, NuevaMateriaActivity::class.java))
            },
            onCalendarioClick = {
                startActivity(Intent(this, CalendarioActivity::class.java))
            }
        )
    }

    @Composable
    fun HorarioScreen(
        onNuevaMateriaClick: () -> Unit,
        onCalendarioClick: () -> Unit
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {


                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Logo
                    Image(
                        painter = painterResource(id = R.drawable.logo),
                        contentDescription = "Logo",
                        modifier = Modifier.size(70.dp)
                    )


                    buttonAnimation(
                        drawableId = R.drawable.perfil,
                        modifier = Modifier.size(40.dp)
                    ) {
                        // startActivity(Intent(context, PerfilActivity::class.java))
                    }
                }

                Spacer(modifier = Modifier.height(40.dp))


                Button(
                    onClick = onNuevaMateriaClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text("Nueva Materia", fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.mas),
                            contentDescription = "Agregar materia",
                            tint = Color.Black,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }


                Spacer(modifier = Modifier.height(12.dp))


                Button(
                    onClick = onCalendarioClick,
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFB300)),
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.fillMaxWidth(0.5f)
                ) {
                    Text("Calendario", color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    data class Materia(val nombre: String, val horario: String, val color: Color)

    @Composable
    fun MateriaCard(materia: Materia) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = materia.color,
            modifier = Modifier
                .fillMaxWidth()
                .height(70.dp)
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = materia.nombre,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1C1C1C)
                )
                Text(
                    text = materia.horario,
                    textAlign = TextAlign.End,
                    color = Color(0xFF1C1C1C),
                    fontSize = 14.sp
                )
            }
        }
    }
}
