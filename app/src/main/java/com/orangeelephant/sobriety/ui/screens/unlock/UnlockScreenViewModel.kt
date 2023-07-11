package com.orangeelephant.sobriety.ui.screens.unlock

import android.content.Context
import android.security.keystore.KeyPermanentlyInvalidatedException
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.database.SqlCipherKey
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import android.util.Base64
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.compose.runtime.MutableState
import androidx.fragment.app.FragmentActivity
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.util.KeyStoreHelper
import com.orangeelephant.sobriety.util.showBiometricPrompt
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class UnlockScreenViewModel @Inject constructor(
    @ApplicationContext app: Context
): ViewModel() {
    companion object {
        private val TAG = UnlockScreenViewModel::class.java.simpleName
    }

    private val preferences = SobrietyPreferences(context = app)

    val loadingValues = mutableStateOf(true)
    val retrievingKey = mutableStateOf(false)
    val cipherKeyLoaded = mutableStateOf(false)
    val showIncorrectPasswordDialog = mutableStateOf(false)
    val showNoPasswordDialog = mutableStateOf(false)

    val fingerprintUnlockEnabled = mutableStateOf(false)
    val showKeyInvalidatedDialog = mutableStateOf(false)
    val encrypted = mutableStateOf(false)

    private val keystoreEncryptedCipherKey: MutableState<SobrietyPreferences.EncryptedData?> = mutableStateOf(null)

    init {
        viewModelScope.launch {
            fingerprintUnlockEnabled.value = preferences.biometricUnlock.first()
            keystoreEncryptedCipherKey.value = preferences.getKeystoreEncryptedKey()
            encrypted.value = preferences.encryptedByPassword.first()

            if (!encrypted.value && !fingerprintUnlockEnabled.value) {
                ApplicationDependencies.setSqlcipherKey(SqlCipherKey(isEncrypted = false))
                cipherKeyLoaded.value = true
            }

            loadingValues.value = false
        }
    }

    fun onSubmitPassword(password: String?) {
        viewModelScope.launch {
            retrievingKey.value = true

            withContext(Dispatchers.Default) {
                if (!encrypted.value) {
                    ApplicationDependencies.setSqlcipherKey(SqlCipherKey(isEncrypted = false))
                    cipherKeyLoaded.value = true
                } else if (encrypted.value && (password == null || password == "")) {
                    showNoPasswordDialog.value = true
                } else {
                    val salt = Base64.decode(preferences.passwordSalt.first(), Base64.DEFAULT)
                    val key = SqlCipherKey(
                        isEncrypted = true,
                        password = password!!.encodeToByteArray(),
                        salt = salt
                    )

                    if (ApplicationDependencies.getDatabase().keyIsCorrect(key)) {
                        ApplicationDependencies.setSqlcipherKey(key)
                        cipherKeyLoaded.value = true
                    } else {
                        showIncorrectPasswordDialog.value = true
                    }
                }
            }

            retrievingKey.value = false
        }
    }

    fun promptForBiometrics(activity: FragmentActivity) {
        if (!fingerprintUnlockEnabled.value) {
            return
        }

        if (encrypted.value && keystoreEncryptedCipherKey.value == null) {
            disableBiometricsAndWarn()
            return
        }

        val keyStoreHelper = KeyStoreHelper()
        var cryptoObject: CryptoObject? = null

        if (encrypted.value) {
            try {
                cryptoObject = CryptoObject(keyStoreHelper.getDecryptCipher(keystoreEncryptedCipherKey.value!!.iv))
            } catch (e: KeyPermanentlyInvalidatedException) {
                LogEvent.i(TAG, "Key invalidated by biometric change, warning user to re-enable biometrics")
                disableBiometricsAndWarn()
                return
            }
        }

        showBiometricPrompt(
            activity,
            cryptoObject,
            onAuthenticated = { cryptoObj -> onBiometricSuccess(cryptoObj) }
        )
    }

    private fun onBiometricSuccess(cryptoObject: CryptoObject?) {
        viewModelScope.launch {
            retrievingKey.value = true

            if (!encrypted.value) {
                ApplicationDependencies.setSqlcipherKey(SqlCipherKey(isEncrypted = false))
                cipherKeyLoaded.value = true
            } else if (cryptoObject != null &&
                cryptoObject.cipher != null &&
                keystoreEncryptedCipherKey.value != null
            ) {
                val keyStoreHelper = KeyStoreHelper()
                val decryptedBytes = keyStoreHelper.decryptData(
                    keystoreEncryptedCipherKey.value!!.ciphertext,
                    cryptoObject.cipher!!
                )
                ApplicationDependencies.setSqlcipherKey(
                    SqlCipherKey(
                        isEncrypted = true,
                        keyBytes = decryptedBytes
                    )
                )
                cipherKeyLoaded.value = true
            }

            retrievingKey.value = false
        }
    }

    private fun disableBiometricsAndWarn() {
        showKeyInvalidatedDialog.value = true
        fingerprintUnlockEnabled.value = false

        viewModelScope.launch {
            preferences.setBiometricsEnabled(false)
            preferences.setKeystoreEncryptedKey(null)
        }

        LogEvent.i(TAG, "Disabled biometrics from lock screen and warned user")
    }
}