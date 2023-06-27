package com.orangeelephant.sobriety.ui.settings

import android.content.Context
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext app: Context
): ViewModel() {
    private val preferences = SobrietyPreferences(context = app)
    val availableLanguages = preferences.availableLanguages
    val availableThemes = preferences.availableThemes
    val encryptedWithPassword = mutableStateOf(false)

    init {
        viewModelScope.launch {
            preferences.encryptedByPassword.collect{ encryptedWithPassword.value = it }
        }
    }

    fun onEncryptWithPassword(password: String) {
        println(password)
    }

}