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

    override fun resetCounter(id: Int, comment: String?): Long {
        val currentTime = Calendar.getInstance().timeInMillis
        val currentCounter = getCounter(id)
        val elapsedTime = currentTime - currentCounter.startTimeMillis
        val recordTime = if (currentCounter.recordTimeSoberInMillis < elapsedTime) {
            elapsedTime
        } else {
            currentCounter.recordTimeSoberInMillis
        }

        val sobrietyDatabase = ApplicationDependencies.getDatabase()

        sobrietyDatabase.counters.resetCounterTimer(id, recordTime)
        sobrietyDatabase.relapses.recordRelapse(id, currentTime, comment)

        return recordTime
    }

    override fun getCounter(id: Int): Counter {
        return ApplicationDependencies.getDatabase().counters.getCounterById(id)
    }
}