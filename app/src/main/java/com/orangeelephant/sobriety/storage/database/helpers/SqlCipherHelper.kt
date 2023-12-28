package com.orangeelephant.sobriety.storage.database.helpers

import android.content.Context
import com.orangeelephant.sobriety.logging.LogEvent
import net.sqlcipher.database.SQLiteDatabase
import java.io.File
import java.io.OutputStream

private const val TAG = "SqlCipherHelper"

fun encryptPlaintextDb(context: Context, originalFile: File, newKey: ByteArray, version: Int) {
    LogEvent.i(TAG, "Encrypting plaintext database")
    if (originalFile.exists()) {
        val tmpEncryptedDbFile = File.createTempFile("dbEncryption", "tmpEncrypted.db", context.cacheDir)
        val encryptedDb = SQLiteDatabase.openDatabase(
            tmpEncryptedDbFile.absolutePath,
            newKey,
            null,
            SQLiteDatabase.OPEN_READWRITE,
            null,
            null
        )

        replaceDatabase(
            tmpEncryptedDbFile,
            originalFile,
            encryptedDb,
            "",
            version
        )
    }
}

fun decryptEncryptedDb(context: Context, originalFile: File, currentKey: ByteArray, version: Int) {
    LogEvent.i(TAG, "Decrypting encrypted database")
    if (originalFile.exists()) {
        val tmpUnencryptedFile = File.createTempFile("dbDecryption", "tmpDecrypted.db", context.cacheDir)
        val unencryptedDb = SQLiteDatabase.openDatabase(
            tmpUnencryptedFile.absolutePath,
            "",
            null,
            SQLiteDatabase.OPEN_READWRITE,
            null,
            null
        )

        replaceDatabase(
            tmpUnencryptedFile,
            originalFile,
            unencryptedDb,
            currentKey.decodeToString(),
            version
        )
    }
}

fun exportPlaintextDatabaseFile(context: Context, originalFile: File, exportOutputStream: OutputStream, currentKey: ByteArray?, version: Int) {
    LogEvent.i(TAG, "Exporting database as plaintext")
    if (originalFile.exists()) {
        val tmpExportFile = File.createTempFile("dbExport", "tmpExport.db", context.cacheDir)
        val exportDB = SQLiteDatabase.openDatabase(
            tmpExportFile.absolutePath,
            "",
            null,
            SQLiteDatabase.OPEN_READWRITE,
            null,
            null
        )

        exportDatabase(
            originalFile,
            exportDB,
            currentKey?.decodeToString() ?: "",
            version
        )

        exportOutputStream.use { outputStream ->
            tmpExportFile.inputStream().copyTo(outputStream)
        }
        tmpExportFile.delete()
    }
}

private fun replaceDatabase(tmpDbFile: File, originalFile: File, newDb: SQLiteDatabase, oldKey: String, version: Int) {
    exportDatabase(originalFile, newDb, oldKey, version)
    originalFile.delete()
    tmpDbFile.renameTo(originalFile)
    LogEvent.i(TAG, "Original database file replaced by exported version")
}

private fun exportDatabase(originalFile: File, newDb: SQLiteDatabase, oldKey: String, version: Int) {
    val attachStatement = newDb.compileStatement(
        "ATTACH DATABASE ? AS original KEY '${oldKey}'"
    )
    attachStatement.bindString(1, originalFile.absolutePath)
    attachStatement.execute()
    newDb.rawExecSQL("SELECT sqlcipher_export('main', 'original')")
    newDb.rawExecSQL("DETACH DATABASE original")
    newDb.version = version

    attachStatement.close()
    newDb.close()

    LogEvent.i(TAG, "Database data exported to new file")
}
