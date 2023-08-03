package com.orangeelephant.sobriety.storage.database.migrations

import android.content.Context
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.database.tables.CountersTable
import com.orangeelephant.sobriety.storage.database.tables.RelapsesTable
import com.orangeelephant.sobriety.util.getPackageInfo
import net.sqlcipher.Cursor
import net.sqlcipher.database.SQLiteDatabase

fun V3_RecordCreateAndInitialStartTime(database: SQLiteDatabase, context: Context) {
    val tag = "V3_RecordCreateAndInitialStartTime"

    LogEvent.i(tag, "Running V3 migration")
    database.execSQL("""
        ALTER TABLE ${CountersTable.TABLE_NAME_COUNTERS} 
        ADD ${CountersTable.COLUMN_INITIAL_START_TIME} INTEGER DEFAULT NULL
    """)

    database.execSQL("""
        ALTER TABLE ${CountersTable.TABLE_NAME_COUNTERS} 
        ADD ${CountersTable.COLUMN_CREATION_TIMESTAMP} INTEGER DEFAULT NULL
    """)


    // update creation timestamp to more recent of app install time or initial start time calculated
    val appInstallTime = context.getPackageInfo().firstInstallTime

    val selectInfoForAllCountersSql = """
        SELECT 
            ${CountersTable.COLUMN_ID},
            ${CountersTable.COLUMN_CURRENT_START_TIME},
            ${CountersTable.COLUMN_RECORD_CLEAN_TIME},
            (SELECT COUNT(*) FROM relapses 
             WHERE ${RelapsesTable.TABLE_NAME_RELAPSES}.${RelapsesTable.COLUMN_ASSOCIATED_COUNTER} = 
                   ${CountersTable.TABLE_NAME_COUNTERS}.${CountersTable.COLUMN_ID}
             )
        FROM ${CountersTable.TABLE_NAME_COUNTERS}
    """

    val cursor: Cursor = database.rawQuery(selectInfoForAllCountersSql, null)
    while (cursor.moveToNext()) {
        val id: Int = cursor.getInt(0)
        val currentStartTime: Long = cursor.getLong(1)
        val recordTimeClean: Long = cursor.getLong(2)
        val numRelapses: Int = cursor.getInt(3)

        // update Initial start time to current start time if no relapses, current start minus record
        // time if 1 relapse, null otherwise
        val initialStartTime: Long? = when (numRelapses) {
            0 -> currentStartTime
            1 -> currentStartTime - recordTimeClean
            else -> null
        }

        val creationTimestamp = if (initialStartTime != null && initialStartTime > appInstallTime) {
            initialStartTime
        } else {
            appInstallTime
        }

        database.execSQL("""
            UPDATE ${CountersTable.TABLE_NAME_COUNTERS}
            SET 
                ${CountersTable.COLUMN_INITIAL_START_TIME} = $initialStartTime,
                ${CountersTable.COLUMN_CREATION_TIMESTAMP} = $creationTimestamp
            WHERE ${CountersTable.COLUMN_ID} = $id
        """)
    }
    cursor.close()
    LogEvent.i(tag, "Updated values for creation and initial start times successfully")
}
