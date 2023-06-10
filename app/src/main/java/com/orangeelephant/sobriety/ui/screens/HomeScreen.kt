package com.orangeelephant.sobriety.ui.screens

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.orangeelephant.sobriety.storage.models.Counter

@Composable
fun HomeScreen(counters: List<Counter>) {
    LazyColumn {
        items(counters) { counter ->
            CounterView(counter)
        }
    }
}

@Composable
fun CounterView(counter: Counter) {
    Row {
        Text(text = counter.name)
        Text(text = "counter.startTimeMillis")
    }
}