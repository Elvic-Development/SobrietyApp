package com.orangeelephant.sobriety

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.NavHost
import com.orangeelephant.sobriety.ui.screens.HomeScreen

sealed class Screen(val route: String) {
    // Home screen
    object Home: Screen("home")

    // Add counter screen
    object AddCounter: Screen("addCounter")
}


@Composable
fun SobrietyAppNavigation(navController: NavHostController, context: Context) {
    NavHost(navController, startDestination = Screen.Home.toString()) {
        addHomeNavigation(context)
        addCreateCounterNavigation()
    }
}

fun NavGraphBuilder.addHomeNavigation(context: Context) {
    composable(route = Screen.Home.toString()) {
        HomeScreen(context = context)
    }
}

fun NavGraphBuilder.addCreateCounterNavigation() {

}