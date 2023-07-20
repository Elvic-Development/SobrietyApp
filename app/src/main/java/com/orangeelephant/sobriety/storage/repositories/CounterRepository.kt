package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Relapse

interface CounterRepository {
    fun getAllCounters(): List<Counter>
    fun getCounter(id: Long): Counter
    fun getRelapsesForCounter(counterId: Long): List<Relapse>
    fun addCounter(counter: Counter, list: List<String>): Long
    fun resetCounter(id: Long, comment: String?): Long
    fun deleteCounter(id: Long)
}