package com.orangeelephant.sobriety.storage.database.migrations

import com.orangeelephant.sobriety.storage.database.tables.RelapsesTable
import net.sqlcipher.database.SQLiteDatabase

fun V2_RecordEachRelapse(database: SQLiteDatabase) {
    database.execSQL(RelapsesTable.CREATE_TABLE_RELAPSES)
}