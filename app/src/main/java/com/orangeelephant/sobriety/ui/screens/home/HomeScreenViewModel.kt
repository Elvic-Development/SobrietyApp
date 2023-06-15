package com.orangeelephant.sobriety.ui.screens.home

import androidx.lifecycle.ViewModel
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository
import com.orangeelephant.sobriety.storage.repositories.DatabaseCounterRepository

class HomeScreenViewModel(
    counterRepository: CounterRepository = DatabaseCounterRepository()
): ViewModel() {
    private val _allCounters = counterRepository.getAllCounters()
    val allCounters: List<Counter>
        get() = _allCounters
}