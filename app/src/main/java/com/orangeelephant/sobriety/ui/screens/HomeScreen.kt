package com.orangeelephant.sobriety.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.storage.models.Counter

@Composable
fun HomeScreen(context: Context) {
    val allCounters = ApplicationDependencies.getDatabase().counters.getAllCounters()
    LazyColumn {
        items(allCounters) { counter ->
            CounterView(counter)
        }
    }

    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier.padding(all = 15.dp)
    ) {
        FloatingActionButton(
            onClick = { Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show() },
            shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 35)),
        ) {
            Icon(Icons.Filled.Add, contentDescription = "Create counter")
        }
    }
}

@Composable
fun CounterView(counter: Counter) {
    Column(modifier = Modifier.padding(all = 8.dp)) {
        Text(
            text = counter.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "counter.startTimeMillis")
    }
}
