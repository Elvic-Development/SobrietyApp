package com.orangeelephant.sobriety.ui.screens.export

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.CreateDocument
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.FormatTitleAndDescription
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
                .fillMaxSize()
        ) {
            FormatTitleAndDescription(title = R.string.export_database, description = R.string.export_plaintext_description)
            Spacer(modifier = Modifier.fillMaxHeight(0.15f))

            Button(
                onClick = { exportScreenViewModel.onLaunchSelectFolder(selectOutputFileLauncher, context) },
                shape = MaterialTheme.shapes.small.copy(CornerSize(percent = 25)),
                modifier = Modifier.size(width = 250.dp, height = 50.dp)
            ) {
                Text(stringResource(id = R.string.tap_to_select_export_file))
            }
        }
    }
}