package com.example.sombriyakotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sombriyakotlin.ui.account.CardProfile
import com.example.sombriyakotlin.ui.account.login.LoginScreen
import com.example.sombriyakotlin.ui.account.signup.SignUpScreen
import com.example.sombriyakotlin.feature.home.CardHome
import com.example.sombriyakotlin.feature.main.CardEstaciones
import com.example.sombriyakotlin.feature.main.CardMain
import com.example.sombriyakotlin.feature.rent.CardRent

// Definimos las rutas de navegación
object Routes {
    const val LOGIN = "login"
    const val SIGNUP = "signup"
    const val HOME = "home"

    const val MAIN = "main"

    const val RENT = "rent"

    const val STATIONS = "stations"

    const val PROFILE = "profile"
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
            CardMain(navController)
        }

        composable(Routes.RENT) {
            CardRent(navController)
        }

        composable(Routes.STATIONS) {
            CardEstaciones(navController)
        }

        composable(Routes.PROFILE) {
            CardProfile(navController)
        }


    }
}