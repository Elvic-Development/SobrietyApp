package com.orangeelephant.sobriety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.ui.theme.SobrietyTheme
import net.sqlcipher.database.SQLiteDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!ApplicationDependencies.isInitialised()) {
            ApplicationDependencies.init(application)
            SQLiteDatabase.loadLibs(this)
        }
        super.onCreate(savedInstanceState)

        //setLocale()

        setContent {
            SobrietyTheme {
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

    /*private fun setLocale() {
        val lang: String = Preferences.getLanguage()

        val locale: Locale = if (Objects.equals(lang, "default")) {
            Resources.getSystem().configuration.locales.get(0)
        } else {
            Locale(lang)
        }
        Locale.setDefault(locale)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }*/
}