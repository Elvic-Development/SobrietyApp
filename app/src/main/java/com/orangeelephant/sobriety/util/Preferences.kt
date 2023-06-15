package com.orangeelephant.sobriety.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCE_FILE = "com.orangeelephant.sobriety_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCE_FILE)

class SobrietyPreferences(preferenceDataStore: DataStore<Preferences>) {
    companion object {
        const val DYNAMIC_COLOURS = "dynamic_colours"
        const val THEME = "theme"
        const val LANGUAGE = "language"
    }

    val dynamicColours: Flow<Boolean> = preferenceDataStore.data.map {preferences ->
        preferences[booleanPreferencesKey(DYNAMIC_COLOURS)] ?: true
    }

    val theme: Flow<String> = preferenceDataStore.data.map {preferences ->
        preferences[stringPreferencesKey(THEME)] ?: "default"
    }

    val language: Flow<String> = preferenceDataStore.data.map {preferences ->
        preferences[stringPreferencesKey(LANGUAGE)] ?: "default"
    }

}