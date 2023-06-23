package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.models.Counter
import java.util.Calendar

class DatabaseCounterRepository: CounterRepository {
    override fun getAllCounters(): List<Counter> {
        return ApplicationDependencies.getDatabase().counters.getAllCounters()
    }

    override fun addCounter(counter: Counter) {
        ApplicationDependencies.getDatabase().counters.saveCounterObjectToDb(counter)
    }

    override fun resetCounter(id: Int): Long {
        val currentCounter = getCounter(id)
        val elapsedTime = Calendar.getInstance().timeInMillis - currentCounter.startTimeMillis
        val recordTime = if (currentCounter.recordTimeSoberInMillis < elapsedTime) {
            elapsedTime
        } else {
            currentCounter.recordTimeSoberInMillis
        }

        ApplicationDependencies.getDatabase().counters.resetCounterTimer(id, recordTime)

        return recordTime
    }

    override fun getCounter(id: Int): Counter {
        return ApplicationDependencies.getDatabase().counters.getCounterById(id)
    }
}