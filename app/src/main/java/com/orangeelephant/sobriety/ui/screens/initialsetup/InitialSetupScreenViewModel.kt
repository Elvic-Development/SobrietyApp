package com.orangeelephant.sobriety.ui.screens.initialsetup

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.settings.setEncryptionPassword
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
    private val preferences = SobrietyPreferences(context = app)

    val isEncryptingDb = mutableStateOf(false)

    fun onSetPassword(context: FragmentActivity, password: String) {
        viewModelScope.launch {
            isEncryptingDb.value = true

            withContext(Dispatchers.Default) {
                setEncryptionPassword(context, preferences, password)
            }

            Toast.makeText(context, R.string.encrypted_successfully, Toast.LENGTH_LONG).show()
            isEncryptingDb.value = false
        }
    }
}