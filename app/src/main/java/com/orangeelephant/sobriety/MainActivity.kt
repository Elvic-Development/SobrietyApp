package com.orangeelephant.sobriety

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.ui.theme.SobrietyTheme
import com.orangeelephant.sobriety.util.SobrietyPreferences
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import net.sqlcipher.database.SQLiteDatabase
import java.util.*

class MainActivity : FragmentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!ApplicationDependencies.isInitialised()) {
            ApplicationDependencies.init(application)
            SQLiteDatabase.loadLibs(this)
        }
        super.onCreate(savedInstanceState)

        // update language on pref value change
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SobrietyPreferences(applicationContext).language.collect {
                    setLocale(it)
                }
            }
        }

        // when theme or dynamic colours pref change the theme is recomposed
        val themePreference = mutableStateOf("default")
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SobrietyPreferences(applicationContext).theme.collect {
                    themePreference.value = it
                }
            }
        }

        val dynamicColoursPreference = mutableStateOf(false)
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SobrietyPreferences(applicationContext).dynamicColours.collect {
                    dynamicColoursPreference.value = it
                }
            }
        }

        val biometricUnlock = runBlocking {
            SobrietyPreferences(applicationContext).biometricUnlock.first()
        }

        setContent {
            SobrietyTheme (
                themePreference = themePreference,
                dynamicColor = dynamicColoursPreference
            ){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SobrietyAppNavigation(
                        navController = rememberNavController(),
                        context = this,
                        biometricEnabled = biometricUnlock
                    )
                }
            }
        }
    }

    private fun setLocale(lang: String) {
        val currentLocale = resources.configuration.locales.get(0)
        val newLocale: Locale = if (Objects.equals(lang, "default")) {
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Locale(lang)
        }

        // if language is unchanged on activity recreate then abort
        if (currentLocale == newLocale) {
            println("Locale unchanged")
            return
        }

        Locale.setDefault(newLocale)
        val configuration = resources.configuration
        configuration.setLocale(newLocale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        // recreate to ensure everything is redrawn with new strings
        recreate()
    }
}