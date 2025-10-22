package com.example.sombriyakotlin.ui.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
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
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(colorResource(id = R.color.BlueInterface))
        ) {
            Text("Menú", modifier = Modifier.padding(16.dp), color = colorResource(id = R.color.HomeBlue))

        }

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
                    navController.navigate(Routes.PAYMENT_METHODS)
                }
            }
        )
        NavigationDrawerItem(
            label = { Text("Historial") },
            selected = false,
            onClick = {
                scope.launch {
                    onCloseDrawer()
                    navController.navigate(Routes.HISTORY)
                }
            }
        )
    }
}
