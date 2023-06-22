package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.storage.models.Counter

interface CounterRepository {
    fun getAllCounters(): List<Counter>
    fun getCounter(id: Int): Counter
    fun addCounter(counter: Counter)
}