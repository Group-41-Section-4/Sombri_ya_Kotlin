package com.example.sombriyakotlin.feature.history

// Asumo que la definición de HistoryUiItem se movió aquí para evitar redeclaración.

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Umbrella
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.domain.usecase.history.GetHistory
import com.example.sombriyakotlin.domain.usecase.history.SaveHistory
import com.example.sombriyakotlin.ui.layout.TopBarMini
import javax.inject.Inject


@Composable
fun HistoryScreen (
    navController: NavHostController,
    historyVM: HistoryViewModel = hiltViewModel()
) {
    // Aseguramos que la data se cargue solo una vez
    LaunchedEffect(Unit) {
        Log.d("HistoryScreen", "Cargando historial...")
        historyVM.onScreenOpened()
    }

    // El StateFlow ahora contiene List<HistoryUiItem>
    val history by historyVM.history.collectAsState()

    val Bg = Color(0xFFFFFDFD)

    val connected by historyVM.isConnected.collectAsState()


    Scaffold(
        containerColor = Bg,
        topBar = { TopBarMini(navController, "Historial") },

        ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Bg)
                .padding(inner)
        ) {
            if (history.isEmpty()) {
                Text(
                    if (connected) "Aún no tienes historial" else "Sin conexión a Internet",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyMedium
                )
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp, 16.dp, 16.dp, 96.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // La lista 'history' ahora es de tipo HistoryUiItem
                    itemsIndexed(
                        items = history,
                        key = { _, h -> h.id }
                    ) { _, h ->
                        HistoryCard(h)
                    }
                }
            }
        }
    }
}

@Composable
// ** CAMBIO: Aceptamos HistoryUiItem en lugar del DTO/Domain History **
fun HistoryCard(h: HistoryUiItem) {
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
                imageVector = Icons.Outlined.Umbrella,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(Modifier.width(20.dp))
            Column(Modifier.weight(1f)) {
                Text(
                    // ** Usamos h.date (la fecha ya formateada) **
                    h.date,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    // ** Usamos h.durationMinutes **
                    "Duración: ${h.durationMinutes} minutos",
                    color = Color(0xDE000000),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Spacer(Modifier.width(12.dp))
            Text(
                // ** Usamos h.time (la hora del evento ya formateada) **
                text = h.time,
                color = Color.Black,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }
}