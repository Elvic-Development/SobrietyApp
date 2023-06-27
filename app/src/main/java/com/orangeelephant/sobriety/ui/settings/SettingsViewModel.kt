package com.orangeelephant.sobriety.ui.settings

import android.content.Context
import android.util.Base64
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R
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
    companion object {
        private val TAG = SettingsViewModel::class.java.simpleName
    }

    private val preferences = SobrietyPreferences(context = app)
    val availableLanguages = preferences.availableLanguages
    val availableThemes = preferences.availableThemes
    val encryptedWithPassword = mutableStateOf(false)

    val isEncryptingDb = mutableStateOf(false)
    val isDecryptingDb = mutableStateOf(false)

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

            Toast.makeText(context, R.string.encrypted_successfully, Toast.LENGTH_LONG).show()
            isEncryptingDb.value = false
        }
    }

    fun onDecrypt(context: Context) {
        viewModelScope.launch {
            isDecryptingDb.value = true

            val currentKey = ApplicationDependencies.getSqlCipherKey()
            currentKey.keyBytes?.let {
                ApplicationDependencies.getDatabase().decrypt(context, it)

                preferences.setPasswordSalt("")
                preferences.setEncryptedByPassword(false)
                ApplicationDependencies.setSqlcipherKey(SqlCipherKey(isEncrypted = false))

                Toast.makeText(context, R.string.decrypted_successfully, Toast.LENGTH_LONG).show()
            } ?: run {
                Toast.makeText(context, R.string.decrypt_fail_current_key_null, Toast.LENGTH_LONG).show()
            }

            isDecryptingDb.value = false
        }
    }
}