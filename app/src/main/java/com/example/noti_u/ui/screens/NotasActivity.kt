package com.example.noti_u.ui.screens

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.noti_u.R
import com.example.noti_u.data.model.Notas
import com.example.noti_u.ui.base.BaseMenuActivity
import com.example.noti_u.ui.viewmodel.NotasViewModel
import com.example.noti_u.utils.FirebaseDataSource

val DarkTextColor = Color(0xFF1C1C1C)

class NotasActivity : BaseMenuActivity() {

    @Composable
    override fun PantallaContenido(innerPadding: PaddingValues) {
        val navController = rememberNavController()
        val viewModel: NotasViewModel = viewModel()

        NavHost(
            navController = navController,
            startDestination = "notasScreen",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("notasScreen") { NotasScreen(navController, viewModel) }
            composable("agregarNota") { AgregarNotaScreen(navController, viewModel) }
            composable("editarNota/{notaId}") { backStackEntry ->
                val notaId = backStackEntry.arguments?.getString("notaId") ?: ""
                EditarNotaScreen(navController, notaId, viewModel)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setPantallaConMenu()
    }
}

@Composable
fun PerfilAnimado(modifier: Modifier = Modifier, onClick: () -> Unit) {
    var pressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(if (pressed) 0.8f else 1f)
    // Animation logic would go here if implemented further
}

/* ---------------------------------------------------
                    NOTAS SCREEN
 ---------------------------------------------------*/

@Composable
fun NotasScreen(
    navController: NavHostController,
    viewModel: NotasViewModel
) {
    val userId = FirebaseDataSource.auth.currentUser!!.uid
    var busquedaActiva by remember { mutableStateOf(false) }
    var textoBusqueda by remember { mutableStateOf(TextFieldValue("")) }

    LaunchedEffect(Unit) { viewModel.cargarNotas(userId) }
    val notas = viewModel.notas

    // Filter notes based on search text
    val notasFiltradas = if (textoBusqueda.text.isBlank()) {
        notas
    } else {
        notas.filter {
            it.titulo.contains(textoBusqueda.text, ignoreCase = true) ||
                    it.descripcion.contains(textoBusqueda.text, ignoreCase = true)
        }
    }

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
        ) { /* Handle profile click */ }

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
                Spacer(modifier = Modifier.width(8.dp))

                if (!busquedaActiva) {
                    Text(
                        text = stringResource(id = R.string.todas_las_notas),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        color = DarkTextColor,
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(onClick = { busquedaActiva = true }) {
                        Image(
                            painter = painterResource(id = R.drawable.buscar),
                            contentDescription = stringResource(id = R.string.buscar),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                } else {
                    // Active search field
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(56.dp)
                            .background(
                                Color.White.copy(alpha = 0.8f),
                                RoundedCornerShape(16.dp)
                            )
                            .padding(horizontal = 16.dp),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (textoBusqueda.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.buscar_notas_hint),
                                fontSize = 20.sp,
                                color = DarkTextColor.copy(alpha = 0.5f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }

                        BasicTextField(
                            value = textoBusqueda,
                            onValueChange = { textoBusqueda = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                fontSize = 20.sp,
                                color = DarkTextColor,
                                fontWeight = FontWeight.Medium
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    IconButton(onClick = {
                        busquedaActiva = false
                        textoBusqueda = TextFieldValue("")
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.atras),
                            contentDescription = stringResource(R.string.cd_cerrar_busqueda),
                            tint = DarkTextColor,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))
            }


            Text(
                text = if (busquedaActiva && textoBusqueda.text.isNotBlank()) {
                    if (notasFiltradas.isEmpty()) stringResource(R.string.no_se_encontraron_notas)
                    else stringResource(R.string.notas_encontradas, notasFiltradas.size)
                } else {
                    if (notas.isEmpty()) stringResource(id = R.string.no_hay_notas)
                    else stringResource(id = R.string.cantidad_notas, notas.size)
                },
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
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                itemsIndexed(notasFiltradas) { index, nota ->
                    val color = coloresNotas[index % coloresNotas.size]
                    NotaCard(
                        titulo = nota.titulo,
                        descripcion = nota.descripcion,
                        color = color,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        onClick = { navController.navigate("editarNota/${nota.id}") }
                    )
                }
            }
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
                contentDescription = stringResource(id = R.string.agregar_nota),
                modifier = Modifier.size(45.dp)
            )
        }
    }
}

/* ---------------------------------------------------
                CARD DE NOTA
 ---------------------------------------------------*/

@Composable
fun NotaCard(
    titulo: String,
    descripcion: String,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(color, shape = RoundedCornerShape(16.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {
        Column {
            Text(
                text = titulo,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = DarkTextColor,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = descripcion,
                color = DarkTextColor,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                fontSize = 14.sp
            )
        }
    }
}

/* ---------------------------------------------------
            AGREGAR NOTA SCREEN
 ---------------------------------------------------*/

@Composable
fun AgregarNotaScreen(
    navController: NavHostController,
    viewModel: NotasViewModel
) {
    var titulo by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    val userId = FirebaseDataSource.auth.currentUser!!.uid

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = stringResource(id = R.string.fondo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 32.dp)
                .padding(top = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = { navController.popBackStack() }) {
                    Icon(
                        painter = painterResource(id = R.drawable.atras),
                        contentDescription = stringResource(id = R.string.volver),
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
                    decorationBox = { innerTextField ->
                        if (titulo.text.isEmpty()) {
                            Text(
                                text = stringResource(R.string.titulo_nota_placeholder),
                                fontSize = 22.sp,
                                color = DarkTextColor.copy(alpha = 0.5f),
                                fontWeight = FontWeight.Bold
                            )
                        }
                        innerTextField()
                    },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = {
                    if (titulo.text.isNotBlank() && descripcion.text.isNotBlank()) {
                        viewModel.agregarNota(
                            titulo.text,
                            descripcion.text,
                            userId
                        ) {
                            navController.popBackStack()
                        }
                    }
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.listo),
                        contentDescription = stringResource(R.string.guardar),
                        tint = DarkTextColor
                    )
                }
            }

            Divider(color = Color.Black, thickness = 1.dp)

            Spacer(modifier = Modifier.height(16.dp))

            BasicTextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                textStyle = TextStyle(fontSize = 18.sp, color = DarkTextColor),
                decorationBox = { innerTextField ->
                    if (descripcion.text.isEmpty()) {
                        Text(
                            text = stringResource(R.string.escribe_nota_aqui),
                            fontSize = 18.sp,
                            color = DarkTextColor.copy(alpha = 0.5f)
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = {
                    if (titulo.text.isNotBlank() && descripcion.text.isNotBlank()) {
                        viewModel.agregarNota(
                            titulo.text,
                            descripcion.text,
                            userId
                        ) {
                            navController.popBackStack()
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = stringResource(R.string.guardar),
                    color = DarkTextColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            }
        }
    }
}

/* ---------------------------------------------------
            EDITAR NOTA SCREEN
 ---------------------------------------------------*/

@Composable
fun EditarNotaScreen(
    navController: NavHostController,
    notaId: String,
    viewModel: NotasViewModel
) {
    var titulo by remember { mutableStateOf(TextFieldValue("")) }
    var descripcion by remember { mutableStateOf(TextFieldValue("")) }
    var isLoading by remember { mutableStateOf(true) }
    val userId = FirebaseDataSource.auth.currentUser!!.uid

    // Load existing note
    LaunchedEffect(notaId) {
        val nota = viewModel.consultarNota(userId, notaId)
        if (nota != null) {
            titulo = TextFieldValue(nota.titulo)
            descripcion = TextFieldValue(nota.descripcion)
        }
        isLoading = false
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Image(
            painter = painterResource(id = R.drawable.fondo),
            contentDescription = stringResource(id = R.string.fondo),
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = DarkTextColor
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp, vertical = 32.dp)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            painter = painterResource(id = R.drawable.atras),
                            contentDescription = stringResource(id = R.string.volver),
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
                        modifier = Modifier.weight(1f)
                    )

                    IconButton(onClick = {
                        if (titulo.text.isNotBlank() && descripcion.text.isNotBlank()) {
                            viewModel.editarNota(
                                notaId,
                                titulo.text,
                                descripcion.text,
                                userId
                            ) {
                                navController.popBackStack()
                            }
                        }
                    }) {
                        Icon(
                            painter = painterResource(id = R.drawable.listo),
                            contentDescription = stringResource(R.string.guardar),
                            tint = DarkTextColor
                        )
                    }
                }

                Divider(color = Color.Black, thickness = 1.dp)

                Spacer(modifier = Modifier.height(16.dp))

                BasicTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    textStyle = TextStyle(fontSize = 18.sp, color = DarkTextColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = {
                            viewModel.eliminarNota(userId, notaId) {
                                navController.popBackStack()
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF5350)),
                        modifier = Modifier
                            .width(140.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = stringResource(R.string.eliminar),
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }

                    Button(
                        onClick = {
                            if (titulo.text.isNotBlank() && descripcion.text.isNotBlank()) {
                                viewModel.editarNota(
                                    notaId,
                                    titulo.text,
                                    descripcion.text,
                                    userId
                                ) {
                                    navController.popBackStack()
                                }
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFA726)),
                        modifier = Modifier
                            .width(140.dp)
                            .height(50.dp),
                        shape = RoundedCornerShape(50)
                    ) {
                        Text(
                            text = stringResource(R.string.guardar),
                            color = DarkTextColor,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
    }
}