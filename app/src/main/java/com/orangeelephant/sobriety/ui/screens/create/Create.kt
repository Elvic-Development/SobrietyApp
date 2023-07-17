package com.orangeelephant.sobriety.ui.screens.create

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.Screen
import com.orangeelephant.sobriety.storage.models.Counter
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.ClickableOutlinedTextField
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.CircleImagePicker
import com.orangeelephant.sobriety.ui.common.convertMillisecondsToDate
import com.orangeelephant.sobriety.storage.repositories.mock.MockCounterRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateScreen(
    navController: NavController,
    createScreenViewModel: CreateScreenViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val createConditionsMet = (createScreenViewModel.nameText != "") && (createScreenViewModel.dateVal != null)
    val context = LocalContext.current
    val datePickerState = rememberDatePickerState()
    var isDialogOpen by remember { mutableStateOf(false) }


    // date picker
    if (isDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = false
                        createScreenViewModel.dateVal = datePickerState.selectedDateMillis
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

    // UI
    Scaffold(
        topBar = {
            GenericTopAppBar(
                title = R.string.create_counter,
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackIcon(onClick = {
                        navController.popBackStack()
                    })
                }
            )
        },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
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
                        val newCounter = Counter(-1,
                            createScreenViewModel.nameText,
                            createScreenViewModel.dateVal!!,
                            0)
                        createScreenViewModel.onCreateCounter(
                            newCounter,
                            createScreenViewModel.reasonText,
                            onCounterCreated = {counterId -> navController.navigate(
                                route = Screen.CounterFullView.createRoute(counterId = counterId)
                            )}
                            )
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
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {

            // photo
            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                CircleImagePicker(
                    selectedImageUri = createScreenViewModel.selectedImageUri,
                    onImageSelected = { uri -> createScreenViewModel.selectedImageUri.value = uri }
                )
            }

            // user input
            Column (
                verticalArrangement = Arrangement.Bottom,
                modifier = Modifier
                    .padding(40.dp)
            ) {

                // name
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = createScreenViewModel.nameText,
                    onValueChange = { createScreenViewModel.nameText = it },
                    label = { Text(stringResource(R.string.placeholder_counter_name)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // date
                ClickableOutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value =  TextFieldValue(convertMillisecondsToDate(createScreenViewModel.dateVal)),
                    onClick = { isDialogOpen = true },
                    label = { Text(context.getString(R.string.placeholder_date)) }
                )

                Spacer(modifier = Modifier.height(8.dp))

                // reason
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth(),
                    value = createScreenViewModel.reasonText,
                    onValueChange = { createScreenViewModel.reasonText = it },
                    label = { Text(context.getString(R.string.placeholder_counter_reason)) }
                )
            }
        }
    }
}

// create compostable preview
@Preview
@Composable
fun CreateCounterPreview() {
    CreateScreen(
        navController = rememberNavController(),
        createScreenViewModel = CreateScreenViewModel(MockCounterRepository())
    )
}

