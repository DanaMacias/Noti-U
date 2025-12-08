package com.example.noti_u.ui.screens

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.noti_u.MainActivity
import com.example.noti_u.R
import com.example.noti_u.ui.base.BaseLanguageActivity
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation

class CalendarioActivity :  BaseLanguageActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            NotiUTheme {
                PantallaContenido()
            }
        }
    }
}

@Composable
fun PantallaContenido() {
    var expanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
            .padding(16.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = stringResource(R.string.cd_logo),
                    modifier = Modifier.size(80.dp)
                )

                Box {
                    buttonAnimation(
                        drawableId = R.drawable.perfil,
                        modifier = Modifier.size(40.dp)
                    ) { expanded = !expanded }

                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                    ) {

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.ver_perfil)) },
                            onClick = {
                                expanded = false
                                context.startActivity(Intent(context, PerfilActivity::class.java))
                                (context as? ComponentActivity)?.overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.editar_perfil)) },
                            onClick = {
                                expanded = false
                                context.startActivity(Intent(context, EditarPerfilActivity::class.java))
                                (context as? ComponentActivity)?.overridePendingTransition(
                                    android.R.anim.fade_in,
                                    android.R.anim.fade_out
                                )
                            }
                        )

                        DropdownMenuItem(
                            text = { Text(stringResource(R.string.cerrar_sesion)) },
                            onClick = {
                                expanded = false
                                val intent = Intent(context, MainActivity::class.java)
                                context.startActivity(intent)
                                (context as? ComponentActivity)?.finish()
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                buttonAnimation(
                    drawableId = R.drawable.atras,
                    modifier = Modifier.size(32.dp)
                ) {
                    (context as? ComponentActivity)?.finish()
                }

                Text(
                    text = stringResource(R.string.calendario_titulo),
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 22.sp,
                    color = Color.Black
                )

                buttonAnimation(
                    drawableId = R.drawable.editar,
                    modifier = Modifier.size(32.dp)
                ) {}
            }

            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 6.dp, bottom = 10.dp),
                color = Color.Black,
                thickness = 1.dp
            )



            val dias = listOf(
                stringResource(R.string.dia_lun),
                stringResource(R.string.dia_mar),
                stringResource(R.string.dia_mie),
                stringResource(R.string.dia_jue),
                stringResource(R.string.dia_vie),
                stringResource(R.string.dia_sab)
            )

            val horas = listOf(
                stringResource(R.string.hora_8_10),
                stringResource(R.string.hora_10_12),
                stringResource(R.string.hora_12_14),
                stringResource(R.string.hora_14_16),
                stringResource(R.string.hora_16_18)
            )

            Row(
                modifier = Modifier.horizontalScroll(rememberScrollState())
            ) {

                Column {


                    Row(verticalAlignment = Alignment.CenterVertically) {

                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(35.dp)
                        )

                        dias.forEach { dia ->
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFFFB300), shape = RoundedCornerShape(4.dp))
                                    .width(60.dp)
                                    .height(35.dp)
                                    .padding(horizontal = 2.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = dia,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))


                    horas.forEach { hora ->

                        Row(verticalAlignment = Alignment.CenterVertically) {

                            Box(
                                modifier = Modifier
                                    .width(60.dp)
                                    .height(60.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = hora,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color.Black
                                )
                            }

                            dias.forEach { _ ->
                                Box(
                                    modifier = Modifier
                                        .size(60.dp)
                                        .background(Color(0xFFF5F5F5), RoundedCornerShape(6.dp))
                                        .border(1.dp, Color.LightGray, RoundedCornerShape(6.dp)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "", fontSize = 12.sp, color = Color.Black)
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}
