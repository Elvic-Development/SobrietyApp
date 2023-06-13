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
import com.jamal.composeprefs3.ui.GroupHeader
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.TextPref
import com.orangeelephant.sobriety.BuildConfig
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.util.preferencesDataStore

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
        PrefsScreen(
            dataStore = LocalContext.current.preferencesDataStore,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = 0.dp
            )
        ) {
            prefsGroup({
                GroupHeader(title = stringResource(id = R.string.about))
            }) {
                prefsItem {
                    TextPref(
                        title = stringResource(id = R.string.version),
                        summary = BuildConfig.VERSION_NAME
                    )

                    val context = LocalContext.current
                    val sourceCodeIntent = Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse(stringResource(id = R.string.source_code_url))
                    )
                    TextPref(
                        title = stringResource(id = R.string.source_code),
                        summary = stringResource(id = R.string.source_code_description),
                        onClick = { context.startActivity(sourceCodeIntent) },
                        enabled = true
                    )
                }
            }
        }
    }
}
