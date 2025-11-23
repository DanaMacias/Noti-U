package com.example.noti_u.ui.screens

import android.os.Bundle
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noti_u.R

val DarkTextColor = Color(0xFF1C1C1C)

class NotasActivity : BaseMenuActivity() {

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        val navController = rememberNavController()

        NavHost(
            navController = navController,
            startDestination = "notasScreen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("notasScreen") { NotasScreen(navController) }
            composable("agregarNota") { AgregarNotaScreen(navController) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }
}

data class Nota(val titulo: String, val descripcion: String)

object NotaRepository {
    val notas = mutableStateListOf<Nota>()

    fun agregarNota(titulo: String, descripcion: String) {
        if (titulo.isNotBlank() && descripcion.isNotBlank()) {
            notas.add(Nota(titulo, descripcion))
        }
    }
}

@Composable
fun PerfilAnimado(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.8f else 1f)


}

@Composable
fun NotasScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
    ) {
        PerfilAnimado(
            modifier = Modifier
                .size(80.dp)
                .align(Alignment.TopEnd)
                .padding(top = 30.dp, end = 30.dp)
        ) {
            println("Perfil tocado")
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Text(
                    text = "Todas las Notas",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    color = DarkTextColor
                )

                Spacer(modifier = Modifier.width(8.dp))

                Image(
                    painter = painterResource(id = R.drawable.buscar),
                    contentDescription = "Buscar",
                    modifier = Modifier.size(32.dp)
                )

                Spacer(modifier = Modifier.weight(1f))
            }

            val cantidadNotas = NotaRepository.notas.size
            Text(
                text = if (cantidadNotas == 0) "No hay notas todavía" else "$cantidadNotas notas",
                fontSize = 16.sp,
                color = DarkTextColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(24.dp))

            val coloresNotas = listOf(
                Color(0xFFFFF9C4),
                Color(0xFFB3E5FC),
                Color(0xFFFFCDD2),
                Color(0xFFC8E6C9),
                Color(0xFFD1C4E9)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(bottom = 80.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                content = {
                    itemsIndexed(NotaRepository.notas) { index, nota ->
                        val color = coloresNotas[index % coloresNotas.size]
                        NotaCard(
                            titulo = nota.titulo,
                            descripcion = nota.descripcion,
                            color = color,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(100.dp)
                        )
                    }
                }
            )
        }

        FloatingActionButton(
            onClick = { navController.navigate("agregarNota") },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(24.dp),
            containerColor = Color.White
        ) {
            Image(
                painter = painterResource(id = R.drawable.agregar),
                contentDescription = "Agregar nota",
                modifier = Modifier.size(45.dp)
            )
        }
    }
}

@Composable
fun NotaCard(titulo: String, descripcion: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = DarkTextColor)
            Spacer(modifier = Modifier.height(4.dp))
            Text(descripcion, color = DarkTextColor)
        }
    }
}

@Composable
fun AgregarNotaScreen(navController: NavHostController) {
    var titulo by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }

    Box(modifier = Modifier.fillMaxSize()) {
        // Fondo
        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp, end = 24.dp),
            contentAlignment = Alignment.TopEnd
        ) {
            PerfilAnimado(
                modifier = Modifier.size(70.dp)
            ) {
                println("Perfil tocado en agregar nota")
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.atras),
                        contentDescription = "Volver",
                        tint = DarkTextColor
                    )
                }

                BasicTextField(
                    value = titulo,
                    onValueChange = { titulo = it },
                    textStyle = TextStyle(
                        fontSize = 22.sp,
                        color = DarkTextColor,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                )

                IconButton(onClick = {
                    if (titulo.text.isNotBlank() && descripcion.text.isNotBlank()) {
                        NotaRepository.agregarNota(titulo.text, descripcion.text)
                        navController.popBackStack()
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.listo),
                        contentDescription = "Guardar",
                        tint = DarkTextColor
                    )
                }
            }
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp),
                color = Color.Black,
                thickness = 1.dp
            )
            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                textStyle = TextStyle(fontSize = 18.sp, color = DarkTextColor),
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                if (descripcion.text.isEmpty()) {
                    Text(
                        text = "Descripción",
                        style = TextStyle(
                            color = DarkTextColor.copy(alpha = 0.6f),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Start
                        )
                    )
                } else {
                    it()
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (titulo.text.isNotBlank() && descripcion.text.isNotBlank()) {
                        NotaRepository.agregarNota(titulo.text, descripcion.text)
                        navController.popBackStack()
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Guardar",
                    color = DarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}
