package com.example.sombriyakotlin.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sombriyakotlin.feature.history.HistoryScreen
import com.example.sombriyakotlin.feature.notifications.NotificationsScreen
import com.example.sombriyakotlin.ui.account.CardProfile
import com.example.sombriyakotlin.ui.account.login.LoginScreen
import com.example.sombriyakotlin.ui.account.signup.SignUpScreen
import com.example.sombriyakotlin.ui.home.CardHome
import com.example.sombriyakotlin.ui.main.CardStations
import com.example.sombriyakotlin.ui.main.MainWithDrawer
import com.example.sombriyakotlin.ui.paymentMethods.paymentMethopdsCard
import com.example.sombriyakotlin.ui.rent.MainRenta
import androidx.navigation.NavOptionsBuilder

// Definimos las rutas de navegación
object Routes {
    const val AUTH_GRAPH = "auth_graph"
    const val MAIN_GRAPH = "main_graph"

    const val MAIN = "main"

    const val RENT = "rent"

    const val STATIONS = "stations"

    const val PROFILE = "profile"

    const val PAYMENT_METHODS = "paymentMethods"

    const val NOTIFICATIONS = "notifications"
    const val HISTORY = "history"
}

/**
 * Helper mínimo para navegar evitando duplicados (launchSingleTop) y opcionalmente hacer popUpTo.
 * Uso:
 *  navController.navigateSingleTop(Routes.MAIN, popUpTo = Routes.LOGIN, inclusive = true)
 */
fun NavHostController.navigateSingleTop(
    route: String,
    popUpTo: String? = null,
    inclusive: Boolean = false,
    builder: (NavOptionsBuilder.() -> Unit)? = null
) {
    this.navigate(route) {
        launchSingleTop = true
        restoreState = true
        // Si se especifica popUpTo, lo aplicamos (útil para limpiar auth después del login)
        if (popUpTo != null) {
            popUpTo(popUpTo) { this.inclusive = inclusive; saveState = true }
        }
        builder?.invoke(this)
    }
}

fun NavHostController.safeNavigate(route: String, baseRoute: String) {
    if (currentBackStackEntry?.destination?.route != route) {
        navigate(route) {
            popUpTo(baseRoute) { inclusive = false }
            launchSingleTop = true
        }
    }
}

@Composable
fun AppNavigation(navController: NavHostController,
    isLoggedIn: Boolean) {
    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) Routes.MAIN_GRAPH else Routes.AUTH_GRAPH
    ) {
        navigation(
            route = Routes.AUTH_GRAPH,
            startDestination = AuthRoutes.SPLASH
        ) {
            authGraph(navController)
        }
        navigation(
            route = Routes.MAIN_GRAPH,
            startDestination = Routes.MAIN
        ) {

            composable(Routes.MAIN) {
                MainWithDrawer(navController, navController)
            }

            composable(Routes.RENT) {
                MainRenta(navController , navController)
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

            composable(Routes.HISTORY) {
                HistoryScreen(navController)
            }

            composable(Routes.PAYMENT_METHODS) {
                paymentMethopdsCard(navController)
            }
        }
        composable(Routes.VOICE){
            VoiceScreen(navController)
        }

    }
}
