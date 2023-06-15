package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.models.Counter

class DatabaseCounterRepository: CounterRepository {
    override fun getAllCounters(): List<Counter> {
        return ApplicationDependencies.getDatabase().counters.getAllCounters()
    }
}