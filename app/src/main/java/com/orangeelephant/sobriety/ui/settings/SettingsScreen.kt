package com.orangeelephant.sobriety.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jamal.composeprefs3.ui.GroupHeader
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.ListPref
import com.jamal.composeprefs3.ui.prefs.SwitchPref
import com.jamal.composeprefs3.ui.prefs.TextPref
import com.orangeelephant.sobriety.BuildConfig
import com.orangeelephant.sobriety.MainActivity
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.ConfirmationDialog
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.LoadingDialog
import com.orangeelephant.sobriety.ui.common.PasswordInputField
import com.orangeelephant.sobriety.ui.common.SobrietyAlertDialog
import com.orangeelephant.sobriety.util.SobrietyPreferences
import com.orangeelephant.sobriety.util.dataStore

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    popBack: () -> Unit,
    onNavigateToExport: () -> Unit,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current as MainActivity
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    var showCreatePassword by remember { mutableStateOf(false) }
    var showChangePassword by remember { mutableStateOf(false) }
    var showDisableBioConfirmation by remember { mutableStateOf(false) }
    var showDecryptConfirmation by remember { mutableStateOf(false) }

    Scaffold (
        topBar = { GenericTopAppBar (
            title = R.string.settings,
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackIcon(onClick = popBack)}
        )},
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        PrefsScreen(
            dataStore = LocalContext.current.dataStore,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = 0.dp
            )
        ) {
            prefsGroup({
                GroupHeader(title = stringResource(id = R.string.appearance))
            }) {
                prefsItem {
                    ListPref(
                        key = SobrietyPreferences.LANGUAGE,
                        defaultValue = "default",
                        title = stringResource(id = R.string.language),
                        useSelectedAsSummary = true,
                        entries = settingsViewModel.availableLanguages
                    )
                    ListPref(
                        key = SobrietyPreferences.THEME,
                        defaultValue = "default",
                        title = stringResource(id = R.string.theme),
                        useSelectedAsSummary = true,
                        entries = settingsViewModel.availableThemes
                    )
                    SwitchPref(
                        key = SobrietyPreferences.DYNAMIC_COLOURS,
                        title = stringResource(id = R.string.dynamic_colours),
                        summary = stringResource(id = R.string.dynamic_colours_summary),
                        defaultChecked = false,
                        enabled = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                    )
                    Divider()
                }
            }

            prefsGroup({
                GroupHeader(title = stringResource(id = R.string.security))
            }) {
                prefsItem {
                    SwitchPref(
                        key = SobrietyPreferences.BIOMETRIC_UNLOCK,
                        title = stringResource(id = R.string.biometric_unlock),
                        summary = stringResource(id = R.string.biometric_unlock_description),
                        defaultChecked = false,
                        onCheckedChange = { newValue ->
                            if (newValue) {
                                settingsViewModel.onToggleFingerprint(localContext, true)
                            } else {
                                showDisableBioConfirmation = true
                            }
                        }
                    )
                    if (settingsViewModel.encryptedWithPassword.value) {
                        TextPref(
                            title = stringResource(id = R.string.change_db_password),
                            summary = stringResource(id = R.string.change_db_password_summary),
                            onClick = { showChangePassword = true },
                            enabled = true
                        )
                        TextPref(
                            title = stringResource(id = R.string.decrypt_db),
                            summary = stringResource(id = R.string.decrypt_db_summary),
                            onClick = { showDecryptConfirmation = true },
                            enabled = true
                        )
                    } else {
                        TextPref(
                            title = stringResource(id = R.string.encrypt_db_with_password),
                            summary = stringResource(id = R.string.encrypt_db_with_password_summary),
                            onClick = { showCreatePassword = true },
                            enabled = true
                        )
                    }
                    Divider()
                }
            }

            prefsGroup({
                GroupHeader(title = stringResource(id = R.string.export))
            }) {
                prefsItem {
                    TextPref (
                        title = stringResource(id = R.string.export),
                        onClick = onNavigateToExport,
                        enabled = true
                    )
                }
            }

            prefsGroup({
                GroupHeader(title = stringResource(id = R.string.about))
            }) {
                prefsItem {
                    TextPref(
                        title = stringResource(id = R.string.version),
                        summary = BuildConfig.VERSION_NAME
                    )

                    val context = LocalContext.current
                    val sourceCodeIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(stringResource(id = R.string.source_code_url))
                    )
                    TextPref(
                        title = stringResource(id = R.string.source_code),
                        summary = stringResource(id = R.string.source_code_description),
                        onClick = { context.startActivity(sourceCodeIntent) },
                        enabled = true
                    )
                }
            }
        }

        if (showCreatePassword) {
            ManagePasswordDialog(
                onDismiss = { showCreatePassword = false },
                onConfirm = { password -> 
                    settingsViewModel.onSetPassword(localContext, password)
                    showCreatePassword = false
                },
                title = R.string.create_password_dialog,
                description = R.string.create_password_description,
                btnPosLabel = R.string.encrypt_database
            )
        }

        if (showChangePassword) {
            ManagePasswordDialog(
                onDismiss = { showChangePassword = false },
                onConfirm = { password ->
                    settingsViewModel.onSetPassword(localContext, password)
                    showChangePassword = false
                },
                title = R.string.change_password_dialog,
                description = R.string.change_password_description,
                btnPosLabel = R.string.confirm_change_password,
                inputBoxLabel = R.string.new_password
            )
        }
        
        if (settingsViewModel.isEncryptingDb.value || settingsViewModel.isDecryptingDb.value) {
            val message = if ( settingsViewModel.isEncryptingDb.value ) {
                R.string.encryption_in_progress
            } else {
                R.string.decryption_in_progress
            }
            LoadingDialog(label = message)
        }

        if (showDisableBioConfirmation) {
            ConfirmationDialog(
                onConfirm = {
                    settingsViewModel.onToggleFingerprint(localContext, false)
                    showDisableBioConfirmation = false
                },
                onDismiss = {
                    settingsViewModel.onCancelBiometricDisable()
                    showDisableBioConfirmation = false
                },
                title = R.string.disable_biometrics,
                description = R.string.disable_biometrics_description
            )
        } else if (showDecryptConfirmation) {
            ConfirmationDialog(
                onConfirm = {
                    settingsViewModel.onDecrypt(localContext)
                    showDecryptConfirmation = false
                },
                onDismiss = {
                    showDecryptConfirmation = false
                },
                title = R.string.disable_encryption,
                description = R.string.disable_encryption_description
            )
        }
    }
}

@Composable
fun ManagePasswordDialog(
    onDismiss: () -> Unit,
    onConfirm: (password: String) -> Unit,
    @StringRes title: Int,
    @StringRes description: Int,
    @StringRes btnPosLabel: Int,
    @StringRes inputBoxLabel: Int = R.string.password
) {
    val password = remember { mutableStateOf("") }

    SobrietyAlertDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(id = title),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                stringResource(id = description),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            PasswordInputField(
                password = password,
                label = inputBoxLabel
            ) {
                onConfirm(password.value)
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = { onConfirm(password.value) },
                enabled = password.value != "",
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(id = btnPosLabel))
            }
        }
    }
}