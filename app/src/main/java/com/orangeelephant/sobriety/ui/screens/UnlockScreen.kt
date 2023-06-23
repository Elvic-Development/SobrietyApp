package com.orangeelephant.sobriety.ui.screens

import android.os.Build
import androidx.compose.material3.Button
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.orangeelephant.sobriety.MainActivity
import com.orangeelephant.sobriety.Screen
import com.orangeelephant.sobriety.util.showBiometricPrompt

@Composable
fun UnlockScreen(navController: NavController, biometricEnabled: Boolean) {
    val activity = LocalContext.current as MainActivity
    val hasShownPrompt = remember { mutableStateOf(false) }
    val onAuthenticated = { navController.navigate(Screen.Home.route) }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && biometricEnabled) {
        Button(onClick = { showBiometricPrompt(activity, onAuthenticated) }) {

        }
        if (!hasShownPrompt.value) {
            hasShownPrompt.value = true
            showBiometricPrompt(activity, onAuthenticated)
        }
    } else {
        onAuthenticated()
    }
}