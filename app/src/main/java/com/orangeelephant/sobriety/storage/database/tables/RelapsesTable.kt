package com.orangeelephant.sobriety.storage.database.tables

import androidx.core.content.contentValuesOf
import androidx.core.database.getStringOrNull
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.database.helpers.OpenHelper
import com.orangeelephant.sobriety.storage.models.Relapse
import net.sqlcipher.Cursor
import net.sqlcipher.database.SQLiteDatabase

class RelapsesTable(private val openHelper: OpenHelper) {
    companion object {
        private val TAG = RelapsesTable::class.java.simpleName

        const val TABLE_NAME_RELAPSES = "relapses"

        private const val COLUMN_ID = "_id"
        const val COLUMN_ASSOCIATED_COUNTER = "associated_counter"
        const val COLUMN_RELAPSE_TIME = "relapse_time_unix_millis"
        const val COLUMN_COMMENTS = "comments"
        const val COLUMN_RECORDED_AT_TIME = "recorded_at_time_millis"

        const val CREATE_TABLE_RELAPSES = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME_RELAPSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ASSOCIATED_COUNTER INTEGER,
                $COLUMN_RELAPSE_TIME INTEGER,
                $COLUMN_COMMENTS TEXT DEFAULT NULL,
                $COLUMN_RECORDED_AT_TIME INTEGER,
                FOREIGN KEY (${COLUMN_ASSOCIATED_COUNTER}) REFERENCES 
                    ${CountersTable.TABLE_NAME_COUNTERS}(${COLUMN_ID})
            )
            """
    }

    fun recordRelapse(associatedCounterId: Int, time: Long, comment: String?): Long {
        val db = openHelper.getWritableDatabase()
        val contentValues = contentValuesOf(
            COLUMN_ASSOCIATED_COUNTER to associatedCounterId,
            COLUMN_RELAPSE_TIME to time,
            COLUMN_COMMENTS to comment,
            COLUMN_RECORDED_AT_TIME to System.currentTimeMillis()
        )

        return db.insert(TABLE_NAME_RELAPSES, null, contentValues)
    }

    fun getRelapsesForCounter(counterId: Int): List<Relapse> {
        val db: SQLiteDatabase = openHelper.getReadableDatabase()
        val relapses: ArrayList<Relapse> = ArrayList()
        val relapseSql = """
            SELECT 
                $COLUMN_ID,
                $COLUMN_ASSOCIATED_COUNTER,
                $COLUMN_RELAPSE_TIME,
                $COLUMN_COMMENTS,
                $COLUMN_RECORDED_AT_TIME
            FROM $TABLE_NAME_RELAPSES 
            WHERE $COLUMN_ASSOCIATED_COUNTER = $counterId
            ORDER BY $COLUMN_RELAPSE_TIME DESC
            """

        val cursor: Cursor = db.rawQuery(relapseSql, null)

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id: Int = cursor.getInt(0)
                val assocCounter: Int = cursor.getInt(1)
                val relapseTime: Long = cursor.getLong(2)
                val comment: String? = cursor.getStringOrNull(3)
                val recordedAtTime: Long = cursor.getLong(4)

                relapses.add(Relapse(id, assocCounter, relapseTime, comment, recordedAtTime))
            }
        } else {
            LogEvent.i(TAG, "Counter id $counterId has no associated relapses.")
        }
        cursor.close()

        return relapses
    }

    fun deleteRelapsesForCounter(counterId: Int) {
        val deleteRecordsSql = """DELETE FROM $TABLE_NAME_RELAPSES 
                                  WHERE $COLUMN_ASSOCIATED_COUNTER = $counterId"""

        val db: SQLiteDatabase = openHelper.getWritableDatabase()
        db.execSQL(deleteRecordsSql)
    }
}