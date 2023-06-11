package com.orangeelephant.sobriety.ui.screens

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orangeelephant.sobriety.ApplicationDependencies

@Composable
fun CounterFullView(counterId: Int) {
    val counter = ApplicationDependencies.getDatabase().counters.getCounterById(counterId)
    val reasons = ApplicationDependencies.getDatabase().reasons.getReasonsForCounter(counterId)

    Text (
        text = counter.name,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = "counter.startTimeMillis")
}