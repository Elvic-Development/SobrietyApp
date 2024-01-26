package com.orangeelephant.sobriety.ui.screens.createcounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository


class CreateCounterScreenViewModel(
    private val counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    var nameText: String by mutableStateOf("")
    var dateVal: Long by mutableLongStateOf(System.currentTimeMillis())

    fun onCreateCounter(
        name: String,
        startTimeMillis: Long,
        reasons: List<TextFieldValue>,
        onCounterCreated: (counterID: Int) -> Unit
    ) {
        val newCounter = Counter(
            -1,
            name,
            startTimeMillis,
            0L,
            startTimeMillis,
            System.currentTimeMillis()
        )

        val counterId = counterRepository.addCounter(newCounter)
        for (reason in reasons) {
            counterRepository.addReasonForCounter(counterId, reason.text)
        }

        onCounterCreated(counterId)
    }

}