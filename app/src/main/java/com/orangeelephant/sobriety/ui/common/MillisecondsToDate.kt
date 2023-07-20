package com.orangeelephant.sobriety.ui.common

import java.text.DateFormat
import java.util.Date
import java.util.Locale

fun convertMillisecondsToDate(utcMilliseconds: Long?): String {
    if (utcMilliseconds == null) {
        return ""
    }
    val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    val date = Date(utcMilliseconds)
    return dateFormat.format(date)
}