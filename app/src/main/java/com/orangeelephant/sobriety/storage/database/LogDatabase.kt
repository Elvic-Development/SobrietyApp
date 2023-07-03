package com.orangeelephant.sobriety.storage.database

import android.content.Context
import androidx.core.content.contentValuesOf
import net.sqlcipher.database.SQLiteDatabase
import net.sqlcipher.database.SQLiteOpenHelper

/**
 * A separate database file to store logs in for a longer period, eventually a full log
 * should be exportable as a file.
 */
class LogDatabase(context: Context): SQLiteOpenHelper(
    context,
    DATABASE_NAME,
    null,
    DATABASE_VERSION
){
    companion object {
        private val TAG = LogDatabase::class.java.simpleName

        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "sobriety_logs.db"

        const val TABLE_NAME = "logs"
        const val COLUMN_ID = "_id"
        const val COLUMN_TAG = "tag"
        const val COLUMN_MESSAGE = "message"
        const val COLUMN_STACK_TRACE = "stackTrace"
        const val COLUMN_KEEP_UNTIL = "keepUntil"

        const val CREATE_TABLE_LOGS = """
            CREATE TABLE IF NOT EXISTS $TABLE_NAME (
                $COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_TAG TEXT, 
                $COLUMN_MESSAGE TEXT,
                $COLUMN_STACK_TRACE TEXT DEFAULT NULL,
                $COLUMN_KEEP_UNTIL INTEGER)
            """
    }

    override fun onCreate(sqLiteDatabase: SQLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_LOGS)
    }

    override fun onUpgrade(sqLiteDatabase: SQLiteDatabase, oldVersion: Int, newVersion: Int) {}

    private val writableDatabase: SQLiteDatabase
        get() = super.getWritableDatabase("")

    fun write(tag: String, message: String?, stackTrace: String?, keepUntil: Long) {
        val contentValues = contentValuesOf(
            COLUMN_TAG to tag,
            COLUMN_MESSAGE to message,
            COLUMN_STACK_TRACE to stackTrace,
            COLUMN_KEEP_UNTIL to keepUntil
        )

        val db = writableDatabase
        db.insert(TABLE_NAME, null, contentValues)
        db.close()
    }
}