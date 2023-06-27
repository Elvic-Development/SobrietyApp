package com.orangeelephant.sobriety.ui.settings

import android.content.Context
import android.util.Base64
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.database.SqlCipherKey
import com.orangeelephant.sobriety.util.Argon2
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext app: Context
): ViewModel() {
    private val preferences = SobrietyPreferences(context = app)
    val availableLanguages = preferences.availableLanguages
    val availableThemes = preferences.availableThemes
    val encryptedWithPassword = mutableStateOf(false)

    val isEncryptingDb = mutableStateOf(false)

    init {
        viewModelScope.launch {
            preferences.encryptedByPassword.collect{ encryptedWithPassword.value = it }
        }
    }

    fun onEncryptWithPassword(context: Context, password: String) {
        viewModelScope.launch {
            isEncryptingDb.value = true

            val salt = Argon2().genSalt()

            val key = SqlCipherKey(
                isEncrypted = true,
                password = password.encodeToByteArray(),
                salt = salt
            )

            preferences.setPasswordSalt(Base64.encodeToString(salt, Base64.DEFAULT))
            preferences.setEncryptedByPassword(true)
            ApplicationDependencies.getDatabase().encrypt(context, key.keyBytes!!)
            ApplicationDependencies.setSqlcipherKey(key)

            isEncryptingDb.value = false
        }
    }

}