package com.example.studentprofile.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import android.widget.EditText
import android.widget.Toast

fun View.show() { visibility = View.VISIBLE }
fun View.gone() { visibility = View.GONE }
fun View.invisible() { visibility = View.INVISIBLE }

fun Context.toast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun EditText.trimmedText(): String = text.toString().trim()

fun Double.toAcademicRanking(): String = when {
    this >= 3.6 -> "Xuất sắc (Excellent)"
    this >= 3.2 -> "Giỏi (Very Good)"
    this >= 2.5 -> "Khá (Good)"
    this >= 2.0 -> "Trung bình (Average)"
    this >= 1.0 -> "Yếu (Weak)"
    else -> "Kém (Poor)"
}

fun Double.toRankingColor(): Int = when {
    this >= 3.6 -> Color.parseColor("#34B469")
    this >= 3.2 -> Color.parseColor("#00BCD4")
    this >= 2.5 -> Color.parseColor("#FF9800")
    else -> Color.parseColor("#F44336")
}