package com.orangeelephant.sobriety.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.CryptoObject
import androidx.fragment.app.FragmentActivity
import com.orangeelephant.sobriety.R

fun getAllowedAuthenticators(): Int {
    return if (Build.VERSION.SDK_INT >= 30) {
        BIOMETRIC_STRONG or DEVICE_CREDENTIAL
    } else {
        BIOMETRIC_STRONG
    }
}

fun requiresNegativeText(): Boolean {
    return Build.VERSION.SDK_INT < 30
}

fun canEnableAuthentication(context: Context): Boolean {
    val biometricManager = BiometricManager.from(context)
    when (biometricManager.canAuthenticate(getAllowedAuthenticators())) {
        BiometricManager.BIOMETRIC_SUCCESS -> {
            return true
        }

        BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
            Toast.makeText(context, R.string.enable_error_no_hardware, Toast.LENGTH_LONG).show()
            return false
        }

        BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
            Toast.makeText(context, R.string.enable_error_hardware_unavailable, Toast.LENGTH_LONG)
                .show()
            return false
        }

        BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
            Toast.makeText(context, R.string.enable_error_no_biometric_enrolled, Toast.LENGTH_LONG)
                .show()
            return false
        }

        else -> {
            Toast.makeText(context, R.string.enable_error_unknowm, Toast.LENGTH_LONG).show()
            return false
        }
    }
}

fun showBiometricPrompt(
    activity: FragmentActivity,
    cryptoObject: CryptoObject?,
    onAuthenticated: (CryptoObject?) -> Unit,
    onError: () -> Unit = {},
    @StringRes title: Int = R.string.unlock_title,
    @StringRes subtitle: Int = R.string.unlock_subtitle
) {
    val ignoredErrors = listOf(
        BiometricPrompt.ERROR_CANCELED,
        BiometricPrompt.ERROR_USER_CANCELED
    )

    val promptInfoBuilder = BiometricPrompt.PromptInfo.Builder()
        .setTitle(activity.getString(title))
        .setSubtitle(activity.getString(subtitle))
        .setAllowedAuthenticators(getAllowedAuthenticators())

    if (requiresNegativeText()) {
        promptInfoBuilder.setNegativeButtonText(activity.getString(R.string.cancel_button))
    }
    val promptInfo = promptInfoBuilder.build()

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
                    onError()
                }
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                onAuthenticated(result.cryptoObject)
            }
        }
    )

    cryptoObject?.let {
        biometricPrompt.authenticate(promptInfo, cryptoObject)
    } ?: run {
        biometricPrompt.authenticate(promptInfo)
    }

}