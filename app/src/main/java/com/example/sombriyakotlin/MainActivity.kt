package com.example.sombriyakotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.sombriyakotlin.feature.history.HistoryScreen
import com.example.sombriyakotlin.feature.notifications.NotificationsScreen
import com.example.sombriyakotlin.ui.navigation.AppNavigation
import com.example.sombriyakotlin.ui.theme.SombriYaKotlinTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            SombriYaKotlinTheme {
                //cardRent(navController = rememberNavController())
                //paymentMethopdsCard(navController)

                //HistoryScreen(navController =navController )
                AppNavigation(navController = navController, false)
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SombriYaKotlinTheme {
        Greeting("Android")
    }
}
