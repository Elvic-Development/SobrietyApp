package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.models.Counter

class DatabaseCounterRepository: CounterRepository {
    override fun getAllCounters(): List<Counter> {
        return ApplicationDependencies.getDatabase().counters.getAllCounters()
    }

    override fun addCounter(counter: Counter) {
        ApplicationDependencies.getDatabase().counters.saveCounterObjectToDb(counter)
    }

    override fun getCounter(id: Int): Counter {
        return ApplicationDependencies.getDatabase().counters.getCounterById(id)
    }
}