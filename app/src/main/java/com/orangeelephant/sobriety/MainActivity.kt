package com.orangeelephant.sobriety

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.ui.theme.SobrietyTheme
import net.sqlcipher.database.SQLiteDatabase

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        if (!ApplicationDependencies.isInitialised()) {
            ApplicationDependencies.init(application)
            SQLiteDatabase.loadLibs(this)
        }
        super.onCreate(savedInstanceState)

        setContent {
            SobrietyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SobrietyAppNavigation(navController = rememberNavController(), context = this)
                }
            }
        }
    }

    private fun addTestCounters() {
        val counterDatabase = ApplicationDependencies.getDatabase().counters
        for (counter in listOf(
            Counter(1, "GREAT", 0L, 0L),
            Counter(1, "Bad", 0L, 0L),
            Counter(1, "afdsgag", 0L, 0L),
            Counter(1, "afdgd", 0L, 0L),
        )) {
            counterDatabase.saveCounterObjectToDb(counter)
        }
    }
}