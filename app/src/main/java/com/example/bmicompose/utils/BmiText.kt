package com.example.bmicompose.utils

import androidx.compose.ui.graphics.Color

fun bmiText(bmi: Double): String {
    return if (bmi <= 18.5)
        "Underweight"
    else if (bmi > 18.5 && bmi <= 25)
        "Ideal Weight"
    else if (bmi > 25 && bmi <= 30)
        "Obesity I"
    else if (bmi > 30 && bmi <= 35)
        "Obesity II"
    else if (bmi > 35 && bmi <= 40)
        "Obesity III"
    else
        "Bruh"
}