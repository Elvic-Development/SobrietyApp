package com.orangeelephant.sobriety.storage.database.tables

import androidx.core.content.contentValuesOf
import com.orangeelephant.sobriety.storage.database.helpers.OpenHelper
import com.orangeelephant.sobriety.storage.models.Reason
import net.sqlcipher.Cursor
import net.sqlcipher.database.SQLiteDatabase


class ReasonsTable(private val openHelper: OpenHelper) {
    companion object {
        private const val TABLE_NAME_REASONS = "reasons"

        private const val COLUMN_ID = "_id"
        private const val COLUMN_COUNTER_ID = "counter_id"
        private const val COLUMN_SOBRIETY_REASON = "sobriety_reason"

        const val CREATE_TABLE_REASONS = """CREATE TABLE IF NOT EXISTS $TABLE_NAME_REASONS (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_COUNTER_ID INTEGER,
                $COLUMN_SOBRIETY_REASON TEXT DEFAULT NULL,
                FOREIGN KEY ($COLUMN_COUNTER_ID) REFERENCES 
                    ${CountersTable.TABLE_NAME_COUNTERS} ($COLUMN_ID))
            """
    }

    fun getReasonsForCounter(counterId: Int): ArrayList<Reason> {
        val db: SQLiteDatabase = openHelper.getReadableDatabase()
        val reasons: ArrayList<Reason> = ArrayList()
        val reasonSql = "SELECT * FROM reasons WHERE $COLUMN_COUNTER_ID = $counterId"
        val reasonsCursor: Cursor = db.rawQuery(reasonSql, null)

        if (reasonsCursor.count > 0) {
            while (reasonsCursor.moveToNext()) {
                val reasonId: Int = reasonsCursor.getInt(0)
                val counterId: Int = reasonsCursor.getInt(1)
                val sobrietyReason: String = reasonsCursor.getString(2)

                reasons.add(Reason(reasonId, counterId, sobrietyReason))
            }
        } else {
            //LogEvent.i(TAG, "Counter id $counterId has no associated sobriety reasons.")
        }
        reasonsCursor.close()
        return reasons
    }

    fun deleteReasonsForCounterId(counterId: Int) {
        val sqlReasonRecords = """DELETE FROM $TABLE_NAME_REASONS 
                                  WHERE $COLUMN_COUNTER_ID = $counterId"""

        val db: SQLiteDatabase = openHelper.getWritableDatabase()
        db.execSQL(sqlReasonRecords)
    }

    fun addReasonForCounter(counterId: Int, reason: String) {
        val db: SQLiteDatabase = openHelper.getWritableDatabase()
        val contentValues = contentValuesOf(
            COLUMN_COUNTER_ID to counterId,
            COLUMN_SOBRIETY_REASON to reason
        )
        db.insert(TABLE_NAME_REASONS, null, contentValues)
    }

    fun changeReason(reasonId: Int, reason: String) {
        val db: SQLiteDatabase = openHelper.getWritableDatabase()
        val sql = """UPDATE $TABLE_NAME_REASONS
                     SET $COLUMN_SOBRIETY_REASON = '$reason' 
                     WHERE $COLUMN_ID = $reasonId
                  """

        db.execSQL(sql)
    }
}