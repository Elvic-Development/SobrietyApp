package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Reason
import com.orangeelephant.sobriety.storage.models.Relapse

interface CounterRepository {
    fun getAllCounters(): List<Counter>
    fun getCounter(id: Int): Counter
    fun getRelapsesForCounter(counterId: Int): List<Relapse>
    fun addCounter(counter: Counter, list: List<String>): Long
    fun resetCounter(id: Int, comment: String?): Long
    fun deleteCounter(id: Int)
}