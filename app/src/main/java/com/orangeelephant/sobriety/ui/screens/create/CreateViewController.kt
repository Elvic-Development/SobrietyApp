package com.orangeelephant.sobriety.ui.screens.create

import android.content.ContentResolver
import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf

import androidx.compose.runtime.setValue

import androidx.lifecycle.ViewModel

import java.io.ByteArrayOutputStream
import java.io.IOException
import java.io.InputStream
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class CreateViewModel() : ViewModel() {
    var nameText: String by mutableStateOf("")
    var dateVal: Long? by mutableStateOf(null)
    var reasonText: String by mutableStateOf("")
    var selectedImageUri: Uri? by mutableStateOf<Uri?>(null)
    var imageBlob: ByteArray? by mutableStateOf<ByteArray?>(null)

    var createConditionsMet by mutableStateOf(false)

    //getters
    fun getDateText(): String {
        return convertMillisecondsToDate(dateVal)
    }

    //utcmillisecond to date
    private fun convertMillisecondsToDate(utcMilliseconds: Long?): String {
        if (utcMilliseconds == null) {
            return ""
        }
        val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
        val date = Date(utcMilliseconds)
        return dateFormat.format(date)
    }


}