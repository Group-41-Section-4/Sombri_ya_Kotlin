package com.example.sombriyakotlin.ui.history.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.feature.history.HistoryViewModel
import com.example.sombriyakotlin.ui.layout.TopBarMini

@Composable
fun HistoryDetailScreen(
    navController: NavHostController,
    historyVM: HistoryViewModel = hiltViewModel()
) {
    val state by historyVM.detailState.collectAsState()
    val Bg = Color(0xFFFFFDFD)

    Scaffold(
        containerColor = Bg,
        topBar = { TopBarMini(navController, "Detalle") },

        ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Bg)
                .padding(inner)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        text = state.error!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colors.error
                    )
                }

                state.detail != null -> {
                    HistoryDetailContent(state.detail)
                }
            }
        }
    }
}

@Composable
fun HistoryDetailContent(
    detail: Rental?,
) {
    Box(
        modifier = Modifier
//
//            .fillMaxSize()
//            .background(Color(0xFFFFFFFF))
//            .padding(5.dp)
//            .shadow(12.dp)
    ) {
        Text(
            text = detail?.startedAt ?: "No disponible",
            color = Color.Black
        )
        // UI pura
    }
}

