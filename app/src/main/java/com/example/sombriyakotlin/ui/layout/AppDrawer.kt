package com.example.sombriyakotlin.ui.layout

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.ui.navigation.navigateSingleTop
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    navController: NavHostController,
    scope: CoroutineScope,
    drawerState: DrawerState,
    onCloseDrawer: suspend () -> Unit
) {
    // Obtenemos la ruta actual para marcar la opción seleccionada
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    BackHandler(enabled = drawerState.isOpen) {
        scope.launch { drawerState.close() }
    }

    ModalDrawerSheet {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.BlueInterface))
        ) {
            Text("Menú", modifier = Modifier.padding(16.dp), color = colorResource(id = R.color.HomeBlue))

        }

        NavigationDrawerItem(
            label = { Text("Perfil") },
            selected = currentRoute == Routes.PROFILE,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigateSingleTop(Routes.PROFILE)
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Estaciones") },
            selected = currentRoute == Routes.STATIONS,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigateSingleTop(Routes.STATIONS)
                }
            }
        )

        NavigationDrawerItem(
            label = { Text("Métodos de pago") },
            selected = currentRoute == Routes.PAYMENT_METHODS,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigateSingleTop(Routes.PAYMENT_METHODS)
                }
            }
        )
        NavigationDrawerItem(
            label = { Text("Historial") },
            selected = currentRoute == Routes.HISTORY,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigateSingleTop(Routes.HISTORY)
                }
            }
        )
    }
}
