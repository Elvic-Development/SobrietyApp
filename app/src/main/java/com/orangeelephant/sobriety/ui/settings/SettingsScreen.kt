package com.orangeelephant.sobriety.ui.settings

import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
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
    navController: NavController,
    settingsViewModel: SettingsViewModel = hiltViewModel()
) {
    val localContext = LocalContext.current as MainActivity
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val showCreatePassword = remember { mutableStateOf(false) }
    val showDisableBioConfirmation = remember { mutableStateOf(false) }
    val showDecryptConfirmation = remember { mutableStateOf(false) }

    Scaffold (
        topBar = { GenericTopAppBar (
            title = R.string.settings,
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackIcon(onClick = {
                navController.popBackStack()
            })}
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
                                showDisableBioConfirmation.value = true
                            }
                        }
                    )
                    if (settingsViewModel.encryptedWithPassword.value) {
                        TextPref(
                            title = stringResource(id = R.string.decrypt_db),
                            summary = stringResource(id = R.string.decrypt_db_summary),
                            onClick = { showDecryptConfirmation.value = true },
                            enabled = true
                        )
                    } else {
                        TextPref(
                            title = stringResource(id = R.string.encrypt_db_with_password),
                            summary = stringResource(id = R.string.encrypt_db_with_password_summary),
                            onClick = { showCreatePassword.value = true },
                            enabled = true
                        )
                    }
                    Divider()
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

        if (showCreatePassword.value) {
            SetupPassword(
                onDismiss = { showCreatePassword.value = false },
                onConfirm = { password -> 
                    settingsViewModel.onEncryptWithPassword(localContext, password)
                    showCreatePassword.value = false
                }
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

        if (showDisableBioConfirmation.value) {
            ConfirmationDialog(
                onConfirm = {
                    settingsViewModel.onToggleFingerprint(localContext, false)
                    showDisableBioConfirmation.value = false
                },
                onDismiss = {
                    settingsViewModel.onCancelBiometricDisable()
                    showDisableBioConfirmation.value = false
                },
                title = R.string.disable_biometrics,
                description = R.string.disable_biometrics_description
            )
        } else if (showDecryptConfirmation.value) {
            ConfirmationDialog(
                onConfirm = {
                    settingsViewModel.onDecrypt(localContext)
                    showDecryptConfirmation.value = false
                },
                onDismiss = {
                    showDecryptConfirmation.value = false
                },
                title = R.string.disable_encryption,
                description = R.string.disable_encryption_description
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetupPassword(
    onDismiss: () -> Unit,
    onConfirm: (password: String) -> Unit
) {
    val password = remember { mutableStateOf("") }

    SobrietyAlertDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(id = R.string.create_password_dialog),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.create_password_description),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            PasswordInputField(password = password) {
                onConfirm(password.value)
            }
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = { onConfirm(password.value) },
                enabled = password.value != "",
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(id = R.string.encrypt_database))
            }
        }
    }
}