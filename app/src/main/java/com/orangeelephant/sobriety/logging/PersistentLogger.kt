package com.orangeelephant.sobriety.logging

import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.database.LogDatabase
import java.util.LinkedList

/**
 * A class which stores logs to the [com.orangeelephant.sobriety.storage.database.LogDatabase]
 * this should be done on a background thread to not impact app performance
 *
 * singleton as there should only be one logging thread
 */
class PersistentLogger {
    companion object {
        private val TAG = PersistentLogger::class.java.simpleName
        private const val THREAD_NAME = "logger"
    }

    private val queue: Queue = Queue()
    private val dbWriteThread = DbWriteThread()

    fun startWriteThread() {
        dbWriteThread.start()
        LogEvent.i(TAG, "Started logger thread")
    }

    fun logToDb(tag: String, message: String?, stack_trace: String?) {
        queue.addToQueue(LogRecord(tag, message, stack_trace))
    }

    private data class LogRecord(
        val tag: String,
        val message: String?,
        val stackTrace: String?
    )

    /**
     * This class is the queue of things that need to be logged, since it can be accessed
     * by more than one threads, a lock is needed
     *
     * https://stackoverflow.com/questions/26590542/java-lang-illegalmonitorstateexception-object-not-locked-by-thread-before-wait
     * information about locking was found at the above link
     */
    private inner class Queue {
        private val queue = LinkedList<LogRecord>()
        private val lock = Object()

        fun addToQueue(log: LogRecord) {
            synchronized(lock) {
                queue.add(log)
                lock.notify()
            }
        }

        val next: LogRecord
            get() {
                synchronized(lock) {
                    return try {
                        while (queue.peek() == null) {
                            lock.wait()
                        }
                        queue.remove()
                    } catch (e: InterruptedException) {
                        LogEvent.e(
                            TAG,
                            "Write thread was interrupted while waiting",
                            e
                        )
                        throw AssertionError()
                    }
                }
            }
    }

    /**
     * This thread should have super low priority because it is less important than
     * key app functionality
     */
    private inner class DbWriteThread: Thread(THREAD_NAME) {
        init {
            priority = MIN_PRIORITY
        }

        override fun run() {
            val logDatabase = LogDatabase(ApplicationDependencies.getApplicationContext())
            while (true) {
                val record: LogRecord = queue.next
                logDatabase.write(record.tag, record.message, record.stackTrace, 10)
            }
        }
    }
}