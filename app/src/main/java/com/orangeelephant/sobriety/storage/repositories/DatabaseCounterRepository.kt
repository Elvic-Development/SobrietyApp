package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Relapse
import java.util.Calendar

class DatabaseCounterRepository: CounterRepository {
    companion object {
        private val TAG = DatabaseCounterRepository::class.java.simpleName
    }

    override fun getAllCounters(): List<Counter> {
        LogEvent.i(TAG, "Loading all counters from DB")
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

        LogEvent.i(TAG, "Counter: $id reset successfully")

        return recordTime
    }

    override fun deleteCounter(id: Int) {
        val db = ApplicationDependencies.getDatabase()

        //cleanup associated records
        db.relapses.deleteRelapsesForCounter(id)
        db.reasons.deleteReasonsForCounterId(id)

        //delete record
        db.counters.deleteCounterById(id)

        LogEvent.i(TAG, "Counter: $id and its associated records were deleted.")
    }

    override fun getCounter(id: Int): Counter {
        return ApplicationDependencies.getDatabase().counters.getCounterById(id)
    }

    override fun getRelapsesForCounter(counterId: Int): List<Relapse> {
        return ApplicationDependencies.getDatabase().relapses.getRelapsesForCounter(counterId)
    }
}