package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.ConfirmationDialog
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterFullView(
    counterFullScreenViewModel: CounterFullScreenViewModel,
    navController: NavController
) {
    val counter = counterFullScreenViewModel.counter
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val showResetDialog = remember { mutableStateOf(false) }

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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = { showResetDialog.value = true },
                content = {
                    Text(text = stringResource(id = R.string.reset_button))
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .size(width = 170.dp, height = 50.dp)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            Text(text = counter.value.currentDurationString)
        }

        if (showResetDialog.value) {
            ConfirmationDialog(
                onConfirm = {
                    counterFullScreenViewModel.onResetCounter()
                    showResetDialog.value = false
                },
                onDismiss = { showResetDialog.value = false },
                title = R.string.reset_counter,
                description = R.string.reset_timer_description
            )
        }
    }
}