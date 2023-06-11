package com.orangeelephant.sobriety.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orangeelephant.sobriety.ApplicationDependencies
import com.orangeelephant.sobriety.Screen
import com.orangeelephant.sobriety.storage.models.Counter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(context: Context, navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val allCounters = ApplicationDependencies.getDatabase().counters.getAllCounters()
    
    Scaffold (
        topBar = {
             TopAppBar (
                 title = {
                     Text(
                         text = "Sobriety",
                         style = MaterialTheme.typography.headlineLarge,
                     )
                 },
                 colors = TopAppBarDefaults.topAppBarColors(
                     containerColor = MaterialTheme.colorScheme.background,
                     scrolledContainerColor = MaterialTheme.colorScheme.primaryContainer
                 ),
                 scrollBehavior = scrollBehavior
             )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = { Toast.makeText(context, "Not implemented", Toast.LENGTH_SHORT).show() },
                shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Create counter")
            }
        }
    ) { innerPadding ->
        LazyColumn (
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 0.dp
                )
        ) {
            items(allCounters) { counter ->
                CounterView(counter, navController)
            }
        }
    }
}

@Composable
fun CounterView(counter: Counter, navController: NavController) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 14.dp
            )
            .fillMaxWidth()
            .clickable {
                navController.navigate(route = Screen.CounterFullView.createRoute(counterId = counter.id))
            }
    ) {
        Text(
            text = counter.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = "counter.startTimeMillis")
    }
}
