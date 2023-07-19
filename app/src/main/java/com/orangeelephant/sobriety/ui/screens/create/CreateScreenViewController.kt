package com.orangeelephant.sobriety.ui.screens.create

import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository


class CreateScreenViewModel(
    private val counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    var nameText: String by mutableStateOf("")
    var dateVal: Long? by mutableStateOf(null)
    var reasonText: String by mutableStateOf("")
    var reasonList: List<String> by mutableStateOf(emptyList())
    val selectedImageUri: MutableState<Uri?> = mutableStateOf(null)

    fun onCreateCounter(counter: Counter, reason: String, onCounterCreated: (counterID: Long) -> Unit) {
        val counterID = counterRepository.addCounter(counter, listOf(reason))
        onCounterCreated(counterID)
    }

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
