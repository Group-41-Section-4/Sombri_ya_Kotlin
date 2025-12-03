package com.example.sombriyakotlin.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppLayout(
    navController: NavController,
    navHostController: NavHostController,
    showTopBar: Boolean = true,
    content: @Composable (
        (Boolean) -> Unit  // callback OPCIONAL
    ) -> Unit = { _ -> }  // valor por defecto
) {

    var showBottomBar by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        // -- Main content -- //
        Scaffold(
            topBar = { if (showTopBar) TopBar(navHostController) else
                Box(modifier = Modifier.fillMaxWidth().background(
                colorResource(id = R.color.BlueInterface)
            )
                )  },
            bottomBar = {
                // Contenedor SIEMPRE presente
                Box(
                    modifier = Modifier
                        .fillMaxWidth(),
                        contentAlignment = Alignment.BottomCenter
                ) {
                    // Contenido animado dentro del espacio fijo
                    if (showBottomBar) {
                        Bar(
                            navController = navHostController
                        )
                    }
                }
            }

        ) { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Se pasa el setter del estado
                content { visible -> showBottomBar = visible }
            }
        }


    }
}