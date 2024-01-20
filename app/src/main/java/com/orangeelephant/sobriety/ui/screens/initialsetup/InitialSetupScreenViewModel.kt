package com.orangeelephant.sobriety.ui.screens.initialsetup

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.settings.setEncryptionPassword
import com.orangeelephant.sobriety.ui.settings.toggleBiometrics
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class InitialSetupScreenViewModel @Inject constructor(
    @ApplicationContext app: Context
): ViewModel() {
    companion object {
        const val WELCOME = 0
        const val IMPORT_BACKUP = 1
        const val CREATE_PASSWORD = 2
        const val ENABLE_BIOMETRICS = 3
        const val FINISH = 4
    }

    private val preferences = SobrietyPreferences(context = app)

    var setupComplete by mutableStateOf(false)
    var currentStep by mutableIntStateOf(0)
    var isEncryptingDb by mutableStateOf(false)

    init {
        viewModelScope.launch {
            preferences.setupCurrentStep.collect { currentStep = it }
            preferences.setupCompleted.collect {
                setupComplete = it
            }
        }
    }

    fun onSetPassword(context: FragmentActivity, password: String) {
        viewModelScope.launch {
            isEncryptingDb = true

            withContext(Dispatchers.Default) {
                setEncryptionPassword(context, preferences, password)
            }

            Toast.makeText(context, R.string.encrypted_successfully, Toast.LENGTH_LONG).show()
            isEncryptingDb = false
            incrementCurrentStep()
        }
    }

    fun onEnableBiometrics(context: FragmentActivity) {
        toggleBiometrics(context, preferences, viewModelScope, true)
        incrementCurrentStep()
    }

    fun incrementCurrentStep() {
        viewModelScope.launch {
            if (currentStep == FINISH) {
                preferences.setSetupCompleted(true)
                setupComplete = true
            } else {
                preferences.setSetupCurrentStep(currentStep + 1)
            }
        }
    }

    fun onLaunchSelectImportFile(launcher: ManagedActivityResultLauncher<Array<String>, Uri?>) {
        launcher.launch(arrayOf("application/*"))
    }

    fun onImportPlaintextDatabase(uri: Uri, context: Context) {
        val inputStream = context.contentResolver.openInputStream(uri)
        inputStream?.let {
            ApplicationDependencies.getDatabase().importPlaintextDatabase(context, it)
            it.close()
            incrementCurrentStep()
        }
    }
}