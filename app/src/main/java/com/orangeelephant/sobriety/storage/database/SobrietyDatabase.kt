package com.orangeelephant.sobriety.storage.database

import android.content.Context
import com.orangeelephant.sobriety.storage.database.helpers.OpenHelper
import com.orangeelephant.sobriety.storage.database.helpers.decryptEncryptedDb
import com.orangeelephant.sobriety.storage.database.helpers.encryptPlaintextDb
import com.orangeelephant.sobriety.storage.database.tables.CountersTable
import com.orangeelephant.sobriety.storage.database.tables.ReasonsTable
import com.orangeelephant.sobriety.storage.database.tables.RelapsesTable
import java.io.File

class SobrietyDatabase(context: Context) {
    private val openHelper: OpenHelper = OpenHelper(context)

    val counters: CountersTable = CountersTable(openHelper)
    val reasons: ReasonsTable = ReasonsTable(openHelper)
    val relapses: RelapsesTable = RelapsesTable(openHelper)

    fun encrypt(context: Context, newKey: ByteArray) {
        val database = openHelper.getReadableDatabase("")
        val originalFile = File(database.path)
        val version = database.version
        database.close()

        encryptPlaintextDb(context, originalFile, newKey, version)
    }

    fun decrypt(context: Context, oldKey: ByteArray) {
        val database = openHelper.getReadableDatabase(oldKey)
        val originalFile = File(database.path)
        val version = database.version
        database.close()

        decryptEncryptedDb(context, originalFile, oldKey, version)
    }
}