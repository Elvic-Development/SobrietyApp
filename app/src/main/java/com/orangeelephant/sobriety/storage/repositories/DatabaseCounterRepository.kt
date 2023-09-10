package com.orangeelephant.sobriety.storage.repositories

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.models.Reason
import com.orangeelephant.sobriety.storage.models.Relapse

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

    override fun addReasonForCounter(counterId: Int, reason: String) {
        ApplicationDependencies.getDatabase().reasons.addReasonForCounter(counterId, reason)
    }

    override fun resetCounter(id: Int, relapseTime: Long, comment: String?): Long {
        val db = ApplicationDependencies.getDatabase()
        db.relapses.recordRelapse(id, relapseTime, comment)

        // since relapses may not be inserted chronologically this info must be calculated
        val newStartTime = db.counters.getMostRecentRelapseTime(id)
        val recordTime = db.counters.calculateRecordTimeFromRelapseData(id)

        db.counters.updateCounterTimer(id, newStartTime, recordTime)

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

    override fun getReasonsForCounter(counterId: Int): List<Reason> {
        return ApplicationDependencies.getDatabase().reasons.getReasonsForCounter(counterId)
    }
}