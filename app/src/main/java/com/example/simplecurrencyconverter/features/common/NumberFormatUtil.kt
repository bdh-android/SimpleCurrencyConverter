package com.example.simplecurrencyconverter.features.common

import java.math.RoundingMode
import java.text.DecimalFormat

fun Double.roundBy():String{
     val decimalFormate = DecimalFormat("#.##")
    decimalFormate.roundingMode=RoundingMode.CEILING
    return decimalFormate.format(this)
 }