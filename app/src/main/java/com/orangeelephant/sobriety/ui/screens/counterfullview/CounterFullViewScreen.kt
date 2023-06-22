package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterFullView(
    counterFullScreenViewModel: CounterFullScreenViewModel,
    navController: NavController
) {
    val counter = counterFullScreenViewModel.counter
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        topBar = { GenericTopAppBar(
            title = counter.value.name,
            scrollBehavior = scrollBehavior,
            navigationIcon = {
                BackIcon(onClick = {
                    navController.popBackStack()
                })
            }
        ) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            Text(text = counter.value.currentDurationString)
        }
    }
}