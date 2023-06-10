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

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SobrietyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HomeScreen(counters =
                        listOf(
                            Counter(1, "GREAT", 0L, 0L),
                            Counter(1, "Bad", 0L, 0L),
                            Counter(1, "afdsgag", 0L, 0L),
                            Counter(1, "afdgd", 0L, 0L),

                        )
                    )
                }
            }
        }
    }
}