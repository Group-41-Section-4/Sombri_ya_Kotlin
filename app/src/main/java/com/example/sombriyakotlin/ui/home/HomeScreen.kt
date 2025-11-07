package com.example.sombriyakotlin.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.account.navigation.AuthRoutes
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.ui.navigation.safeNavigate

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardHome(
    navController: NavHostController,
    splashViewModel: SplashViewModel = hiltViewModel()
    ) {

    val connected by splashViewModel.isConnected.collectAsState()

    var showNoInternetDialog by remember { mutableStateOf(false) }

    LaunchedEffect (connected) {
        showNoInternetDialog = !connected
    }

    //run only ones
    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(1500) // 1.5 segs

        val hasUser = splashViewModel.hasUserStored()
        if (hasUser) {
            navController.navigate(Routes.MAIN_GRAPH) {
                popUpTo(Routes.AUTH_GRAPH) { inclusive = true }
                launchSingleTop = true
            }
        } else {
            navController.safeNavigate("login", AuthRoutes.SPLASH)
        }
    }


    Surface(modifier = Modifier.fillMaxSize(),
        color= colorResource(id=R.color.BlueInterface),
    ) {
        Column (
            modifier = Modifier.fillMaxHeight(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_no_bg),
                contentDescription = "Logo",
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .fillMaxHeight(0.75f)
            )
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .fillMaxHeight(0.3f)
//                ,
//                onClick = { navController.safeNavigate("login", AuthRoutes.SPLASH) },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = colorResource(R.color.HomeBlue),
//                    contentColor = Color.White,
//                    )
//            ) { Text("Iniciar Sesión",
//                fontSize = 20.sp)}
        }
    }

    if (showNoInternetDialog) {
        AlertDialog(
            onDismissRequest = { showNoInternetDialog = false },
            title = { Text("Sin conexión") },
            text = { Text("Parece que no tienes acceso a Internet. Por favor comprueba tu conexión.") },
            confirmButton = {
                TextButton(onClick = { showNoInternetDialog = false }) {
                    Text("OK")
                }
            }
        )
    }
}