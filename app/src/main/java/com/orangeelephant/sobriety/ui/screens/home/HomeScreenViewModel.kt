package com.orangeelephant.sobriety.ui.screens.home

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository
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
                for (i in 0 until _allCounters.size - 1) {
                    _allCounters[i] = updateCounterData(_allCounters[i])
                }
            }
        }
    }

    private val _allCounters = mutableStateListOf<Counter>().apply {
        addAll(counterRepository.getAllCounters())
    }
    val allCounters: SnapshotStateList<Counter>
        get() = _allCounters


    private fun updateCounterData(counter: Counter): Counter {
        var timeSoberInMillis = Calendar.getInstance().timeInMillis - counter.startTimeMillis

        val secondsInMilli: Long = 1000
        val minutesInMilli = secondsInMilli * 60
        val hoursInMilli = minutesInMilli * 60
        val daysInMilli = hoursInMilli * 24

        val elapsedDays: Long = timeSoberInMillis / daysInMilli
        timeSoberInMillis %= daysInMilli

        val elapsedHours: Long = timeSoberInMillis / hoursInMilli
        timeSoberInMillis %= hoursInMilli

        val elapsedMinutes: Long = timeSoberInMillis / minutesInMilli
        timeSoberInMillis %= minutesInMilli

        val elapsedSeconds: Long = timeSoberInMillis / secondsInMilli

        val durationString = String.format(
            ApplicationDependencies.getApplicationContext().getString(R.string.duration_string),
            elapsedDays,
            elapsedHours,
            elapsedMinutes,
            elapsedSeconds
        )

        return counter.copy(
            currentDurationString = durationString
        )
    }
}