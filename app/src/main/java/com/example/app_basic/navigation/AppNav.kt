package com.example.app_basic.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app_basic.screens.HomeScreen
import com.example.app_basic.screens.LoginScreen
import com.example.app_basic.screens.RegisterScreen

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home/{fullName}") {
        fun createRoute(fullName: String) = "home/$fullName"
    }
}

@Composable
fun AppNav(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {

        composable(Screen.Login.route) {
            LoginScreen(navController)
        }

        composable(Screen.Register.route) {
            RegisterScreen(navController)
        }

        composable(
            route = "home/{fullName}",
            arguments = listOf(
                navArgument("fullName") {
                    type = NavType.StringType
                    defaultValue = "Người dùng"
                }
            )
        ) { backStackEntry ->
            val fullName = backStackEntry.arguments?.getString("fullName") ?: "Người dùng"
            HomeScreen(navController, fullName)
        }
    }
}