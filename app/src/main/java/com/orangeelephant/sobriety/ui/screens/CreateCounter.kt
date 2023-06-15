package com.orangeelephant.sobriety.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
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
    var createConditionsMet by remember { mutableStateOf(false) }

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
                    onClick = { /*TODO*/ },
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
    val packageName =  context.packageName
    var selectedImageUri by remember { mutableStateOf<Uri?>(Uri.parse("android.resource://$packageName/" + R.drawable.image1)) }


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
        SinglePhotoPicker(selectedImageUri) { uri ->
            selectedImageUri = uri
        }

        Spacer(modifier = Modifier.height(64.dp))

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

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),

            value = dateText,
            onValueChange = { dateText = it },
            label = { Text(context.getString(R.string.placeholder_date))},

            trailingIcon = {
                IconButton(
                    onClick = {
                        isDialogOpen = true
                    },
                    modifier = Modifier
                        .scale(0.7f)
                ) {
                    Icon( painter = painterResource(id = R.drawable.ic_calendar), contentDescription = stringResource(id = R.string.ic_calendar))
                }
            }
        )

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

        Spacer(modifier = Modifier.height(16.dp))
    }

}

@Composable
fun SinglePhotoPicker(selectedImageUri: Uri?, onImageSelected: (Uri?) -> Unit) {
    val singlePhotoDialog = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> onImageSelected(uri) }
    )

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .clickable {
                    singlePhotoDialog.launch(
                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                    )
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = selectedImageUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
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
@Preview
@Composable
fun CreateCounterPreview() {
    CreateCounter(rememberNavController())
}

