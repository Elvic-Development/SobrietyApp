package com.orangeelephant.sobriety.storage.database.helpers

import android.content.Context
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.database.migrations.V2_RecordEachRelapse
import com.orangeelephant.sobriety.storage.database.tables.CountersTable
import com.orangeelephant.sobriety.storage.database.tables.ReasonsTable
import com.orangeelephant.sobriety.storage.database.tables.RelapsesTable
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

open class OpenHelper(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object Constants {
        const val DATABASE_NAME = "sobriety.db"
        const val DATABASE_VERSION = 2

        const val RECORD_EACH_RELAPSE = 2
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CountersTable.CREATE_TABLE_COUNTERS)
        database.execSQL(ReasonsTable.CREATE_TABLE_REASONS)
        database.execSQL(RelapsesTable.CREATE_TABLE_RELAPSES)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        if (oldVersion < RECORD_EACH_RELAPSE) {
            V2_RecordEachRelapse(database)
        }
    }

    fun getReadableDatabase(): SQLiteDatabase {
        return super.getReadableDatabase(ApplicationDependencies.getSqlCipherKey().keyBytes)
    }

    fun getWritableDatabase(): SQLiteDatabase {
        return super.getWritableDatabase(ApplicationDependencies.getSqlCipherKey().keyBytes)
    }

    fun reKey(oldKey: String, newKey: String) {
        val db = getWritableDatabase(oldKey)
        db.changePassword(newKey)
        db.close()
    }
}