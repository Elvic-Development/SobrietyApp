package com.orangeelephant.sobriety.util

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
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

@RequiresApi(Build.VERSION_CODES.P)
fun showBiometricPrompt(activity: FragmentActivity, onAuthenticated: () -> Unit) {
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
                Toast.makeText(
                    activity,
                    R.string.unlock_error,
                    Toast.LENGTH_LONG
                ).show()
            }

            override fun onAuthenticationSucceeded(
                result: BiometricPrompt.AuthenticationResult
            ) {
                onAuthenticated()
            }

            override fun onAuthenticationFailed() {
                Toast.makeText(
                    activity,
                    R.string.unlock_failed,
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )
    biometricPrompt.authenticate(promptInfo)
}