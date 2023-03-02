package com.example.bmicompose.utils

import androidx.compose.ui.graphics.Color

fun bmiColor(bmi: Double): Color {
    return if (bmi <= 18.5)
        Color.Red
    else if (bmi > 18.5 && bmi <= 25)
        Color.Green
    else if (bmi > 25 && bmi <= 30)
        Color.Yellow
    else if (bmi > 30 && bmi <= 35)
        Color(253, 129, 19, 255)
    else if (bmi > 35 && bmi <= 40)
        Color.Red
    else
        Color.DarkGray
}