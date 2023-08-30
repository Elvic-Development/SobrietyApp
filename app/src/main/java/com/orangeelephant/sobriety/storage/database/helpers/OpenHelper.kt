package com.orangeelephant.sobriety.storage.database.helpers

import android.content.Context
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.database.migrations.V2_RecordEachRelapse
import com.orangeelephant.sobriety.storage.database.migrations.V3_RecordCreateAndInitialStartTime
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
    private val applicationContext = context.applicationContext

    companion object Constants {
        const val DATABASE_NAME = "sobriety.db"
        const val DATABASE_VERSION = 3

        const val RECORD_EACH_RELAPSE = 2
        const val RECORD_CREATE_AND_INITIAL_START_TIME = 3
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
        if (oldVersion < RECORD_CREATE_AND_INITIAL_START_TIME) {
            V3_RecordCreateAndInitialStartTime(database, applicationContext)
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