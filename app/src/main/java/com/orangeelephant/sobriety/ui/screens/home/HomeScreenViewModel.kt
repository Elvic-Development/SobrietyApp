package com.orangeelephant.sobriety.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository import com.orangeelephant.sobriety.util.CounterViewUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Calendar


class HomeScreenViewModel(
    counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                for (i in 0 until _allCounters.size) {
                    val counter = _allCounters[i]
                    _allCounters[i] = counter.copy(
                        currentDurationString = CounterViewUtil.formatDurationAsString(
                            Calendar.getInstance().timeInMillis - counter.startTimeMillis
                        )
                    )
                }
            }
        }
    }

    private val _allCounters = mutableStateListOf<Counter>().apply {
        addAll(counterRepository.getAllCounters())
    }
    val allCounters: SnapshotStateList<Counter>
        get() = _allCounters

}