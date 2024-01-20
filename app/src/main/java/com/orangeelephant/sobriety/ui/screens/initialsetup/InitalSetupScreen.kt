package com.orangeelephant.sobriety.ui.screens.initialsetup

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.togetherWith
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
import com.orangeelephant.sobriety.ui.common.FormatTitleAndDescription
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
                InitialSetupScreenViewModel.WELCOME,
                InitialSetupScreenViewModel.FINISH -> {
                    LargeLogo()
                }
                InitialSetupScreenViewModel.IMPORT_BACKUP,
                InitialSetupScreenViewModel.CREATE_PASSWORD,
                InitialSetupScreenViewModel.ENABLE_BIOMETRICS -> {
                    SmallLogo()
                }
            }

            AnimatedContent(
                targetState = viewModel.currentStep,
                transitionSpec = {
                    slideInHorizontally(
                        animationSpec = tween(600),
                        initialOffsetX = { fullHeight -> fullHeight }) togetherWith
                            fadeOut(
                                animationSpec = tween(200)
                            )
                },
                label = "Setup stage animation"
            ) { targetState ->
                when (targetState) {
                    InitialSetupScreenViewModel.WELCOME -> {
                        Welcome()
                    }

                    InitialSetupScreenViewModel.IMPORT_BACKUP -> {
                        ImportBackup(viewModel = viewModel)
                    }

                    InitialSetupScreenViewModel.CREATE_PASSWORD -> {
                        SetupPassword(viewModel = viewModel)
                    }

                    InitialSetupScreenViewModel.ENABLE_BIOMETRICS -> {
                        EnableBiometrics(viewModel = viewModel)
                    }

                    InitialSetupScreenViewModel.FINISH -> {
                        Finish()
                    }
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

            when (viewModel.currentStep) {
                InitialSetupScreenViewModel.WELCOME -> {
                    TextButton(onClick = { viewModel.incrementCurrentStep() }) {
                        Text(
                            stringResource(id = R.string.next),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                InitialSetupScreenViewModel.IMPORT_BACKUP -> {
                    TextButton(onClick = { viewModel.incrementCurrentStep() }) {
                        Text(
                            stringResource(id = R.string.skip_import),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                InitialSetupScreenViewModel.CREATE_PASSWORD,
                InitialSetupScreenViewModel.ENABLE_BIOMETRICS -> {
                    TextButton(onClick = { viewModel.incrementCurrentStep() }) {
                        Text(
                            stringResource(id = R.string.skip_for_now),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
                InitialSetupScreenViewModel.FINISH -> {
                    TextButton(onClick = { viewModel.incrementCurrentStep() }) {
                        Text(
                            stringResource(id = R.string.finish),
                            color = MaterialTheme.colorScheme.outline
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Welcome() {
    FormatTitleAndDescription(title = R.string.welcome, description = R.string.welcome_description)
}

@Composable
fun ImportBackup(viewModel: InitialSetupScreenViewModel) {
    val context = LocalContext.current
    val selectImportDbFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            viewModel.onImportPlaintextDatabase(uri, context)
        } ?: run {
            Toast.makeText(context, context.getString(R.string.failed_to_select_file_location), Toast.LENGTH_LONG).show()
        }
    }

    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormatTitleAndDescription(
            title = R.string.import_backup,
            description = R.string.import_backup_description
        )
        Spacer(modifier = Modifier.fillMaxHeight(0.15f))

        Button(
            onClick = { viewModel.onLaunchSelectImportFile(selectImportDbFileLauncher) },
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
            modifier = Modifier.size(width = 250.dp, height = 50.dp)
        ) {
            Text(stringResource(id = R.string.tap_to_select_backup_file))
        }
    }
}

@Composable
fun SetupPassword(viewModel: InitialSetupScreenViewModel) {
    val context = LocalContext.current as MainActivity

    PasswordConfirmationLayout(
        onConfirm = { password ->
            viewModel.onSetPassword(context, password)
        },
        title = R.string.create_password,
        description = R.string.create_password_setup_description,
        btnPosLabel = R.string.encrypt_database
    )

    if (viewModel.isEncryptingDb) {
        LoadingDialog(label = R.string.encryption_in_progress)
    }
}

@Composable
fun EnableBiometrics(viewModel: InitialSetupScreenViewModel) {
    Column (
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        FormatTitleAndDescription(title = R.string.enable_biometrics, description = R.string.enable_biometrics_description)
        Spacer(modifier = Modifier.fillMaxHeight(0.15f))

        val activity = LocalContext.current as MainActivity
        Button(
            onClick = { viewModel.onEnableBiometrics(activity) },
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
            modifier = Modifier.size(width = 250.dp, height = 50.dp)
        ) {
            Icon(Icons.Default.Lock, contentDescription = "lock icon")
            Spacer(modifier = Modifier.width(10.dp))
            Text(stringResource(id = R.string.tap_to_enable_biometrics))
        }
    }
}

@Composable
fun Finish() {
    FormatTitleAndDescription(title = R.string.finish, description = R.string.finish_description)
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
    Spacer(modifier = Modifier.fillMaxHeight(0.1f))
    LogoAndName()
    Spacer(modifier = Modifier.fillMaxHeight(0.15f))
}