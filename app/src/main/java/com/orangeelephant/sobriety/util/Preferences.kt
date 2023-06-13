package com.orangeelephant.sobriety.util

import android.content.Context
import androidx.datastore.preferences.preferencesDataStore

private const val SHARED_PREFERENCE_FILE = "com.orangeelephant.sobriety_preferences"

val Context.preferencesDataStore by preferencesDataStore(SHARED_PREFERENCE_FILE)