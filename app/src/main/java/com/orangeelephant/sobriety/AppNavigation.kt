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
import com.orangeelephant.sobriety.ui.screens.CounterFullView
import com.orangeelephant.sobriety.ui.screens.CreateCounter
import com.orangeelephant.sobriety.ui.screens.home.HomeScreen
import com.orangeelephant.sobriety.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    // Home screen
    object Home: Screen("home")

    // Counter full view
    object CounterFullView: Screen("counterFullView/{counterId}") {
        fun createRoute(counterId: Int) = "counterFullView/$counterId"
    }

    // Add counter screen
    object AddCounter: Screen("addCounter")

    // Settings screen
    object Settings: Screen("settings")
}


@Composable
fun SobrietyAppNavigation(
    navController: NavHostController,
    context: Context
) {
    NavHost(navController, startDestination = Screen.Home.route) {
        addHomeNavigation(navController)
        addCounterFullViewNavigation(context)
        addCreateCounterNavigation(navController)
        addSettingsNavigation(navController)
    }
}

fun NavGraphBuilder.addHomeNavigation(navController: NavHostController) {
    composable(route = Screen.Home.route) {
        HomeScreen(navController)
    }
}

fun NavGraphBuilder.addCounterFullViewNavigation(context: Context) {
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
            CounterFullView(counterId = counterId)
        } ?: run {
            Toast.makeText(context, "No counterID provided", Toast.LENGTH_LONG).show()
        }
    }
}

fun NavGraphBuilder.addCreateCounterNavigation(navController: NavHostController) {
    composable(route = Screen.AddCounter.route) {
        CreateCounter(navController = navController)
    }

}

fun NavGraphBuilder.addSettingsNavigation(navController: NavHostController) {
    composable(route = Screen.Settings.route) {
        SettingsScreen(navController = navController)
    }
}
