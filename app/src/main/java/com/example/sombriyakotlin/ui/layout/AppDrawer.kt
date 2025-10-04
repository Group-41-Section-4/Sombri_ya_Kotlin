package com.example.sombriyakotlin.ui.layout

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.ui.navigation.Routes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    navController: NavController,
    scope: CoroutineScope,
    onCloseDrawer: suspend () -> Unit
) {
    ModalDrawerSheet {
        Text("Opciones", modifier = Modifier.padding(16.dp))

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = false,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Routes.PROFILE)
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Estaciones") },
            selected = false,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Routes.STATIONS)
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Métodos de pago") },
            selected = false,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    // navController.navigate(Routes.PAYMENT_METHODS)
                }
            }
        )
    }
}
