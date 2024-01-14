package com.orangeelephant.sobriety.ui.settings

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
            preferences.encryptedByPassword.collect { encryptedWithPassword.value = it }
        }
    }

    fun onSetPassword(context: FragmentActivity, password: String) {
        viewModelScope.launch {
            isEncryptingDb.value = true
            var biometricsEnabledPreviously: Boolean

            withContext(Dispatchers.Default) {
                biometricsEnabledPreviously = preferences.biometricUnlock.first()
                preferences.setBiometricsEnabled(false)

                setEncryptionPassword(context, preferences, password)
            }

            Toast.makeText(context, R.string.encrypted_successfully, Toast.LENGTH_LONG).show()
            isEncryptingDb.value = false

            if (biometricsEnabledPreviously) {
                onToggleFingerprint(context, true)
            }
        }
    }

    fun onDecrypt(context: Context) {
        viewModelScope.launch {
            isDecryptingDb.value = true
            disableEncryption(context, preferences)
            isDecryptingDb.value = false
        }
    }

    fun onToggleFingerprint(context: FragmentActivity, newValue: Boolean) {
        toggleBiometrics(context, preferences, viewModelScope, newValue)
    }

    fun onCancelBiometricDisable() {
        viewModelScope.launch {
            preferences.setBiometricsEnabled(true)
        }
    }
}