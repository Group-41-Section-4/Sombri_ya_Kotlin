package com.example.sombriyakotlin.ui.account.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.example.sombriyakotlin.ui.account.login.LoginScreen
import com.example.sombriyakotlin.ui.account.signup.SignUpScreen
import com.example.sombriyakotlin.ui.home.CardHome
import com.example.sombriyakotlin.ui.navigation.Routes

object AuthRoutes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val SPLASH = "home"

}

fun NavGraphBuilder.authGraph(navController: NavHostController) {
    composable(AuthRoutes.SPLASH) {
        CardHome(navController)
    }
    composable(AuthRoutes.LOGIN) { LoginScreen(
        onNavigateToSignUp = {
            navController.navigate(AuthRoutes.SIGNUP)
        },
        onContinue = {
            // Navigate to main and pop login
            navController.navigate(Routes.MAIN_GRAPH) {
                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                launchSingleTop = true
            }
        }) }
    composable(AuthRoutes.SIGNUP) { SignUpScreen(
        onNavigateBack = {
            navController.popBackStack()
        },
        onContinue = {
            // Navigate to main and pop singup
            navController.navigate(Routes.MAIN_GRAPH) {
                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                launchSingleTop = true
            }
        }) }
}
