package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository

class CounterFullScreenViewModel(
    counterId: Int,
    counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    private val _counter = mutableStateOf(
        counterRepository.getCounter(counterId)
    )
    val counter: MutableState<Counter>
        get() = _counter
}