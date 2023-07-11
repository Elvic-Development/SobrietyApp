package com.orangeelephant.sobriety.ui.screens.create

import android.net.Uri
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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import java.net.URI

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
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        // photo
        SinglePhotoPicker(createViewModel.selectedImageUri)
        { uri ->
            createViewModel.selectedImageUri = uri
        }

        if (createViewModel.selectedImageUri == null) {
            createViewModel.selectedImageUri = Uri.parse("android.resource://$packageName/${R.drawable.image1}")
        }

        Spacer(modifier = Modifier.height(64.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_no_drink),
                contentDescription = stringResource(id = R.string.ic_no_drink),
                modifier = Modifier
                    .scale(0.80f)
                    .padding(end = 8.dp)

            )
            OutlinedTextField(
                modifier = Modifier
                    .weight(1f),
                value = createViewModel.nameText,
                onValueChange = { createViewModel.nameText = it },
                label = { Text(stringResource(R.string.placeholder_counter_name)) }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_calendar),
                contentDescription = stringResource(id = R.string.ic_calendar),
                modifier = Modifier
                    .scale(0.80f)
                    .padding(end = 8.dp)

            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = createViewModel.getDateText(),
                onValueChange = { /*TODO*/ },
                label = { Text(context.getString(R.string.placeholder_date)) },

                trailingIcon = {
                    IconButton(
                        onClick = {
                            isDialogOpen = true
                        },
                        modifier = Modifier
                            .scale(0.7f)
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit_calendar),
                            contentDescription = stringResource(id = R.string.ic_calendar)
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
           Icon(
                painter = painterResource(id = R.drawable.ic_notes),
                contentDescription = stringResource(id = R.string.ic_calendar),
                modifier = Modifier
                    .scale(0.80f)
                    .padding(end = 8.dp)

            )
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth(),
                value = createViewModel.reasonText,
                onValueChange = { createViewModel.reasonText = it },
                label = { Text(context.getString(R.string.placeholder_counter_reason)) }
            )
        }



        Spacer(modifier = Modifier.height(16.dp))
    }
}

// create compostable preview
@Preview
@Composable
fun CreateCounterPreview() {
    CreateCounter(rememberNavController())
}

