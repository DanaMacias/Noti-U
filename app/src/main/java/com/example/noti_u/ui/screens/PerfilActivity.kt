package com.example.noti_u.ui.screens

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.ui.screens.EditarPerfilActivity
import com.example.noti_u.R
import com.example.noti_u.ui.theme.NotiUTheme
import com.example.noti_u.ui.theme.buttonAnimation

class PerfilActivity : ComponentActivity() {
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
fun PerfilScreen() {
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAF3E0))
            .padding(16.dp),
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
                contentDescription = "Logo",
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
                    text = "Mi Perfil",
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
            text = "Datos de Usuario",
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
                .padding(top = 8.dp)
        ) {
            Column {
                Text(
                    text = "Nombre del Usuario:\nMarcelo",
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Correo Electrónico:\nmarcelo@usuario.com",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Periodo Académico",
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
                    text = "Duración:\n2025-II",
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Fecha de inicio:\n01/08/2025",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Fecha de fin:\n15/12/2025",
                    color = Color.Black,
                    fontSize = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Configuración de idioma",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            color = Color.Black,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(bottom = 8.dp)
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
                text = "Español",
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.clickable {  }
            )

            Divider(
                color = Color.Gray,
                modifier = Modifier
                    .height(20.dp)
                    .width(1.dp)
            )

            Text(
                text = "English",
                fontWeight = FontWeight.Medium,
                color = Color.Black,
                fontSize = 14.sp,
                modifier = Modifier.clickable { }
            )
        }
    }
}
