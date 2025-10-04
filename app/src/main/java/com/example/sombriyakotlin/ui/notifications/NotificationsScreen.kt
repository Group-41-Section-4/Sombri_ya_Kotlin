package com.example.sombriyakotlin.feature.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.Alarm
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.domain.model.Notification
import com.example.sombriyakotlin.domain.model.NotificationType
import com.example.sombriyakotlin.ui.main.LocationViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(navController: NavController) {
    TopAppBar(
        title = {
            Text(
                "Notificaciones",
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.primary),
            navigationIconContentColor = Color.Black
        ),
        navigationIcon = {
            IconButton(onClick = { navController.navigate("main") }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás")
            }
        }
    )
}

@Composable
fun NotificationsScreen(
    navController: NavController
) {
    val locationVM: LocationViewModel = viewModel()
    val notificationsVM: NotificationsViewModel = hiltViewModel()
    LaunchedEffect(Unit) {
        notificationsVM.onScreenOpened()
    }


    val notifications by notificationsVM.notifications.collectAsState()

    val Bg = Color(0xFFFFFDFD)

    Scaffold(
        containerColor = Bg,
        topBar = { TopBar(navController) },
        bottomBar = {
            if (notifications.isNotEmpty()) {
                BottomAppBar(containerColor = Bg) {
                    Button(
                        onClick = { notificationsVM.clearAll() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = colorResource(R.color.red),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(24.dp)
                    ) { Text("Borrar Notificaciones") }
                }
            }
        }
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Bg)
                .padding(inner)
        ) {
            if (notifications.isEmpty()) {
                Text(
                    "No tienes notificaciones",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(
                        items = notifications,
                        key = { index, item -> "${item.id}#$index" }
                    ) { _, n ->
                        NotificationCard(n)
                    }
                }
            }
        }
    }
}

@Composable
fun NotificationCard(n: Notification) {
    Card(
        shape = RoundedCornerShape(30.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        colors = CardDefaults.cardColors(containerColor = colorResource(R.color.light_blue)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = when (n.type) {
                    NotificationType.WEATHER -> Icons.Outlined.WbSunny
                    NotificationType.SUBSCRIPTION -> Icons.Outlined.Info
                    NotificationType.RENTAL -> Icons.Outlined.Alarm
                },
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(Modifier.width(20.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    n.title,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    n.message,
                    color = Color(0xDE000000),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(n.time, color = Color.Black, style = MaterialTheme.typography.labelMedium)
        }
    }
}
