package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Reason
import com.orangeelephant.sobriety.storage.models.Relapse

interface CounterRepository {
    fun getAllCounters(): List<Counter>
    fun getCounter(id: Int): Counter
    fun getRelapsesForCounter(counterId: Int): List<Relapse>
    fun getReasonsForCounter(counterId: Int): List<Reason>
    fun addCounter(counter: Counter): Int
    fun addReasonForCounter(counterId: Int, reason: String)
    fun resetCounter(id: Int, relapseTime: Long, comment: String?): Long
    fun deleteCounter(id: Int)
}