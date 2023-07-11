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
import java.net.URI
import java.net.URL
import java.nio.ByteBuffer
import java.nio.channels.Channels
import java.text.DateFormat
import java.util.Date
import java.util.Locale

class CreateViewModel() : ViewModel() {
    var nameText: String by mutableStateOf("")
    var dateVal: Long? by mutableStateOf(null)
    var reasonText: String by mutableStateOf("")
    var selectedImageUri: Uri? by mutableStateOf<Uri?>(null)

    var createConditionsMet by mutableStateOf(false)

    //getters
    fun getDateText(): String {
        return convertMillisecondsToDate(dateVal)
    }

    fun getImageBlob(): ByteArray? {
        return convertUriToBlob(selectedImageUri)
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

    //convert uri to byte array
    private fun convertUriToBlob(uri: Uri?): ByteArray? {
        if (uri == null) {
            // Handle the case when the URI is null
            return null
        }

        val urlString: String = uri.toString()
        val url = URL(urlString)
        val inputStream: InputStream = url.openStream()
        val outputStream = ByteArrayOutputStream()

        // Read the contents of the URI into a byte array
        Channels.newChannel(inputStream).use { channel ->
            val buffer = ByteBuffer.allocate(1024)
            while (channel.read(buffer) != -1) {
                buffer.flip()
                while (buffer.hasRemaining()) {
                    outputStream.write(buffer.get().toInt())
                }
                buffer.clear()
            }
        }

        return outputStream.toByteArray()
    }



}