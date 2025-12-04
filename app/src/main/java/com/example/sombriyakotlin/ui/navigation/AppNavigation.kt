package com.example.sombriyakotlin.ui.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.sombriyakotlin.feature.history.HistoryScreen
import com.example.sombriyakotlin.ui.notifications.NotificationsScreen
import com.example.sombriyakotlin.ui.account.CardProfile
import com.example.sombriyakotlin.ui.main.CardStations
import com.example.sombriyakotlin.ui.main.MainWithDrawer
import com.example.sombriyakotlin.ui.paymentMethods.paymentMethopdsCard
import com.example.sombriyakotlin.ui.rent.MainRenta
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.navigation
import com.example.sombriyakotlin.ui.account.AccountManagementScreen
import com.example.sombriyakotlin.ui.account.navigation.AuthRoutes
import com.example.sombriyakotlin.ui.account.navigation.authGraph
import com.example.sombriyakotlin.ui.chatbot.ChatbotScreen
import com.example.sombriyakotlin.ui.help.HelpScreen
import com.example.sombriyakotlin.ui.menu.MainMenu
import com.example.sombriyakotlin.ui.reports.ReporScreen
import com.example.sombriyakotlin.ui.tyc.TyCScreen
import com.example.sombriyakotlin.ui.voice.VoiceScreen

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
    const val VOICE = "voice"

    const val CHATBOT = "chatbot"

    const val MENU = "menu"

    const val TYC = "tyc"

    const val FORMULARIO = "formulario"

    const val HELP = "help"

    const val ACCOUNT_MANAGEMENT = "account_management"


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
    isLoggedIn: Boolean,uri: Uri) {
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

            composable(Routes.MENU){
                MainMenu(navController, navController)
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
            composable(Routes.VOICE){
                VoiceScreen(navController,navController)
            }

            composable(Routes.CHATBOT){
                ChatbotScreen(navController)
            }

            composable(Routes.FORMULARIO){
                ReporScreen(photoUri = uri, navhostController = navController)
            }

            composable(Routes.TYC){
                TyCScreen(navController)
            }

            composable(Routes.HELP){
                HelpScreen(navController)
            }

            composable(Routes.ACCOUNT_MANAGEMENT) {
                AccountManagementScreen(navController = navController)
            }


        }
    }
}