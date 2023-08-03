package com.orangeelephant.sobriety.ui.screens.counterfullview

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.storage.models.Reason
import com.orangeelephant.sobriety.storage.models.Relapse
import com.orangeelephant.sobriety.storage.repositories.mock.MockCounterRepository
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.ConfirmationDialog
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.MileStoneProgressTracker
import com.orangeelephant.sobriety.ui.common.SobrietyAlertDialog
import com.orangeelephant.sobriety.ui.common.WarningDialog
import com.orangeelephant.sobriety.util.CounterViewUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CounterFullView(
    counterFullScreenViewModel: CounterFullScreenViewModel,
    popBack: () -> Unit
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    val showResetDialog = remember { mutableStateOf(false) }
    val showDeleteDialog = remember { mutableStateOf(false) }
    val showAddReasonDialog = remember { mutableStateOf(false) }
    val menuExpanded = remember { mutableStateOf(false) }

    val counter = remember { counterFullScreenViewModel.counter }

    counter.value?.let {
        Scaffold(
            topBar = {
                GenericTopAppBar(
                    title = it.name,
                    scrollBehavior = scrollBehavior,
                    navigationIcon = {
                        BackIcon(onClick = {
                            popBack()
                        })
                    },
                    actions = { DropdownOptionsMenu(
                        menuExpanded,
                        showAddReasonDialog
                    )}
                )
            },
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            floatingActionButton = {
                Row {
                    OutlinedButton(
                        colors = ButtonDefaults.outlinedButtonColors(
                            containerColor = MaterialTheme.colorScheme.background
                        ),
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
            floatingActionButtonPosition = FabPosition.Center
        ) { innerPadding ->
            LazyColumn (
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.Start,
                contentPadding = PaddingValues(
                    bottom = innerPadding.calculateBottomPadding() + 100.dp
                )
            ) {
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Column (
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        MileStoneProgressTracker(
                            counterFullScreenViewModel.duration.value
                        )
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }

                if (counterFullScreenViewModel.reasons.size > 0) {
                    item {
                        Text(
                            stringResource(id = R.string.associated_reasons),
                            style = MaterialTheme.typography.headlineMedium
                        )
                    }
                    items(counterFullScreenViewModel.reasons) { reason ->
                        ReasonView(reason)
                    }
                }

                if (counterFullScreenViewModel.relapses.size > 0) {
                    item {
                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            stringResource(id = R.string.past_recorded_relapses),
                            style = MaterialTheme.typography.headlineMedium
                        )
                        // show initial start time before relapses
                        counterFullScreenViewModel.counter.value?.let {
                            if (it.initialStartTime != null) {
                                RelapseView(
                                    relapse = Relapse(
                                        -1,
                                        it.id,
                                        it.initialStartTime,
                                        stringResource(id = R.string.initial_start_time)
                                    )
                                )
                            }
                        }
                    }
                    items(counterFullScreenViewModel.relapses) { relapse ->
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
                        counterFullScreenViewModel.onDeleteCounter(context, popBack)
                        showDeleteDialog.value = false
                    },
                    onDismiss = { showDeleteDialog.value = false },
                    title = R.string.delete_counter,
                    description = R.string.delete_counter_description
                )
            } else if (showAddReasonDialog.value) {
                AddReason(
                    onDismiss = { showAddReasonDialog.value = false },
                    onAddReason = { reason ->
                        counterFullScreenViewModel.onAddReason(reason)
                        showAddReasonDialog.value = false
                    }
                )
            }
        }
    } ?: run {
        WarningDialog(
            onDismiss = { popBack() },
            title = R.string.couldnt_load_counter,
            description = R.string.couldnt_load_counter_description,
            confirmText = R.string.okay
        )
    }
}

@Composable
fun DropdownOptionsMenu(
    menuExpanded: MutableState<Boolean>,
    showAddReason: MutableState<Boolean>
) {
    IconButton(onClick = {
        menuExpanded.value = !menuExpanded.value
    }) {
        Icon(
            imageVector = Icons.Filled.MoreVert,
            contentDescription = stringResource(id = R.string.more_options)
        )
    }
    DropdownMenu(
        expanded = menuExpanded.value,
        onDismissRequest = { menuExpanded.value = false }
    ) {
        DropdownMenuItem(
            text = {
                Text(stringResource(id = R.string.add_a_reason))
            },
            onClick = {
                showAddReason.value = true
                menuExpanded.value = false
            },
        )
    }
}

@Composable
fun AddReason(
    onDismiss: () -> Unit,
    onAddReason: (newReason: String) -> Unit
) {
    val newReason = remember { mutableStateOf("") }

    SobrietyAlertDialog(onDismiss = onDismiss) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                stringResource(id = R.string.record_relapse),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.titleLarge
            )
            Spacer(modifier = Modifier.height(5.dp))
            OutlinedTextField(
                value = newReason.value,
                onValueChange = { newReason.value = it },
                label = { Text(stringResource(R.string.reason_input)) },
                maxLines = 4
            )
            Spacer(modifier = Modifier.height(10.dp))
            TextButton(
                enabled = newReason.value != "",
                onClick = { onAddReason(newReason.value) },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text(stringResource(id = R.string.okay))
            }
        }
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
    Spacer(modifier = Modifier.height(5.dp))
    Row {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            CounterViewUtil.convertMillisecondsToDate(relapse.time),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.width(70.dp)
        )

        Spacer(modifier = Modifier.width(10.dp))
        relapse.comment?.let {
            Text(
                it,
                style = MaterialTheme.typography.bodyLarge,
            )
        } ?: run {
            Text(
                "No comment",
                color = MaterialTheme.colorScheme.outline,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Composable
fun ReasonView(reason: Reason) {
    Spacer(modifier = Modifier.height(5.dp))
    Row {
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            reason.reason,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

@Preview
@Composable
fun CounterFullPreview() {
    CounterFullView(
        counterFullScreenViewModel = CounterFullScreenViewModel(1, MockCounterRepository()),
        popBack = {}
    )
}
