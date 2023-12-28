package com.orangeelephant.sobriety.ui.screens.export

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExportScreen(
    popBack: () -> Unit,
    exportScreenViewModel: ExportScreenViewModel = viewModel()
) {
    val context = LocalContext.current
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val selectOutputFileLauncher = rememberLauncherForActivityResult(
        CreateDocument("application/x-sqlite3")
    ) { uri ->
        uri?.let {
            exportScreenViewModel.onExportPlaintextDatabase(it, context)
        } ?: run {
            Toast.makeText(context, context.getString(R.string.failed_to_select_file_location), Toast.LENGTH_LONG).show()
        }
    }

    val selectImportDbFileLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri ->
        uri?.let {
            exportScreenViewModel.onImportPlaintextDatabase(uri, context)
        }
        // TODO error msg
    }

    Scaffold (
        topBar = { GenericTopAppBar (
            title = R.string.export,
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackIcon(onClick = popBack)}
        )},
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    ) { innerPadding ->
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 30.dp)
                .fillMaxSize()
        ) {
            Button(onClick = {
                exportScreenViewModel.onLaunchSelectFolder(selectOutputFileLauncher)
            }) {

            }

            Button(onClick = {
                exportScreenViewModel.onLaunchSelectImportFile(selectImportDbFileLauncher)
            }) {

            }
        }
    }
}