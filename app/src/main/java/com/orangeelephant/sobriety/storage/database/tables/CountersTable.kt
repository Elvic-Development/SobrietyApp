package com.orangeelephant.sobriety.storage.database.tables

import androidx.core.content.contentValuesOf
import com.orangeelephant.sobriety.storage.database.helpers.OpenHelper
import com.orangeelephant.sobriety.storage.models.Counter
import net.sqlcipher.Cursor
import net.sqlcipher.CursorIndexOutOfBoundsException
import net.sqlcipher.database.SQLiteDatabase
import java.util.*
import kotlin.collections.ArrayList


class CountersTable(private val openHelper: OpenHelper) {
    companion object {
        const val TABLE_NAME_COUNTERS = "counters"

        const val COLUMN_ID = "_id"
        const val COLUMN_NAME = "name"
        const val COLUMN_CURRENT_START_TIME = "start_time_unix_millis"
        const val COLUMN_RECORD_CLEAN_TIME = "record_time_clean"
        const val COLUMN_INITIAL_START_TIME = "initial_start_time"
        const val COLUMN_CREATION_TIMESTAMP = "creation_timestamp"

        const val CREATE_TABLE_COUNTERS = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME_COUNTERS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_NAME TEXT,
                $COLUMN_CURRENT_START_TIME INTEGER,
                $COLUMN_RECORD_CLEAN_TIME INTEGER DEFAULT 0,
                $COLUMN_INITIAL_START_TIME INTEGER,
                $COLUMN_CREATION_TIMESTAMP INTEGER
            )
            """
    }

    fun getCounterById(counterId: Int): Counter {
        val db: SQLiteDatabase = openHelper.getReadableDatabase()
        val sql = """SELECT 
                        $COLUMN_ID,
                        $COLUMN_NAME,
                        $COLUMN_CURRENT_START_TIME,
                        $COLUMN_RECORD_CLEAN_TIME,
                        $COLUMN_INITIAL_START_TIME,
                        $COLUMN_CREATION_TIMESTAMP
                     FROM $TABLE_NAME_COUNTERS 
                     WHERE $COLUMN_ID = $counterId
                     """

        val cursor: Cursor = db.rawQuery(sql, null)
        if (cursor.count == 0) {
            throw CursorIndexOutOfBoundsException("No counter found with the provided id $counterId")
        }

        cursor.moveToFirst()
        val name: String = cursor.getString(1)
        val time: Long = cursor.getLong(2)
        val recordtime: Long = cursor.getLong(3)
        val initialStartTime: Long = cursor.getLong(4)
        val creationTimestamp: Long = cursor.getLong(5)
        cursor.close()

        return Counter(counterId, name, time, recordtime, initialStartTime, creationTimestamp)
    }

    fun getAllCounters(): List<Counter> {
        val db: SQLiteDatabase = openHelper.getReadableDatabase()
        val sql = """
                     SELECT 
                        $COLUMN_ID,
                        $COLUMN_NAME,
                        $COLUMN_CURRENT_START_TIME,
                        $COLUMN_RECORD_CLEAN_TIME,
                        $COLUMN_INITIAL_START_TIME,
                        $COLUMN_CREATION_TIMESTAMP
                     FROM $TABLE_NAME_COUNTERS
                     ORDER by $COLUMN_CURRENT_START_TIME ASC
                  """

        val cursor: Cursor = db.rawQuery(sql, null)
        val counters: ArrayList<Counter> = ArrayList(cursor.count)

        while (cursor.moveToNext()) {
            val id: Int = cursor.getInt(0)
            val name: String = cursor.getString(1)
            val time: Long = cursor.getLong(2)
            val recordtime: Long = cursor.getLong(3)
            val initialStartTime: Long = cursor.getLong(4)
            val creationTimestamp: Long = cursor.getLong(5)

            counters.add(Counter(id, name, time, recordtime, initialStartTime, creationTimestamp))
        }
        cursor.close()

        return counters
    }

    fun resetCounterTimer(counterId: Int, recordTime: Long) {
        val timeNow: Long = Date().time
        val sql = """
            UPDATE $TABLE_NAME_COUNTERS
            SET $COLUMN_RECORD_CLEAN_TIME = $recordTime, $COLUMN_CURRENT_START_TIME = $timeNow
            WHERE $COLUMN_ID = $counterId
            """

        val db: SQLiteDatabase = openHelper.getWritableDatabase()
        db.execSQL(sql)
    }

    fun deleteCounterById(counterId: Int) {
        val deleteRecordSql = """DELETE FROM $TABLE_NAME_COUNTERS 
                                 WHERE $COLUMN_ID = $counterId"""
        openHelper.getWritableDatabase().execSQL(deleteRecordSql)
    }


    /**
     * used to save a counter object to the database
     * currently used when creating new counters
     *
     * @param counterToSave the counter object to be saved
     */
    fun saveCounterObjectToDb(counterToSave: Counter): Long {
        val db: SQLiteDatabase = openHelper.getWritableDatabase()
        val contentValues = contentValuesOf(
            COLUMN_NAME to counterToSave.name,
            COLUMN_CURRENT_START_TIME to counterToSave.startTimeMillis,
            COLUMN_RECORD_CLEAN_TIME to counterToSave.recordTimeSoberInMillis
        )

        return db.insert(TABLE_NAME_COUNTERS, null, contentValues)
    }
}