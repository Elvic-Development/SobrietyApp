package com.orangeelephant.sobriety.storage.repositories.mock

import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository

class MockCounterRepository: CounterRepository {
    private val allCounters = mutableListOf(
        Counter(1, "GREAT", 0L, 0L, "Duration string here"),
        Counter(2, "Bad", 0L, 0L, "Duration string here"),
        Counter(3, "afdsgag", 0L, 0L, "Duration string here"),
        Counter(4, "afdgd", 0L, 0L, "Duration string here"),
    )

    override fun getAllCounters(): List<Counter> {
        return allCounters
    }

    override fun getCounter(id: Int): Counter {
        return Counter(1, "GREAT", 0L, 0L)
    }

    override fun addCounter(counter: Counter) {
        allCounters.add(counter)
    }

    override fun resetCounter(id: Int, comment: String?): Long {
        return 0L
    }

    override fun deleteCounter(id: Int) { }
}