package com.example.noti_u

import androidx.navigation.NavHostController
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun NotasScreen(navController: NavHostController) {

    var selectedTab by remember { mutableStateOf<String?>(null) }

    val tabs = listOf("Horarios", "Pendientes", "Recordatorios", "Notas")

    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0)) // ✅ Fondo igual al contenido
    ) {
        // ====== BARRA LATERAL ======
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .width(90.dp)
                .padding(vertical = 24.dp) // ✅ deja espacio arriba y abajo
                .background(Color(0xFFFAF3E0)), // ✅ evita el fondo blanco
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalAlignment = Alignment.Start
        ) {
            tabs.forEachIndexed { index, name ->
                val isSelected = selectedTab == name
                val widthAnim by animateDpAsState(
                    targetValue = if (isSelected) 210.dp else 75.dp,
                    animationSpec = tween(400)
                )

                val shape = when (index) {
                    0 -> RoundedCornerShape(topEnd = 32.dp)
                    tabs.lastIndex -> RoundedCornerShape(bottomEnd = 32.dp)
                    else -> RoundedCornerShape(12.dp)
                }

                Box(
                    modifier = Modifier
                        .width(widthAnim)
                        .height(200.dp) // ✅ cada carpeta más pequeña
                        .background(
                            color = Color(0xFFFFA726),
                            shape = shape
                        )
                        .clickable { selectedTab = if (isSelected) null else name },
                    contentAlignment = Alignment.CenterStart
                ) {
                    // Texto horizontal al expandirse
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isSelected,
                        enter = expandHorizontally(animationSpec = tween(400)) + fadeIn(),
                        exit = shrinkHorizontally(animationSpec = tween(300)) + fadeOut()
                    ) {
                        Text(
                            text = name,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(start = 24.dp)
                        )
                    }

                    // Texto vertical cuando está cerrada
                    androidx.compose.animation.AnimatedVisibility(
                        visible = !isSelected,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = name,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black,
                                maxLines = 1,
                                softWrap = true,



                                )
                        }
                    }
                }
            }
        }

        // ====== CONTENIDO PRINCIPAL ======
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFFAF3E0))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo Noti Ü",
                    modifier = Modifier
                        .size(200.dp)
                        .padding(bottom = 16.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Todas las Notas",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Image(
                        painter = painterResource(id = R.drawable.buscar),
                        contentDescription = "Buscar",
                        modifier = Modifier.size(32.dp)
                    )
                }

                val cantidadNotas = NotaRepository.notas.size
                Text(
                    text = if (cantidadNotas == 0) "No hay notas todavía" else "$cantidadNotas notas",
                    fontSize = 16.sp,
                    color = Color.Gray,
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
                                    .height(150.dp)
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
}

// 🟢 Card de nota
@Composable
fun NotaCard(titulo: String, descripcion: String, color: Color, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(color, shape = RoundedCornerShape(16.dp))
            .padding(12.dp)
    ) {
        Column {
            Text(titulo, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(descripcion, color = Color.DarkGray)
        }
    }
}
