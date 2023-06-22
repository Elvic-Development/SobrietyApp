package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CounterFullView(
    counterFullScreenViewModel: CounterFullScreenViewModel
) {
    val counter = counterFullScreenViewModel.counter

    Text (
        text = counter.value.name,
        color = MaterialTheme.colorScheme.primary,
        style = MaterialTheme.typography.headlineMedium,
    )
    Spacer(modifier = Modifier.width(4.dp))
    Text(text = "counter.startTimeMillis")
}