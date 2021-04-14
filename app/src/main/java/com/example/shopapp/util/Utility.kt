package com.example.shopapp.util

import com.example.shopapp.R
import java.text.SimpleDateFormat
import java.util.*

fun getCategoryColorByName(categoryName: String): Int {
    return when (categoryName) {
        "Stovarište" -> R.color.red
        "Mašine" -> R.color.blue
        else -> R.color.green
    }
}

fun getPaymentMethodNameByID(paymentMethodID:Int):String{
    return if (paymentMethodID == 1) "Kreditna kartica" else "Keš"
}

fun convertLongToTime(time: Long): String {
    val date = Date(time)
    val format = SimpleDateFormat("yyyy.MM.dd")
    return format.format(date)
}