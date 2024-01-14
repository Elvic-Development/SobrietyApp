package com.orangeelephant.sobriety

import android.content.res.Resources
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.logging.LogEvent
import com.orangeelephant.sobriety.ui.screens.unlock.UnlockScreen
import com.orangeelephant.sobriety.ui.theme.SobrietyTheme
import com.orangeelephant.sobriety.util.SobrietyPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import net.sqlcipher.database.SQLiteDatabase
import java.lang.IllegalStateException
import java.util.*

@AndroidEntryPoint
class MainActivity : FragmentActivity() {
    private val dynamicColoursPreference = mutableStateOf(false)
    private val themePreference = mutableStateOf("default")

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

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
                    LogEvent.i(TAG, "Updating language to $it")
                    setLocale(it)
                }
            }
        }

        // when theme or dynamic colours pref change the theme is recomposed
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SobrietyPreferences(applicationContext).theme.collect {
                    LogEvent.i(TAG, "Updating theme to $it")
                    themePreference.value = it
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                SobrietyPreferences(applicationContext).dynamicColours.collect {
                    LogEvent.i(TAG, "Updating dynamic colours to $it")
                    dynamicColoursPreference.value = it
                }
            }
        }

        installSplashScreen()
        requireUnlock()
    }

    override fun onResume() {
        super.onResume()
        try {
            ApplicationDependencies.getSqlCipherKey()
        } catch (e: IllegalStateException) {
            requireUnlock()
            LogEvent.e(TAG, "SQL cipher key unavailable after activity resume", e)
        }
    }

    private fun setContentMain() {
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
                        context = this
                    )
                }
            }
        }
    }

    private fun requireUnlock() {
        setContent {
            SobrietyTheme (
                themePreference = themePreference,
                dynamicColor = dynamicColoursPreference
            ){
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    UnlockScreen(navigateToHome = { setContentMain() })
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
            LogEvent.i(TAG, "Locale unchanged")
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