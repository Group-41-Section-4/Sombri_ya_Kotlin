package com.example.sombriyakotlin.ui.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLayout(
    navController: NavController,
    navHostController: NavHostController,
    content: @Composable () -> Unit,

    ) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // -- Main content -- //
        Scaffold(
            topBar = { TopBar(navHostController) },
            bottomBar = {
                Bar(
                    navController = navHostController,
                    onMenuClick = {
                        scope.launch {
                            if (drawerState.isClosed) drawerState.open()
                            else drawerState.close()
                        }
                    }
                )
            }
        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                content()
            }
        }

        // -- background -- //
        AnimatedVisibility(
            visible = drawerState.isOpen,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.scrim.copy(alpha = 0.5f))
                    .clickable(
                        indication = null,
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
                    ) {
                        // Close on click
                        scope.launch { drawerState.close() }
                    }
            )
        }

        // -- Panel animado -- //
        AnimatedVisibility(
            modifier = Modifier
                .fillMaxSize()
                .wrapContentSize(align = Alignment.CenterEnd),
            visible = drawerState.isOpen,
            // start rigth
            enter = slideInHorizontally(initialOffsetX = { fullWidth -> fullWidth }),
            // end rigth
            exit = slideOutHorizontally(targetOffsetX = { fullWidth -> fullWidth })
        ) {
            Surface(
                tonalElevation = 8.dp,
                shadowElevation = 8.dp, // Añade sombra para un mejor efecto visual
                modifier = Modifier
                    .fillMaxHeight()
                    .width(280.dp)
            ) {
                AppDrawer(
                    navController = navHostController,
                    scope = scope,
                    onCloseDrawer = { drawerState.close() },
                    drawerState = drawerState
                )
            }
        }
    }
}