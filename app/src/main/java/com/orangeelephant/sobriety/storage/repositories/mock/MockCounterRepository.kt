package com.orangeelephant.sobriety.storage.repositories.mock

import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Relapse
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

    override fun getCounter(id: Long): Counter {
        return allCounters[0]
    }

    override fun getRelapsesForCounter(counterId: Long): List<Relapse> {
        return listOf(
            Relapse(1, 1, 0L, "Nope"),
            Relapse(2, 1, 0L, "big sad")
        )
    }

    override fun addCounter(counter: Counter, reasons: List<String>): Long {
        allCounters.add(counter)
        return 0L
    }

    override fun resetCounter(id: Long, comment: String?): Long {
        return 0L
    }

    override fun deleteCounter(id: Long) { }
}