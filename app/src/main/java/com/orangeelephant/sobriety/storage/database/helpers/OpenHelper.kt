package com.orangeelephant.sobriety.storage.database.helpers

import android.content.Context
import com.orangeelephant.sobriety.storage.database.tables.CountersTable
import com.orangeelephant.sobriety.storage.database.tables.ReasonsTable
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

open class OpenHelper(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
) {

    companion object Constants {
        const val DATABASE_NAME = "sobriety.db"
        const val DATABASE_VERSION = 1
    }

    override fun onCreate(database: SQLiteDatabase) {
        database.execSQL(CountersTable.CREATE_TABLE_COUNTERS)
        database.execSQL(ReasonsTable.CREATE_TABLE_REASONS)
    }

    override fun onUpgrade(database: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    fun getReadableDatabase(): SQLiteDatabase? {
        //TODO implement SQLCipher optionally
        val password: ByteArray = "".toByteArray()
        return if (false /*SobrietyPreferences.getIsDatabaseEncrypted()*/) {
            super.getReadableDatabase(password)
        } else {
            super.getReadableDatabase("")
        }
    }

    fun getWritableDatabase(): SQLiteDatabase? {
        //TODO implement SQLCipher optionally
        val password: ByteArray = "".toByteArray()
        return if (false /*SobrietyPreferences.getIsDatabaseEncrypted()*/) {
            super.getWritableDatabase(password)
        } else {
            super.getWritableDatabase("")
        }
    }
}