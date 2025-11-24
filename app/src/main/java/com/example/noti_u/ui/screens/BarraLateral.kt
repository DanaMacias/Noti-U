package com.example.noti_u.ui.theme

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.noti_u.R

@Composable
fun BarraLateralDesplegable(
    selectedTab: String?,
    onSelect: (String?) -> Unit,
    onNavigate: (String) -> Unit
) {
    val tabs = listOf("Horarios", "Pendientes", "Recordatorios", "Notas")

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(90.dp)
            .background(Color(0xFFFAF3E0)),
        verticalArrangement = Arrangement.spacedBy(1.dp),
        horizontalAlignment = Alignment.Start
    ) {
        tabs.forEachIndexed { index, name ->
            val isSelected = selectedTab == name
            val widthAnim by animateDpAsState(
                targetValue = if (isSelected) 220.dp else 75.dp,
                animationSpec = tween(400)
            )




            Box(
                modifier = Modifier
                    .width(widthAnim)
                    .weight(1f)

                    .clickable {
                        onSelect(if (isSelected) null else name)
                        onNavigate(name)
                    },
                contentAlignment = Alignment.Center
            ) {

                Image(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Fondo menú",
                    modifier = Modifier
                        .fillMaxSize()

                )


                Box(
                    modifier = Modifier
                        .wrapContentSize()
                        .rotate(270f),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = name,
                        fontSize = 11.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black,
                        maxLines = 1,
                        softWrap = false
                    )
                }
            }
        }
    }
}