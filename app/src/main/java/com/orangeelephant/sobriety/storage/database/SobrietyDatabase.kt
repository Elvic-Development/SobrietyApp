package com.orangeelephant.sobriety.storage.database

import android.content.Context
import com.orangeelephant.sobriety.storage.database.helpers.OpenHelper
import com.orangeelephant.sobriety.storage.database.tables.CountersTable
import com.orangeelephant.sobriety.storage.database.tables.ReasonsTable

class SobrietyDatabase(context: Context) {
    private val openHelper: OpenHelper = OpenHelper(context)

    val counters: CountersTable = CountersTable(openHelper)
    val reasons: ReasonsTable = ReasonsTable(openHelper)
}