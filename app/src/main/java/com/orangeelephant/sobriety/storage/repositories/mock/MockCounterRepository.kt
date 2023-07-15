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

    override fun getCounter(id: Int): Counter {
        return allCounters[0]
    }

    override fun getRelapsesForCounter(counterId: Int): List<Relapse> {
        return listOf(
            Relapse(1, 1, 0L, "Nope"),
            Relapse(2, 1, 0L, "big sad")
        )
    }

    override fun addCounter(counter: Counter) {
        allCounters.add(counter)
    }

    override fun resetCounter(id: Int, comment: String?): Long {
        return 0L
    }

    override fun deleteCounter(id: Int) { }
}