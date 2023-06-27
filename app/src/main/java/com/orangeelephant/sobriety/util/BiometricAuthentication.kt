package com.orangeelephant.sobriety.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.fragment.app.FragmentActivity
import com.orangeelephant.sobriety.R

fun getAllowedAuthenticators(): Int {
    return if (Build.VERSION.SDK_INT >= 30) {
        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    } else {
        BIOMETRIC_STRONG
    }
}

fun canEnableAuthentication(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    when (biometricManager.canAuthenticate(getAllowedAuthenticators())) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            return true
        }
        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            return false
        }
        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            return false
        }
        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            return false
        }
        else -> {
            return false
        }
    }
}

fun showBiometricPrompt(activity: FragmentActivity, onAuthenticated: (String?) -> Unit) {
    val ignoredErrors = listOf(
        BiometricPrompt.ERROR_CANCELED,
        BiometricPrompt.ERROR_USER_CANCELED
    )

    val promptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle(activity.getString(R.string.unlock_title))
        .setSubtitle(activity.getString(R.string.unlock_subtitle))
        .setAllowedAuthenticators(getAllowedAuthenticators())
        .build()

    val biometricPrompt = BiometricPrompt (
        activity,
        object : BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(
                errorCode: Int,
                errString: CharSequence
            ) {
                if (errorCode !in ignoredErrors) {
                    Toast.makeText(
                        activity,
                        activity.getString(R.string.unlock_error, errString),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                onAuthenticated(null)
            }
        }
    )
    biometricPrompt.authenticate(promptInfo)
}