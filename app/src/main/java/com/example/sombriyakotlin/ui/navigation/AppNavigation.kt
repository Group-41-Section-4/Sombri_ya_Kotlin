package com.example.sombriyakotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sombriyakotlin.feature.notifications.NotificationsScreen
import com.example.sombriyakotlin.ui.account.CardProfile
import com.example.sombriyakotlin.ui.account.login.LoginScreen
import com.example.sombriyakotlin.ui.account.signup.SignUpScreen
import com.example.sombriyakotlin.ui.home.CardHome
import com.example.sombriyakotlin.ui.main.CardStations
import com.example.sombriyakotlin.ui.main.MainWithDrawer
import com.example.sombriyakotlin.ui.rent.CardRent
import com.example.sombriyakotlin.ui.rent.MainRenta

// Definimos las rutas de navegación
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"

    const val MAIN = "main"

    const val RENT = "rent"

    const val STATIONS = "stations"

    const val PROFILE = "profile"

    const val PAYMENT_METHODS = "paymentMethods"

    const val NOTIFICATIONS = "notifications"
}

@Composable
fun AppNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Routes.HOME
    ) {
        composable(Routes.LOGIN) {
            LoginScreen(
                onNavigateToSignUp = {
                    navController.navigate(Routes.SIGNUP)
                },
                onContinue = {
                    // Comentado para futura implementación
                    navController.navigate(Routes.MAIN)
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
                    navController.navigate(Routes.MAIN)
                },

            )
        }
        

        composable(Routes.HOME) {
            CardHome(
                navController
            )
        }

        composable(Routes.MAIN) {
            MainWithDrawer(navController)
        }

        composable(Routes.RENT) {
            MainRenta(navController)
        }

        composable(Routes.STATIONS) {
            CardStations(navController)
        }

        composable(Routes.PROFILE) {
            CardProfile(navController)
        }

        composable(Routes.NOTIFICATIONS) {
            NotificationsScreen(navController)
        }

    }
}