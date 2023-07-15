package com.cultivaet.hassad.core.extension

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.text.format.DateUtils
import com.cultivaet.hassad.core.util.Constants.Day.TODAY
import com.cultivaet.hassad.core.util.Constants.Day.TOMORROW
import com.cultivaet.hassad.core.util.Constants.Day.YESTERDAY
import java.text.SimpleDateFormat
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.convertDate(): Date {
    return SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(this)
}

@SuppressLint("SimpleDateFormat")
fun String.convertDateOnly(): Date {
    return SimpleDateFormat("yyyy-MM-dd").parse(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.extractDateOnly(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    return formatter.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.extractDate(): String {
    val formatter = SimpleDateFormat("EEE, d MMM")
    return formatter.format(this)
}

@SuppressLint("SimpleDateFormat")
fun String.extractDateOnly(): String {
    val formatter = SimpleDateFormat("EEE, d MMM")
    return formatter.format(this.convertDate())
}

@SuppressLint("SimpleDateFormat")
fun String.extractTimeOnly(): String {
    val formatter = SimpleDateFormat("hh:mma")
    return formatter.format(this.convertDate())
}

fun Date.isToday(): Boolean {
    return DateUtils.isToday(this.time)
}

fun Date.isYesterday(): Boolean {
    return DateUtils.isToday(this.time + DateUtils.DAY_IN_MILLIS)
}

fun Date.isTomorrow(): Boolean {
    return DateUtils.isToday(this.time - DateUtils.DAY_IN_MILLIS)
}

fun String.getDay(): String {
    return when {
        this.convertDate().isYesterday() -> {
            YESTERDAY
        }

        this.convertDate().isToday() -> {
            TODAY
        }

        this.convertDate().isTomorrow() -> {
            TOMORROW
        }

        else -> {
            this.extractDateOnly()
        }
    }
}

fun Context.customDialog(selected: (Calendar) -> Unit) {
    val calendar: Calendar = Calendar.getInstance()
    val datePickerDialog = DatePickerDialog(
        this,
        { _, year, month, dayOfMonth ->
            val secondCalendar = Calendar.getInstance()
            secondCalendar.set(year, month, dayOfMonth)
            selected.invoke(secondCalendar)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )
    datePickerDialog.show()
}
