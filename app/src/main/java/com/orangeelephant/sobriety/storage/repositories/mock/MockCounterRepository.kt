package com.orangeelephant.sobriety.storage.repositories.mock

import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.CounterRepository

class MockCounterRepository: CounterRepository {
    override fun getAllCounters(): List<Counter> {
        return listOf(
            Counter(1, "GREAT", 0L, 0L),
            Counter(2, "Bad", 0L, 0L),
            Counter(3, "afdsgag", 0L, 0L),
            Counter(4, "afdgd", 0L, 0L),
        )
    }
}