package com.orangeelephant.sobriety.storage.database.helpers

import android.content.Context
import net.sqlcipher.database.SQLiteDatabase
import java.io.File

fun encryptPlaintextDb(context: Context, originalFile: File, newKey: ByteArray, version: Int) {
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

        exportDatabase(
            tmpEncryptedDbFile,
            originalFile,
            encryptedDb,
            "",
            version
        )
    }
}

fun decryptEncryptedDb(context: Context, originalFile: File, currentKey: ByteArray, version: Int) {
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

        exportDatabase(
            tmpUnencryptedFile,
            originalFile,
            unencryptedDb,
            currentKey.decodeToString(),
            version
        )
    }
}

private fun exportDatabase(tmpDbFile: File, originalFile: File, newDb: SQLiteDatabase, oldKey: String, version: Int) {
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
    originalFile.delete()

    tmpDbFile.renameTo(originalFile)
}
