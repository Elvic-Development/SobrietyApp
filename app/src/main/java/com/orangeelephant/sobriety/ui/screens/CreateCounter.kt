package com.orangeelephant.sobriety.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource

import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import java.text.DateFormat
import java.util.Date
import java.util.Locale



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCounter(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        topBar = { GenericTopAppBar(
            title = R.string.create_counter,
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackIcon(onClick = {
                navController.popBackStack()
            })
            }
        ) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            Creation()
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Creation() {
    val context = LocalContext.current
    var nameText by remember { mutableStateOf("")}
    var dateText by remember { mutableStateOf("")}
    var reasonText by remember { mutableStateOf("")}
    var isDialogOpen by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis())

    if (isDialogOpen) {
        DatePickerDialog(
            onDismissRequest = { isDialogOpen = false },
            confirmButton = {
                Button(
                    onClick = {
                        isDialogOpen = false
                        dateText = convertMillisecondsToDate(datePickerState.selectedDateMillis)
                    }
                ) {
                    Text(text = context.getString(R.string.submit_button))
                }
            },
            dismissButton = {
                Button(
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

        // name
        Text(
            text = context.getString(R.string.create_counter_name),
            style = MaterialTheme.typography.titleMedium,
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = nameText,
            onValueChange = { nameText = it },
            label = { Text(stringResource(R.string.placeholder_counter_name)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        //date
        Text(
            text = context.getString(R.string.create_counter_start_date),
            style = MaterialTheme.typography.titleMedium,
        )

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                value = dateText,
                onValueChange = { dateText = it },
                label = { Text(context.getString(R.string.placeholder_date)) }

            )


            IconButton(
                onClick = {
                    isDialogOpen = true
                },
                modifier = Modifier.padding(start = 8.dp, top = 8.dp) // Add padding to the left and top of the IconButton
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_calendar),
                    contentDescription = stringResource(id = R.string.ic_calendar)
                )
            }
        }




        Spacer(modifier = Modifier.height(16.dp))

        // reason
        Text(
            text = context.getString(R.string.create_counter_reason),
            style = MaterialTheme.typography.titleMedium,
        )


        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = reasonText,
            onValueChange = { reasonText = it },
            label = { Text(context.getString(R.string.placeholder_counter_reason)) }
        )
    }
}

fun convertMillisecondsToDate(utcMilliseconds: Long?): String {
    if (utcMilliseconds == null) {
        return ""
    }

    val dateFormat = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault())
    val date = Date(utcMilliseconds)
    return dateFormat.format(date)
}


// create compostable preview
@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun CreateCounterPreview() {
    CreateCounter(rememberNavController())
}
