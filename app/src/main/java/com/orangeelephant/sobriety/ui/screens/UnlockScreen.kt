package com.orangeelephant.sobriety.ui.screens

import android.os.Build
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orangeelephant.sobriety.MainActivity
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.Screen
import com.orangeelephant.sobriety.util.showBiometricPrompt

@Composable
fun UnlockScreen(navController: NavController, biometricEnabled: Boolean) {
    val activity = LocalContext.current as MainActivity
    val hasShownPrompt = remember { mutableStateOf(false) }
    val onAuthenticated = {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Unlock.route) {
                inclusive = true
            }
        }
    }

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P && biometricEnabled) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(id = R.drawable.lotus_app_icon),
                contentDescription = stringResource(id = R.string.app_name)
            )
            Text(stringResource(id = R.string.app_name), style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(40.dp))
            Button(
                onClick = { showBiometricPrompt(activity, onAuthenticated) },
                shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
                modifier = Modifier.size(width = 250.dp, height = 50.dp)
            ) {
                Icon(Icons.Default.Lock, contentDescription = "lock icon")
                Spacer(modifier = Modifier.width(10.dp))
                Text(stringResource(id = R.string.prompt_unlock_button))
            }
        }

        if (!hasShownPrompt.value) {
            hasShownPrompt.value = true
            showBiometricPrompt(activity, onAuthenticated)
        }
    } else {
        onAuthenticated()
    }
}