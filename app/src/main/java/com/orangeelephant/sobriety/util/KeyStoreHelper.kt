package com.orangeelephant.sobriety.util

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.logging.LogEvent.e
import java.io.IOException
import java.security.GeneralSecurityException
import java.security.InvalidKeyException
import java.security.Key
import java.security.KeyManagementException
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class KeyStoreHelper {
    companion object {
        private val TAG = KeyStoreHelper::class.java.simpleName

        private val AES_MODE = "AES/GCM/NoPadding"
        private const val ANDROID_KEY_STORE = "AndroidKeyStore"
        private const val KEY_ALIAS = "sobriety_database_key"
        private const val KEY_SIZE = 256
    }

    private val keyStore: KeyStore = KeyStore.getInstance(ANDROID_KEY_STORE)
    init {
        keyStore.load(null)
    }

    fun getEncryptCipher(): Cipher {
        val cipher = Cipher.getInstance(AES_MODE)
        val key = getKey()
        cipher.init(Cipher.ENCRYPT_MODE, key)
        return cipher
    }

    fun getDecryptCipher(iv: ByteArray): Cipher {
        val cipher = Cipher.getInstance(AES_MODE)
        val key = getKey()
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
        return cipher
    }

    fun encryptData(plaintext: ByteArray, cipher: Cipher): SobrietyPreferences.EncryptedData {
        val ciphertext = cipher.doFinal(plaintext)
        return SobrietyPreferences.EncryptedData(ciphertext, cipher.iv)
    }

    fun decryptData(ciphertext: ByteArray, cipher: Cipher): ByteArray {
        return cipher.doFinal(ciphertext)
    }

    private fun getKey(): Key {
        try {
            if (!keyStore.containsAlias(KEY_ALIAS)) {
                generateKey()
            }
            val key = keyStore.getKey(KEY_ALIAS, null) as SecretKey

            if (isKeyInvalidated(key)) {
                throw KeyPermanentlyInvalidatedException()
            } else {
                return key
            }
        } catch (e: GeneralSecurityException) {
            e(TAG, "Unable to get the key from the keystore", e)
            throw KeyManagementException("Unable to get the key from the keystore")
        } catch (e: IOException) {
            e(TAG, "Unable to get the key from the keystore", e)
            throw KeyManagementException("Unable to get the key from the keystore")
        }
    }

    private fun isKeyInvalidated(key: SecretKey): Boolean {
        try {
            val cipher = Cipher.getInstance(AES_MODE)
            cipher.init(Cipher.ENCRYPT_MODE, key)
        } catch (e: InvalidKeyException) {
            return true
        }
        return false
    }

    private fun generateKey() {
        try {
            val generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)

            generator.init(
                KeyGenParameterSpec.Builder(
                    KEY_ALIAS,
                    KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
                )
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setUserAuthenticationRequired(true)
                .setRandomizedEncryptionRequired(true)
                .setKeySize(KEY_SIZE)
                .build())

            generator.generateKey()
            LogEvent.i(TAG, "New keypair for encrypting secrets created")
        } catch (e: GeneralSecurityException) {
            e(TAG, "Unable to generate a key", e)
        }
    }
}