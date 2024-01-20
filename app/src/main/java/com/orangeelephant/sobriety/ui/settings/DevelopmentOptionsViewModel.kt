package com.orangeelephant.sobriety.ui.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DevelopmentOptionsViewModel @Inject constructor(
    @ApplicationContext app: Context
): ViewModel() {
    companion object {
        private val TAG = DevelopmentOptionsViewModel::class.java.simpleName
    }

    private val preferences = SobrietyPreferences(context = app)

    fun onMarkSetupIncomplete() {
        viewModelScope.launch {
            preferences.setSetupCompleted(false)
            preferences.setSetupCurrentStep(0)
        }
    }
}