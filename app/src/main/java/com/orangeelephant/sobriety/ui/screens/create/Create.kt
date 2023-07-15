package com.orangeelephant.sobriety.ui.screens.create

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.ui.common.SinglePhotoPicker
import java.text.DateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCounter(
    navController: NavController,
    createViewModel: CreateViewModel = viewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

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
                    enabled = createViewModel.createConditionsMet,
                    onClick = {
                              /*TODO*/
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
        floatingActionButtonPosition = FabPosition.Center,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {

            Creation(CreateViewModel())
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Creation(
    createViewModel: CreateViewModel = viewModel()
) {
    val context = LocalContext.current
    val packageName = context.packageName
    val datePickerState = rememberDatePickerState()
    var isDialogOpen by remember { mutableStateOf(false) }

    if (isDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = false
                        createViewModel.dateVal = datePickerState.selectedDateMillis
                        createViewModel.dateText = convertMillisecondsToDate(datePickerState.selectedDateMillis)

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

    Column(
        verticalArrangement = Arrangement.Top,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(8.dp))

        // photo

        SinglePhotoPicker(
            selectedImageUri = createViewModel.selectedImageUri,
            onImageSelected = { uri ->
                createViewModel.selectedImageUri.value = uri
            }
        )
    }

    Column (
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            ) {

        // name
        Text(
            text = context.getString(R.string.create_counter_name),
            style = MaterialTheme.typography.titleSmall,
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = createViewModel.nameText,
            onValueChange = { createViewModel.nameText = it },
            label = { Text(stringResource(R.string.placeholder_counter_name)) }
        )

        Spacer(modifier = Modifier.height(20.dp))

        //date
        Text(
            text = context.getString(R.string.create_counter_start_date),
            style = MaterialTheme.typography.titleSmall,
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(onClick = { isDialogOpen = true }),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = createViewModel.dateText,
                onValueChange = { createViewModel.dateText = it.take(10) },
                label = { Text(context.getString(R.string.placeholder_date)) },
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // reason
        Text(
            text = context.getString(R.string.create_counter_reason),
            style = MaterialTheme.typography.titleSmall,
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = createViewModel.reasonText,
            onValueChange = { createViewModel.reasonText = it },
            label = { Text(context.getString(R.string.placeholder_counter_reason)) }
        )

    }
}

//utcmillisecond to date
fun convertMillisecondsToDate(utcMilliseconds: Long?): String {
    if (utcMilliseconds == null) {
        return ""
    }
    val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    val date = Date(utcMilliseconds)
    return dateFormat.format(date)
}

// create compostable preview
@Preview
@Composable
fun CreateCounterPreview() {
    CreateCounter(rememberNavController())
}

