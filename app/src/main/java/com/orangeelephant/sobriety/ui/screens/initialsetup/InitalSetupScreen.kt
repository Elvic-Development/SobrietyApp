package com.orangeelephant.sobriety.ui.screens.initialsetup

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.orangeelephant.sobriety.MainActivity
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.LinearProgressIndicator
import com.orangeelephant.sobriety.ui.common.LoadingDialog
import com.orangeelephant.sobriety.ui.common.Logo
import com.orangeelephant.sobriety.ui.common.LogoAndName
import com.orangeelephant.sobriety.ui.common.PasswordConfirmationLayout

@Composable
fun InitialSetupScreen(
    onNavigateToHome: () -> Unit,
    viewModel: InitialSetupScreenViewModel = hiltViewModel()
) {
    println(viewModel.setupComplete)
    if (viewModel.setupComplete) {
        onNavigateToHome()
    }

    Scaffold { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
        ) {
            when (viewModel.currentStep) {
                InitialSetupScreenViewModel.WELCOME -> {
                    LargeLogo()
                    Welcome()
                }
                InitialSetupScreenViewModel.IMPORT_BACKUP -> {
                    SmallLogo()
                    ImportBackup()
                }
                InitialSetupScreenViewModel.CREATE_PASSWORD -> {
                    SmallLogo()
                    SetupPassword(viewModel = viewModel)
                }
                InitialSetupScreenViewModel.ENABLE_BIOMETRICS -> {
                    SmallLogo()
                    EnableBiometrics(viewModel = viewModel)
                }
                InitialSetupScreenViewModel.FINISH -> {
                    LargeLogo()
                    Finish()
                }
            }

            Spacer(modifier = Modifier.fillMaxHeight(0.85f))

        }
        Column (
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
        ) {
            LinearProgressIndicator(
                numSteps = InitialSetupScreenViewModel.FINISH,
                currentStep = viewModel.currentStep
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(onClick = { viewModel.incrementCurrentStep() }) {
                Text("Skip for now", color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun Welcome() {
    Text("Welcome", color = MaterialTheme.colorScheme.outline)
}

@Composable
fun ImportBackup() {
    Text("Import", color = MaterialTheme.colorScheme.outline)
}

@Composable
fun SetupPassword(viewModel: InitialSetupScreenViewModel) {
    val context = LocalContext.current as MainActivity

    PasswordConfirmationLayout(
        onConfirm = { password ->
            viewModel.onSetPassword(context, password)
        },
        title = R.string.create_password_dialog,
        description = R.string.create_password_description,
        btnPosLabel = R.string.encrypt_database
    )

    if (viewModel.isEncryptingDb) {
        LoadingDialog(label = R.string.encryption_in_progress)
    }
}

@Composable
fun EnableBiometrics(viewModel: InitialSetupScreenViewModel) {
    val activity = LocalContext.current as MainActivity
    Button(
        onClick = { viewModel.onEnableBiometrics(activity) },
        shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
        modifier = Modifier.size(width = 250.dp, height = 50.dp)
    ) {
        Icon(Icons.Default.Lock, contentDescription = "lock icon")
        Spacer(modifier = Modifier.width(10.dp))
        Text(stringResource(id = R.string.prompt_unlock_button))
    }
}

@Composable
fun Finish() {
    Text("Finish", color = MaterialTheme.colorScheme.outline)
}

@Composable
fun SmallLogo() {
    Spacer(modifier = Modifier.height(20.dp))
    Box(modifier = Modifier.size(80.dp)) {
        Logo()
    }
    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
}

@Composable
fun LargeLogo() {
    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
    LogoAndName()
    Spacer(modifier = Modifier.fillMaxHeight(0.2f))
}