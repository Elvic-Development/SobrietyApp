package com.orangeelephant.sobriety.storage.database.migrations

import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.database.tables.RelapsesTable
import net.sqlcipher.database.SQLiteDatabase

fun V4_SupportBackdatingRelapses(database: SQLiteDatabase) {
    val tag = "V4_SupportBackdatingRelapses"

    LogEvent.i(tag, "Running V4 migration to alter relapses table")

    database.execSQL("""
        ALTER TABLE ${RelapsesTable.TABLE_NAME_RELAPSES} 
        ADD ${RelapsesTable.COLUMN_RECORDED_AT_TIME} INTEGER
    """)
    database.execSQL("""
        UPDATE ${RelapsesTable.TABLE_NAME_RELAPSES}
        SET ${RelapsesTable.COLUMN_RECORDED_AT_TIME} = ${RelapsesTable.COLUMN_RELAPSE_TIME}
    """)
}