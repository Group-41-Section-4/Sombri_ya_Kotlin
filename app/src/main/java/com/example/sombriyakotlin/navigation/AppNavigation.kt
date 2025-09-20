package com.example.sombriyakotlin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sombriyakotlin.feature.account.login.LoginScreen
import com.example.sombriyakotlin.feature.account.signup.SignUpScreen
import com.example.sombriyakotlin.screens.HomeScreen

// Definimos las rutas de navegación
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.LOGIN
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGNUP)
                },
                onContinue = {
                    // Comentado para futura implementación
                    // navController.navigate(Routes.HOME)
                }
            )
        }
        
        composable(Routes.SIGNUP) {
            SignUpScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onContinue = {
                    // Comentado para futura implementación
                    // navController.navigate(Routes.HOME)
                }
            )
        }
        
        // Ruta comentada para futura implementación
        /*
        composable(Routes.HOME) {
            HomeScreen(
                onLogout = {
                    navController.navigate(Routes.LOGIN) {
                        popUpTo(Routes.LOGIN) { inclusive = true }
                    }
                }
            )
        }
        */
    }
}