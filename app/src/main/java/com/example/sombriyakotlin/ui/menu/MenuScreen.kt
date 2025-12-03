package com.example.sombriyakotlin.ui.menu

import androidx.compose.foundation.background
import androidx.compose.runtime.Composable
//import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
//import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.example.sombriyakotlin.ui.navigation.Routes
import com.example.sombriyakotlin.ui.navigation.safeNavigate

// --- 1. Data Class para los elementos del menú ---
            data class MenuItemData(
        val icon: ImageVector,
        val title: String,
        val onClick: () -> Unit // Función para manejar el clic
    )

    // --- 2. Perfil de Usuario ---
    @Composable
    fun UserProfileHeader(name: String, subtext: String, onClick: () -> Unit) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.15f)
                .background(colorResource(id = R.color.BlueInterface)),
            contentAlignment = Alignment.BottomCenter,

        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp, horizontal = 24.dp)
                    .clickable(onClick = onClick)
                    .background(colorResource(id = R.color.BlueInterface)),
                verticalAlignment = Alignment.CenterVertically
            )
            {
            // Icono de perfil (como en la imagen)
            Icon(
                imageVector = Icons.Default.AccountCircle,
                contentDescription = "Perfil",
                modifier = Modifier.size(48.dp),
                tint = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(
                    text = name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = subtext,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary // Color azul para "Mi perfil"
                )
            }
        }
        }
        // Opcional: Separador visual
//        Divider(modifier = Modifier.padding(horizontal = 24.dp), color = Color.LightGray.copy(alpha = 0.5f))
    }

    // --- 3. Componente individual para cada ítem del menú ---
    @Composable
    fun MenuItem(item: MenuItemData) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
                .clickable(onClick = item.onClick),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp), // Sin sombra para que se vea plano
            shape = MaterialTheme.shapes.extraSmall // Para bordes rectangulares
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }

    // --- 4. Pantalla Principal del Menú ---
    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SettingsMenuScreen(navController: NavController,
                           navHostController: NavHostController) {
        // Definición de los elementos del menú
        val menuItems = listOf(
            MenuItemData(Icons.Default.CreditCard, "Métodos de pago") { navHostController.safeNavigate(Routes.PAYMENT_METHODS, Routes.MENU) },
            MenuItemData(Icons.Default.History, "Historial") { navHostController.safeNavigate(Routes.HISTORY, Routes.MENU) },
            MenuItemData(Icons.Default.Mic, "Acción de voz") { navHostController.safeNavigate(Routes.VOICE, Routes.MENU) },
            MenuItemData(Icons.Default.Notifications, "Notificaciones") { navHostController.safeNavigate(Routes.NOTIFICATIONS, Routes.MENU) },
            MenuItemData(Icons.Default.SmartToy, "Sombri-IA") { navHostController.safeNavigate(Routes.CHATBOT, Routes.MENU) },
            MenuItemData(Icons.Default.HelpOutline, "Ayuda") { navHostController.safeNavigate(Routes.MAIN, Routes.MENU) }
        )

        Scaffold(

            // Barra superior con el nombre del usuario o "Más"
            // Contenido principal del menú
            content = { paddingValues ->
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(top = 8.dp), // Espacio superior
                    horizontalAlignment = Alignment.Start
                ) {

                    // Elementos del menú (tarjetas blancas)
                    items(menuItems) { item ->
                        MenuItem(item = item)
                    }

                    // Términos y condiciones
                    item {
                        Text(
                            text = "Términos y condiciones",
                            style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold),
                            modifier = Modifier
                                .clickable { /* Acción: Abrir TyC */ }
                                .padding(horizontal = 24.dp, vertical = 24.dp)
                                .fillMaxWidth()
                        )
                    }

                    // Separador antes del menú de navegación inferior
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                    }
                }
            },
            topBar = {
                UserProfileHeader(name = "Santiago", subtext = "Mi perfil >",
                    onClick =
                        {navHostController.safeNavigate(
                            Routes.PROFILE, Routes.MENU)})
            }
        )
    }


@Composable
fun MainMenu(navController: NavController,
             navHostController: NavHostController){
    AppLayout(navController, navHostController, false) {
        SettingsMenuScreen(
            navController = navController,
            navHostController = navHostController
        )
    }
}
