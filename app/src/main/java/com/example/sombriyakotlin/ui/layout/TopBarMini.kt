package com.example.sombriyakotlin.ui.layout

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
fun TopBarMini(navController: NavHostController, title: String, fromMenu: Boolean=true, content: @Composable () -> Unit ? = {}){
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.White
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.primary),
            navigationIconContentColor = Color.White
        ),
        navigationIcon = {
            IconButton(onClick = { navController.safeNavigate(if (fromMenu) Routes.MENU else Routes.MAIN, Routes.MAIN ) }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        },
        actions = {
            content()
        }
    )
}