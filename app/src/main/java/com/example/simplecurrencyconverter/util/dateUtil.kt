package com.example.simplecurrencyconverter.util

import java.text.SimpleDateFormat
import java.util.*

fun convertTimestampToString(timeStamp : Long):String{
    val dateFormate : SimpleDateFormat= SimpleDateFormat("dd:MM:yyyy")

    return dateFormate.format(Date(timeStamp))
}