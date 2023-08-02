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
import com.orangeelephant.sobriety.ui.screens.CreateCounter
import com.orangeelephant.sobriety.ui.screens.unlock.UnlockScreen
import com.orangeelephant.sobriety.ui.screens.counterfullview.CounterFullScreenViewModel
import com.orangeelephant.sobriety.ui.screens.home.HomeScreen
import com.orangeelephant.sobriety.ui.settings.SettingsScreen

sealed class Screen(val route: String) {
    object Unlock: Screen("unlock")
    object Home: Screen("home")
    object CounterFullView: Screen("counterFullView/{counterId}") {
        fun createRoute(counterId: Int) = "counterFullView/$counterId"
    }
    object AddCounter: Screen("addCounter")
    object Settings: Screen("settings")
}


@Composable
fun SobrietyAppNavigation(
    navController: NavHostController,
    context: Context
) {
    val startDestination = Screen.Unlock.route

    NavHost(navController, startDestination = startDestination) {
        addUnlockNavigation(navController)
        addHomeNavigation(navController)
        addCounterFullViewNavigation(context, navController)
        addCreateCounterNavigation(navController)
        addSettingsNavigation(navController)
    }
}

fun NavGraphBuilder.addUnlockNavigation(navController: NavHostController) {
    composable(route = Screen.Unlock.route) {
        UnlockScreen(
            navigateToHome = {
                navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Unlock.route) {
                        inclusive = true
                    }
                }
            }
        )
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
                popBack = { navController.popBackStack() }
            )
        } ?: run {
            Toast.makeText(context, "No counterID provided", Toast.LENGTH_LONG).show()
        }
    }
}

fun NavGraphBuilder.addCreateCounterNavigation(navController: NavHostController) {
    composable(route = Screen.AddCounter.route) {
        CreateCounter(popBack = { navController.popBackStack() })
    }

}

fun NavGraphBuilder.addSettingsNavigation(navController: NavHostController) {
    composable(route = Screen.Settings.route) {
        SettingsScreen(popBack = { navController.popBackStack() })
    }
}
