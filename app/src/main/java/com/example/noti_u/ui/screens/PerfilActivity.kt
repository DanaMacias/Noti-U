package com.example.noti_u.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation
import com.example.noti_u.ui.viewmodel.PerfilViewModel


class PerfilActivity : BaseLanguageActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotiUTheme {
                PerfilScreen()
            }
        }
    }
}

@Composable
fun PerfilScreen(
    viewModel: PerfilViewModel = viewModel()
) {
    val context = LocalContext.current
    val user = viewModel.userData.collectAsState().value


    fun cambiarIdioma(idioma: String) {

        val prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE)
        prefs.edit().putString("language", idioma).apply()


        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)


        (context as? Activity)?.finish()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = stringResource(R.string.logo),
                modifier = Modifier.size(120.dp)
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.size(36.dp)
                ) {
                    (context as? ComponentActivity)?.finish()
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = stringResource(R.string.mi_perfil),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 30.sp,
                    color = Color.Black
                )
            }

            buttonAnimation(
                drawableId = R.drawable.editar,
                modifier = Modifier.size(32.dp)
            ) {
                val intent = Intent(context, EditarPerfilActivity::class.java)
                context.startActivity(intent)
                (context as? ComponentActivity)?.overridePendingTransition(
                    android.R.anim.fade_in,
                    android.R.anim.fade_out
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

        Text(
            text = stringResource(R.string.datos_usuario),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )


        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFFFB74D), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Column {

                Text(
                    text = stringResource(R.string.nombre),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = user?.nombre ?: stringResource(R.string.cargando),
                    color = Color.Black,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.correo),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = user?.correo ?: "",
                    color = Color.Black,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.telefono),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = user?.telefono ?: "",
                    color = Color.Black,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.area),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = user?.area ?: "",
                    color = Color.Black,
                    fontSize = 14.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = stringResource(R.string.institucion),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Text(
                    text = user?.institucion ?: "",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(25.dp))

        Text(
            text = stringResource(R.string.periodo_academico),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFFFB74D), RoundedCornerShape(12.dp))
                .padding(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color(0xFFFFB74D), RoundedCornerShape(12.dp))
                    .padding(12.dp)
            ) {
                Column {

                    Text(
                        text = "Periodo: ${user?.periodo ?: "Sin definir"}",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Fecha inicio: ${user?.fechaInicio ?: "----"}",
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Fecha fin: ${user?.fechaFin ?: "----"}",
                        color = Color.Black,
                        fontSize = 14.sp
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Duración: ${user?.duracion ?: "----"}",
                        color = Color.Black,
                        fontSize = 14.sp
                    )
                }
            }

        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = stringResource(R.string.configuracion_idioma),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Start)
        )


        Row(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color(0xFFE6E6E6), RoundedCornerShape(20.dp))
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.espanol),
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
                    .clickable { cambiarIdioma("es") }
                    .padding(8.dp)
            )
            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
            )
            Text(
                text = stringResource(R.string.english),
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier
                    .clickable { cambiarIdioma("en") }
                    .padding(8.dp)
            )
        }
    }
}