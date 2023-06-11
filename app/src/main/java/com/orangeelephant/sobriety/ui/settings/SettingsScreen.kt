package com.orangeelephant.sobriety.ui.settings

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.orangeelephant.sobriety.BuildConfig
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(navController: NavController) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        topBar = { GenericTopAppBar (
            title = R.string.settings,
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackIcon(onClick = {
                navController.popBackStack()
            })}
        )},
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ){
            About()
        }
    }
}

@Composable
fun DisplaySettings() {

}

@Composable
fun SecuritySettings() {

}

@Composable
fun About() {
    val context = LocalContext.current
    val sourceCodeIntent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse(stringResource(id = R.string.source_code_url))
    )

    Divider(modifier = Modifier.padding(vertical = 5.dp))
    SettingsRow(
        title = stringResource(id = R.string.version),
        description = BuildConfig.VERSION_NAME
    )
    SettingsRow(
        title = R.string.source_code,
        description = R.string.source_code_description,
        onClick = {
            context.startActivity(sourceCodeIntent)
        }
    )
}
