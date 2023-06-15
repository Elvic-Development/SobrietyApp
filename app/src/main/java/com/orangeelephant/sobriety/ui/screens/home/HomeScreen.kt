package com.orangeelephant.sobriety.ui.screens.home

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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.Screen
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.mock.MockCounterRepository
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.SettingsLink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    homeScreenViewModel: HomeScreenViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    
    Scaffold (
        topBar = { GenericTopAppBar(
            title = R.string.app_name,
            scrollBehavior = scrollBehavior,
            actions = { SettingsLink(navController) }
        ) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navController.navigate(route = Screen.AddCounter.route)},
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
            items(homeScreenViewModel.allCounters) { counter ->
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
                navController.navigate(
                    route = Screen.CounterFullView.createRoute(counterId = counter.id)
                )
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


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        navController = rememberNavController(),
        homeScreenViewModel = HomeScreenViewModel(MockCounterRepository())
    )
}