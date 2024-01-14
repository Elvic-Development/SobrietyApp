package com.orangeelephant.sobriety.ui.screens.initialsetup

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.navigation.compose.hiltViewModel
import com.orangeelephant.sobriety.MainActivity
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.LoadingDialog
import com.orangeelephant.sobriety.ui.common.PasswordConfirmationLayout

@Composable
fun SetupScreen(
    viewModel: InitialSetupScreenViewModel = hiltViewModel()
) {

}

@Composable
fun Welcome() {

}

@Composable
fun ImportBackup() {

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

    if (viewModel.isEncryptingDb.value) {
        LoadingDialog(label = R.string.encryption_in_progress)
    }
}

@Composable
fun EnableBiometrics() {

}

@Composable
fun Finish() {

}