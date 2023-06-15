package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.storage.models.Counter

interface CounterRepository {
    fun getAllCounters(): List<Counter>
}