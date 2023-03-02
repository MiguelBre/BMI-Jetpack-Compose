package com.example.bmicompose.utils

import kotlin.math.pow

fun bmiCalculate(weight: Int, height: Double) = weight / (height/100).pow(2)
