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
        private const val COLUMN_ASSOCIATED_COUNTER = "associated_counter"
        private const val COLUMN_RELAPSE_TIME = "relapse_time_unix_millis"
        private const val COLUMN_COMMENTS = "comments"

        const val CREATE_TABLE_RELAPSES = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME_RELAPSES (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ASSOCIATED_COUNTER INTEGER,
                $COLUMN_RELAPSE_TIME INTEGER,
                $COLUMN_COMMENTS TEXT DEFAULT NULL,
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
            COLUMN_COMMENTS to comment
        )

        return db.insert(TABLE_NAME_RELAPSES, null, contentValues)
    }

    fun getRelapsesForCounter(counterId: Int): List<Relapse> {
        val db: SQLiteDatabase = openHelper.getReadableDatabase()
        val relapses: ArrayList<Relapse> = ArrayList()
        val relapseSql = """
            SELECT * FROM $TABLE_NAME_RELAPSES 
            WHERE $COLUMN_ASSOCIATED_COUNTER = $counterId
            """

        val cursor: Cursor = db.rawQuery(relapseSql, null)

        if (cursor.count > 0) {
            while (cursor.moveToNext()) {
                val id: Int = cursor.getInt(0)
                val assocCounter: Int = cursor.getInt(1)
                val time: Long = cursor.getLong(2)
                val comment: String? = cursor.getStringOrNull(3)

                relapses.add(Relapse(id, assocCounter, time, comment))
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