package com.example.sombriyakotlin.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.ui.navigation.safeNavigate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavHostController) {
    TopAppBar(
        title = { PedometerCounter() },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.BlueInterface),
            navigationIconContentColor = Color.Companion.Black
        ),
        navigationIcon = {
            IconButton(onClick = {navController.safeNavigate(Routes.NOTIFICATIONS, Routes.MAIN) } ) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "notificaciones"
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.safeNavigate(Routes.PROFILE, Routes.MAIN) }) {
                Icon(imageVector = Icons.Filled.AccountCircle, contentDescription = "Perfil")
            }
        }
    )
}