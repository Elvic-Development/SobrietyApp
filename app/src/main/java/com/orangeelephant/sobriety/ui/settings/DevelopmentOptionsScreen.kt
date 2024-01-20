package com.orangeelephant.sobriety.ui.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.jamal.composeprefs3.ui.GroupHeader
import com.jamal.composeprefs3.ui.PrefsScreen
import com.jamal.composeprefs3.ui.prefs.TextPref
import com.orangeelephant.sobriety.R
import com.orangeelephant.sobriety.ui.common.BackIcon
import com.orangeelephant.sobriety.ui.common.GenericTopAppBar
import com.orangeelephant.sobriety.util.dataStore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DevelopmentOptionsScreen(
    popBack: () -> Unit,
    viewModel: DevelopmentOptionsViewModel = hiltViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    Scaffold (
        topBar = { GenericTopAppBar (
            title = R.string.development_settings,
            scrollBehavior = scrollBehavior,
            navigationIcon = { BackIcon(onClick = popBack) }
        )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
    ) { innerPadding ->
        PrefsScreen(
            dataStore = LocalContext.current.dataStore,
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = 0.dp
            )
        ) {
            prefsGroup({
                GroupHeader(title = stringResource(id = R.string.app_setup))
            }) {
                prefsItem {
                    TextPref (
                        title = stringResource(id = R.string.mark_setup_incomplete),
                        summary = stringResource(id = R.string.mark_setup_incomplete_description),
                        onClick = { viewModel.onMarkSetupIncomplete() },
                        enabled = true
                    )
                }
            }
        }
    }
}