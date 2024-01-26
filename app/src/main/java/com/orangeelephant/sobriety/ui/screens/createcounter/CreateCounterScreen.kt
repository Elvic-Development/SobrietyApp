package com.orangeelephant.sobriety.ui.screens.createcounter

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.ClickableOutlinedTextField
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.storage.repositories.mock.MockCounterRepository
import com.orangeelephant.sobriety.util.DateTimeFormatUtil

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCounterScreen(
    popBack: () -> Unit,
    navigateToCounterFullView: (Int) -> Unit,
    createScreenViewModel: CreateCounterScreenViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val createConditionsMet = createScreenViewModel.nameText != ""
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()
    var isDialogOpen by remember { mutableStateOf(false) }
    var numberOfReasonsInputFields by remember { mutableIntStateOf(1) }

    val reasonValues = remember { mutableStateListOf<TextFieldValue>() }
    
    if (isDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = false
                        createScreenViewModel.dateVal = datePickerState.selectedDateMillis ?: run {
                            System.currentTimeMillis()
                        }
                    }
                ) {
                    Text(text = context.getString(R.string.submit_button))
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { isDialogOpen = false },
                ) {
                    Text(text = context.getString(R.string.cancel_button))
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Scaffold(
        topBar = {
            GenericTopAppBar(
                title = R.string.create_counter,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackIcon(onClick = popBack)
                }
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                OutlinedButton(
                    onClick = popBack,
                    content = {
                        Text(text = stringResource(id = R.string.cancel_button))
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .size(width = 170.dp, height = 50.dp)
                )

                Spacer(modifier = Modifier.width(30.dp))

                Button(
                    enabled = createConditionsMet,
                    onClick = {
                        createScreenViewModel.onCreateCounter(
                            createScreenViewModel.nameText,
                            createScreenViewModel.dateVal,
                            reasonValues.toList()
                        ) { counterId ->
                            navigateToCounterFullView(counterId)
                        }
                    },
                    content = {
                        Text(text = stringResource(id = R.string.create_button))
                    },
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier
                        .size(width = 170.dp, height = 50.dp)
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center

    ) { innerPadding ->
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding() + 40.dp, bottom = 0.dp)
                .padding(horizontal = 40.dp)
                .verticalScroll(rememberScrollState())
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = createScreenViewModel.nameText,
                onValueChange = { createScreenViewModel.nameText = it },
                label = { Text(stringResource(R.string.placeholder_counter_name)) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            val dateText = TextFieldValue(DateTimeFormatUtil.formatDate(context,
                createScreenViewModel.dateVal
            ))

            ClickableOutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = dateText,
                onClick = { isDialogOpen = true },
                label = { Text(context.getString(R.string.placeholder_date)) }
            )
            Spacer(modifier = Modifier.height(8.dp))

            if (numberOfReasonsInputFields > reasonValues.size) {
                repeat(numberOfReasonsInputFields - reasonValues.size) {
                    reasonValues.add(TextFieldValue())
                }
            }

            AnimatedContent(
                targetState = numberOfReasonsInputFields,
                label = "Animate presence of reason input boxes"
            ) { targetState ->
                Column {
                    for (i in 0 until targetState) {
                        OutlinedTextField(
                            modifier = Modifier
                                .fillMaxWidth(),
                            value = reasonValues[i],
                            onValueChange = { reasonValues[i] = it },
                            label = {
                                if (i == 0) {
                                    Text(stringResource(R.string.placeholder_counter_reason))
                                } else {
                                    Text(
                                        stringResource(
                                            R.string.placeholder_counter_reason_numbered,
                                            i + 1
                                        )
                                    )
                                }
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            AnimatedVisibility(visible = reasonValues[0] != TextFieldValue("")) {
                Button(
                    onClick = { numberOfReasonsInputFields++ }
                ) {
                    Text(text = context.getString(R.string.add_additional_reason))
                }
            }
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))
        }
    }
}

@Preview
@Composable
fun CreateCounterPreview() {
    CreateCounterScreen(
        popBack = {},
        navigateToCounterFullView = {},
        createScreenViewModel = CreateCounterScreenViewModel(MockCounterRepository())
    )
}

