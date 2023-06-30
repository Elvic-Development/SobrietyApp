package com.orangeelephant.sobriety.ui.screens.unlock

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.orangeelephant.sobriety.MainActivity
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UnlockScreen(
    navController: NavController,
    viewModel: UnlockScreenViewModel = hiltViewModel()
) {
    val activity = LocalContext.current as MainActivity

    val hasShownPrompt = remember { mutableStateOf(false) }

    if (viewModel.cipherKeyLoaded.value) {
        navController.navigate(Screen.Home.route) {
            popUpTo(Screen.Unlock.route) {
                inclusive = true
            }
        }
    }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            Icon(
                painterResource(id = R.drawable.lotus_app_icon),
                contentDescription = stringResource(id = R.string.app_name)
            )
            Text(
                stringResource(id = R.string.app_name),
                style = MaterialTheme.typography.headlineLarge
            )

            Spacer(modifier = Modifier.height(40.dp))

            if (viewModel.loadingValues.value) {
                Text("LOADING...")
            } else if (viewModel.fingerprintUnlockEnabled.value || viewModel.encrypted.value) {
                if (viewModel.encrypted.value) {
                    val password = remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = password.value,
                        onValueChange = { password.value = it },
                        label = { Text(stringResource(R.string.password)) }
                    )
                    Button(
                        onClick = { viewModel.onSubmitPassword(password.value) },
                        enabled = password.value != ""
                    ) {
                        Text(stringResource(id = R.string.submit_button))
                    }
                }

                if (viewModel.fingerprintUnlockEnabled.value) {
                    Button(
                        onClick = { viewModel.promptForBiometrics(activity) },
                        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
                        modifier = Modifier.size(width = 250.dp, height = 50.dp)
                    ) {
                        Icon(Icons.Default.Lock, contentDescription = "lock icon")
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(stringResource(id = R.string.prompt_unlock_button))
                    }

                    if (!hasShownPrompt.value && viewModel.fingerprintUnlockEnabled.value) {
                        hasShownPrompt.value = true
                        viewModel.promptForBiometrics(activity)
                    }
                }
            }
        }

        if (viewModel.retrievingKey.value) {
            AlertDialog(onDismissRequest = { /* Don't dismiss */ }) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Text("Loading encryption key")
                }
            }
        }

        if (viewModel.showKeyInvalidatedDialog.value) {
            val onDismiss = {
                viewModel.showKeyInvalidatedDialog.value = false
            }

            AlertDialog(onDismissRequest = { onDismiss() }) {
                Surface(
                    modifier = Modifier
                        .wrapContentWidth()
                        .wrapContentHeight(),
                    shape = MaterialTheme.shapes.large
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            stringResource(id = R.string.biometric_unlock_invalidated),
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Spacer(modifier = Modifier.height(5.dp))
                        Text(
                            stringResource(id = R.string.biometric_unlock_invalidated_summary),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(5.dp))

                        TextButton(
                            onClick = { onDismiss() },
                            modifier = Modifier.align(Alignment.End)
                        ) {
                            Text(stringResource(id = R.string.understood))
                        }
                    }
                }
            }
        }
    }
}