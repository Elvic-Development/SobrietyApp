package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository
import com.orangeelephant.sobriety.util.CounterViewUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar

class CounterFullScreenViewModel(
    private val counterId: Int,
    private val counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _counter.value = _counter.value.copy(
                    currentDurationString = CounterViewUtil.formatDurationAsString(
                        Calendar.getInstance().timeInMillis - _counter.value.startTimeMillis
                    )
                )
            }
        }
    }

    private val _counter = mutableStateOf(
        counterRepository.getCounter(counterId)
    )
    val counter: MutableState<Counter>
        get() = _counter


    fun onResetCounter() {
        val startTimeMillis = Calendar.getInstance().timeInMillis
        val newRecord = counterRepository.resetCounter(counterId)
        _counter.value = _counter.value.copy(
            startTimeMillis = startTimeMillis,
            recordTimeSoberInMillis = newRecord,
            currentDurationString = CounterViewUtil.formatDurationAsString(startTimeMillis)
        )
    }
}