package com.example.noti_u.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.noti_u.R

// 🔹 Agrupa todas las variantes de Comic Sans
val ComicSans = FontFamily(
    Font(R.font.comic_sans, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.comic_sans_italic, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.comic_sans_negrita, FontWeight.Bold, FontStyle.Normal),
    Font(R.font.comic_sans_italic_negrita, FontWeight.Bold, FontStyle.Italic)
)

// 🔹 Define la tipografía global
val AppTypography = Typography(
    bodyLarge = TextStyle(
        fontFamily = ComicSans,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    titleLarge = TextStyle(
        fontFamily = ComicSans,
        fontWeight = FontWeight.Bold,
        fontSize = 22.sp
    ),
    labelLarge = TextStyle(
        fontFamily = ComicSans,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp
    )
)
