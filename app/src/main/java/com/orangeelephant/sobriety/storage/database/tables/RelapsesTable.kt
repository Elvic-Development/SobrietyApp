package com.orangeelephant.sobriety.storage.database.tables

import androidx.core.content.contentValuesOf
import com.orangeelephant.sobriety.storage.database.helpers.OpenHelper

class RelapsesTable(private val openHelper: OpenHelper) {
    companion object {
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
}