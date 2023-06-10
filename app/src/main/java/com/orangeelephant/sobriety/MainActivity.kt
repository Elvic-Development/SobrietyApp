package com.orangeelephant.sobriety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.ui.screens.HomeScreen
import com.orangeelephant.sobriety.ui.theme.SobrietyTheme
import net.sqlcipher.database.SQLiteDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!ApplicationDependencies.isInitialised()) {
            initialise();
        }

        super.onCreate(savedInstanceState)

        setContent {
            SobrietyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(this)
                }
            }
        }
    }

    private fun initialise() {
        //initialise ApplicationDependencies
        ApplicationDependencies.init(application)
        SQLiteDatabase.loadLibs(this);
    }
}