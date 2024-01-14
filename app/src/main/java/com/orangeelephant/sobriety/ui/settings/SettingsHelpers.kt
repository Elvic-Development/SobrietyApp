package com.orangeelephant.sobriety.ui.settings

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.util.Base64
import android.widget.Toast
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.storage.database.SqlCipherKey
import com.orangeelephant.sobriety.util.Argon2
import com.orangeelephant.sobriety.util.KeyStoreHelper
import com.orangeelephant.sobriety.util.SobrietyPreferences
import com.orangeelephant.sobriety.util.canEnableAuthentication
import com.orangeelephant.sobriety.util.showBiometricPrompt
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Functions which change certain settings, to allow the settings to be enabled from
 * multiple different view models, eg. settings screen and initial setup screen
 */

private val TAG = SettingsViewModel::class.java.simpleName

suspend fun setEncryptionPassword(
    context: Context,
    preferences: SobrietyPreferences,
    password: String
) {
    val salt = Argon2().genSalt()

    val newKey = SqlCipherKey(
        isEncrypted = true,
        password = password.encodeToByteArray(),
        salt = salt
    )

    preferences.setPasswordSalt(Base64.encodeToString(salt, Base64.DEFAULT))

    // re-key encrypted db
    if (preferences.encryptedByPassword.first()) {
        val currentKey = ApplicationDependencies.getSqlCipherKey().keyBytes!!
        ApplicationDependencies.getDatabase().changeKey(currentKey, newKey.keyBytes!!)
    } else {
        preferences.setEncryptedByPassword(true)
        ApplicationDependencies.getDatabase().encrypt(context, newKey.keyBytes!!)
    }

    ApplicationDependencies.setSqlcipherKey(newKey)
}

suspend fun disableEncryption(context: Context, preferences: SobrietyPreferences) {
    val currentKey = ApplicationDependencies.getSqlCipherKey()
    currentKey.keyBytes?.let {
        withContext(Dispatchers.Default) {
            ApplicationDependencies.getDatabase().decrypt(context, it)

            preferences.setPasswordSalt("")
            preferences.setEncryptedByPassword(false)
            preferences.setKeystoreEncryptedKey(null)
            ApplicationDependencies.setSqlcipherKey(SqlCipherKey(isEncrypted = false))
        }

        Toast.makeText(context, R.string.decrypted_successfully, Toast.LENGTH_LONG).show()
    } ?: run {
        Toast.makeText(context, R.string.decrypt_fail_current_key_null, Toast.LENGTH_LONG)
            .show()
    }
}

fun toggleBiometrics(
    context: FragmentActivity,
    preferences: SobrietyPreferences,
    coroutineScope: CoroutineScope,
    newValue: Boolean
) {
    coroutineScope.launch {
        if (newValue) {
            preferences.setBiometricsEnabled(false)

            val canEnable = canEnableAuthentication(context)
            if (!canEnable) {
                return@launch
            }

            if (preferences.encryptedByPassword.first() && ApplicationDependencies.getSqlCipherKey().keyBytes != null) {
                val keyStoreHelper = KeyStoreHelper()
                val cipher = try {
                    keyStoreHelper.getEncryptCipher()
                } catch (e: KeyPermanentlyInvalidatedException) {
                    LogEvent.i(TAG, "Keystore key permanently invalidated")
                    Toast.makeText(
                        context,
                        R.string.enable_error_couldnt_get_encrypt_key,
                        Toast.LENGTH_LONG
                    ).show()
                    return@launch
                }

                showBiometricPrompt(
                    context,
                    BiometricPrompt.CryptoObject(cipher),
                    onAuthenticated = { cryptoObject ->
                        coroutineScope.launch {
                            val encryptedData = keyStoreHelper.encryptData(
                                ApplicationDependencies.getSqlCipherKey().keyBytes!!,
                                cryptoObject!!.cipher!!
                            )
                            preferences.setKeystoreEncryptedKey(encryptedData)
                            preferences.setBiometricsEnabled(true)
                        }
                    },
                    title = R.string.enable_title,
                    subtitle = R.string.enable_summary
                )
            } else {
                preferences.setBiometricsEnabled(true)
                Toast.makeText(context, R.string.enable_success, Toast.LENGTH_LONG).show()
            }
        } else {
            preferences.setKeystoreEncryptedKey(null)
            Toast.makeText(context, R.string.disable_success, Toast.LENGTH_LONG).show()
        }
    }
}
