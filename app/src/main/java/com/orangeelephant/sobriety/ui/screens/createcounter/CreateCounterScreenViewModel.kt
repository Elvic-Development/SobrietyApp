package com.orangeelephant.sobriety.ui.screens.createcounter

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository


class CreateCounterScreenViewModel(
    private val counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    var nameText: String by mutableStateOf("")
    var dateVal: Long by mutableLongStateOf(System.currentTimeMillis())
    var reasonText: String by mutableStateOf("")
    var reasonList: List<String> by mutableStateOf(emptyList())

    fun onCreateCounter(
        name: String,
        startTimeMillis: Long,
        reason: String,
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
        counterRepository.addReasonForCounter(counterId, reason)

        onCounterCreated(counterId)
    }

}