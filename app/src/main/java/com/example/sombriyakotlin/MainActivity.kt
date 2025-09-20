package com.example.sombriyakotlin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.sombriyakotlin.feature.account.CardProfile
import com.example.sombriyakotlin.feature.home.CardHome
import com.example.sombriyakotlin.feature.main.CardEstaciones
import com.example.sombriyakotlin.feature.main.CardMain
import com.example.sombriyakotlin.feature.rent.cardRent
import androidx.navigation.compose.rememberNavController
import com.example.sombriyakotlin.navigation.AppNavigation
import com.example.sombriyakotlin.ui.theme.SombriYaKotlinTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()
        setContent {
            SombriYaKotlinTheme {
                //cardRent(navController = rememberNavController())
                val navController = rememberNavController()
                AppNavigation(navController = navController)
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