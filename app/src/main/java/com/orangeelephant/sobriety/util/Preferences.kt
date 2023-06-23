package com.orangeelephant.sobriety.util

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.orangeelephant.sobriety.R
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREFERENCE_FILE = "com.orangeelephant.sobriety_preferences"

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(PREFERENCE_FILE)

class SobrietyPreferences(context: Context) {
    private val preferenceDataStore = context.dataStore
    companion object {
        const val DYNAMIC_COLOURS = "dynamic_colours"
        const val THEME = "theme"
        const val LANGUAGE = "language"

        const val BIOMETRIC_UNLOCK = "biometric_unlock"
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

    val biometricUnlock: Flow<Boolean> = preferenceDataStore.data.map { preferences ->
        preferences[booleanPreferencesKey(BIOMETRIC_UNLOCK)] ?: false
    }

    private val _availableLanguages = mapOf(
        "default" to context.getString(R.string.system_default),
        "en" to "English"
    )
    val availableLanguages: Map<String, String>
        get() = _availableLanguages

    private val _availableThemes = mapOf(
        "default" to context.getString(R.string.system_default),
        "light" to context.getString(R.string.light_mode),
        "dark" to context.getString(R.string.dark_mode)
    )
    val availableThemes: Map<String, String>
        get() = _availableThemes
}