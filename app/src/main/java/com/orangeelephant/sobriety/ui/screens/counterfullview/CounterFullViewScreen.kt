package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.storage.models.Relapse
import com.orangeelephant.sobriety.storage.repositories.mock.MockCounterRepository
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.ConfirmationDialog
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.MileStoneProgressTracker
import com.orangeelephant.sobriety.ui.common.SobrietyAlertDialog
import com.orangeelephant.sobriety.ui.common.WarningDialog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterFullView(
    counterFullScreenViewModel: CounterFullScreenViewModel,
    navController: NavController
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val showResetDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }

    val counter = remember { counterFullScreenViewModel.counter }

    counter.value?.let {
        Scaffold(
            topBar = {
                GenericTopAppBar(
                    title = it.name,
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        BackIcon(onClick = {
                            navController.popBackStack()
                        })
                    }
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                Row {
                    OutlinedButton(
                        onClick = { showDeleteDialog.value = true },
                        content = {
                            Text(text = stringResource(id = R.string.delete_button))
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .size(width = 170.dp, height = 50.dp)
                    )
                    Spacer(modifier = Modifier.width(30.dp))
                    Button(
                        onClick = { showResetDialog.value = true },
                        content = {
                            Text(text = stringResource(id = R.string.reset_button))
                        },
                        shape = RoundedCornerShape(50.dp),
                        modifier = Modifier
                            .size(width = 170.dp, height = 50.dp)
                    )
                }
            },
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                MileStoneProgressTracker(
                    counterFullScreenViewModel.duration.value
                )
                Spacer(modifier = Modifier.height(20.dp))

                LazyColumn (
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = innerPadding.calculateTopPadding(),
                            bottom = 0.dp
                        )
                ) {
                    items(counterFullScreenViewModel.relapses, key = { it.id }) { relapse ->
                        RelapseView(relapse)
                    }
                }
            }

            if (showResetDialog.value) {
                RecordRelapse(
                    onDismiss = { showResetDialog.value = false },
                    onConfirm = { comment ->
                        counterFullScreenViewModel.onResetCounter(comment)
                        showResetDialog.value = false
                    }
                )
            } else if (showDeleteDialog.value) {
                ConfirmationDialog(
                    onConfirm = {
                        counterFullScreenViewModel.onDeleteCounter(context, navController)
                        showDeleteDialog.value = false
                    },
                    onDismiss = { showDeleteDialog.value = false },
                    title = R.string.delete_counter,
                    description = R.string.delete_counter_description
                )
            }
        }
    } ?: run {
        WarningDialog(
            onDismiss = { navController.popBackStack() },
            title = R.string.couldnt_load_counter,
            description = R.string.couldnt_load_counter_description,
            confirmText = R.string.okay
        )
    }
}

@Composable
fun RecordRelapse(
    onDismiss: () -> Unit,
    onConfirm: (reason: String?) -> Unit
) {
    val relapseReason = remember { mutableStateOf("") }

    SobrietyAlertDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(id = R.string.record_relapse),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            Text(
                stringResource(id = R.string.record_relapse_description),
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = relapseReason.value,
                onValueChange = { relapseReason.value = it },
                label = { Text(stringResource(R.string.record_relapse_comment)) },
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                onClick = {
                    if (relapseReason.value == "") {
                        onConfirm(null)
                    } else {
                        onConfirm(relapseReason.value)
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(id = R.string.okay))
            }
        }
    }
}

@Composable
fun RelapseView(relapse: Relapse) {
    Row {
        Text("${relapse.time}")

        relapse.comment?.let {
            Text(it)
        } ?: run {
            Text("No comment", color = MaterialTheme.colorScheme.outline)
        }
    }
}

@Preview
@Composable
fun CounterFullPreview() {
    CounterFullView(
        counterFullScreenViewModel = CounterFullScreenViewModel(1, MockCounterRepository()),
        navController = rememberNavController()
    )
}
