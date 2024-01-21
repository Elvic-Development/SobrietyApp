package com.orangeelephant.sobriety

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import androidx.navigation.navArgument
import com.orangeelephant.sobriety.ui.screens.counterfullview.CounterFullView
import com.orangeelephant.sobriety.ui.screens.counterfullview.CounterFullScreenViewModel
import com.orangeelephant.sobriety.ui.screens.createcounter.CreateCounterScreen
import com.orangeelephant.sobriety.ui.screens.export.ExportScreen
import com.orangeelephant.sobriety.ui.screens.home.HomeScreen
import com.orangeelephant.sobriety.ui.screens.initialsetup.InitialSetupScreen
import com.orangeelephant.sobriety.ui.screens.settings.DevelopmentOptionsScreen
import com.orangeelephant.sobriety.ui.screens.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Home: Screen("home")
    object CounterFullView: Screen("counterFullView/{counterId}") {
        fun createRoute(counterId: Int) = "counterFullView/$counterId"
    }
    object AddCounter: Screen("addCounter")
    object Settings: Screen("settings")
    object DevelopmentSettings: Screen("developmentSettings")
    object Export: Screen("export")
    object InitialSetup: Screen("setup")
}


@Composable
fun SobrietyAppNavigation(
    navController: NavHostController,
    context: Context,
    isInSetup: Boolean
) {
    val startDestination = Screen.Home.route
    NavHost(navController, startDestination = startDestination) {
        addHomeNavigation(navController)
        addCounterFullViewNavigation(context, navController)
        addCreateCounterNavigation(navController)
        addSettingsNavigation(navController)
        addExportDatabaseNavigation(navController)
        addInitialSetupNavigation(navController)
    }

    if (isInSetup) {
        navController.navigate(Screen.InitialSetup.route)
    }
}

fun NavGraphBuilder.addHomeNavigation(navController: NavHostController) {
    composable(route = Screen.Home.route) {
        HomeScreen(
            onClickSettings = { navController.navigate(route = Screen.Settings.route) },
            onClickAddCounter = { navController.navigate(route = Screen.AddCounter.route) },
            onOpenCounter = { counterId: Int ->
                navController.navigate(
                    route = Screen.CounterFullView.createRoute(counterId = counterId)
                )
            }
        )
    }
}

fun NavGraphBuilder.addCounterFullViewNavigation(context: Context, navController: NavHostController) {
    composable(
        route = Screen.CounterFullView.route,
        arguments = listOf(
            navArgument("counterId") {
                type = NavType.IntType
            }
        )
    ) {backStackEntry ->
        val counterId = backStackEntry.arguments?.getInt("counterId")

        counterId?.let {
            CounterFullView(
                counterFullScreenViewModel = CounterFullScreenViewModel(counterId),
                popBack = { navController.navigate(Screen.Home.route) }
            )
        } ?: run {
            Toast.makeText(context, "No counterID provided", Toast.LENGTH_LONG).show()
        }
    }
}

fun NavGraphBuilder.addCreateCounterNavigation(navController: NavHostController) {
    composable(route = Screen.AddCounter.route) {
        CreateCounterScreen(
            popBack = { navController.popBackStack() },
            navigateToCounterFullView = { counterId ->
                navController.navigate(
                    route = Screen.CounterFullView.createRoute(counterId)
                )
            }
        )
    }

}

fun NavGraphBuilder.addSettingsNavigation(navController: NavHostController) {
    composable(route = Screen.Settings.route) {
        SettingsScreen(
            popBack = { navController.popBackStack() },
            onNavigateToExport = { navController.navigate(route = Screen.Export.route) },
            onNavigateToDevlopmentOptions = { navController.navigate(route = Screen.DevelopmentSettings.route)}
        )
    }

    composable(route = Screen.DevelopmentSettings.route) {
        DevelopmentOptionsScreen(
            popBack = { navController.popBackStack() }
        )
    }
}

fun NavGraphBuilder.addExportDatabaseNavigation(navController: NavHostController) {
    composable(route = Screen.Export.route) {
        ExportScreen(popBack = { navController.popBackStack() })
    }
}

fun NavGraphBuilder.addInitialSetupNavigation(navController: NavHostController) {
    composable(route = Screen.InitialSetup.route) {
        InitialSetupScreen(onNavigateToHome = { navController.navigate(Screen.Home.route) })
    }
}
