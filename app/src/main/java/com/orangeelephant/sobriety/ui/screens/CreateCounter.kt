package com.orangeelephant.sobriety.ui.screens

import android.provider.Settings.Global.getString
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.getString
import androidx.core.content.res.TypedArrayUtils.getString
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar

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

@Composable
fun Creation() {
    val context = LocalContext.current
    var text by remember { mutableStateOf("") }

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
            value = text,
            onValueChange = { text = it },
            label = { Text(context.getString(R.string.placeholder_counter_name)) }
        )

        Spacer(modifier = Modifier.height(16.dp))

        //date
        Text(
            text = context.getString(R.string.create_counter_start_date),
            style = MaterialTheme.typography.titleMedium,
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
            value = text,
            onValueChange = { text = it },
            label = { Text(context.getString(R.string.placeholder_counter_reason)) }
        )
    }
}

// create compostable preview
@Preview
@Composable
fun CreateCounterPreview() {
    CreateCounter(rememberNavController())
}
