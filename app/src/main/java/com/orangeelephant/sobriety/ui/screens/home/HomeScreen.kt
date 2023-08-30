package com.orangeelephant.sobriety.ui.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.storage.repositories.mock.MockCounterRepository
import com.orangeelephant.sobriety.ui.common.Fab
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.SettingsLink

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onClickSettings: () -> Unit,
    onClickAddCounter: () -> Unit,
    onOpenCounter: (Int) -> Unit,
    homeScreenViewModel: HomeScreenViewModel = viewModel(),
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val allCounters = remember { homeScreenViewModel.allCounters }

    Scaffold (
        topBar = { GenericTopAppBar(
            title = R.string.app_name,
            scrollBehavior = scrollBehavior,
            actions = { SettingsLink(onClickSettings) }
        ) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            Fab(
                onClick = { onClickAddCounter() },
                icon = Icons.Filled.Add,
                contentDescription = R.string.create_counter
            )
        }
    ) { innerPadding ->
        LazyColumn (
            contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding() + 80.dp),
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = 0.dp
                )
        ) {
            items(allCounters, key = { it.id }) { counter ->
                CounterView(counter, onOpenCounter)
            }
        }
    }
}

@Composable
fun CounterView(counter: Counter, onOpenCounter: (Int) -> Unit) {
    Column(
        modifier = Modifier
            .padding(
                vertical = 8.dp,
                horizontal = 14.dp
            )
            .fillMaxWidth()
            .clickable {
                onOpenCounter(counter.id)
            }
    ) {
        Text(
            text = counter.name,
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.headlineMedium,
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = counter.currentDurationString)
    }
}


@Preview
@Composable
fun HomeScreenPreview() {
    HomeScreen(
        onClickSettings = {},
        onClickAddCounter = {},
        onOpenCounter = {},
        homeScreenViewModel = HomeScreenViewModel(MockCounterRepository())
    )
}