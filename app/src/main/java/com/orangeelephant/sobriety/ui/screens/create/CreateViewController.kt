package com.orangeelephant.sobriety.ui.screens.create

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CreateViewModel() : ViewModel() {
    var nameText: String by mutableStateOf("")
    var dateVal: Long? by mutableStateOf(null)
    var reasonText: String by mutableStateOf("")
    val selectedImageUri: MutableState<Uri?> = mutableStateOf(null)


}
//    //convert uri to byte array
//    private fun convertUriToBlob(uri: Uri?): ByteArray? {
//        if (uri == null) {
//            // Handle the case when the URI is null
//            return null
//        }
//
//        val urlString: String = uri.toString()
//        val url = URL(urlString)
//        val inputStream: InputStream = url.openStream()
//        val outputStream = ByteArrayOutputStream()
//
//        // Read the contents of the URI into a byte array
//        Channels.newChannel(inputStream).use { channel ->
//            val buffer = ByteBuffer.allocate(1024)
//            while (channel.read(buffer) != -1) {
//                buffer.flip()
//                while (buffer.hasRemaining()) {
//                    outputStream.write(buffer.get().toInt())
//                }
//                buffer.clear()
//            }
//        }
//
//        return outputStream.toByteArray()
//    }