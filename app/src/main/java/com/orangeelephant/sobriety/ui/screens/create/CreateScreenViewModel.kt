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
