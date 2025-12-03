package com.example.sombriyakotlin.ui.history.detail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.feature.history.HistoryViewModel

@Composable
fun HistoryDetailScreen(
    historyVM: HistoryViewModel = hiltViewModel()
) {
    val state by historyVM.detailState.collectAsState()

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

@Composable
fun HistoryDetailContent(
    detail: Rental?,
) {
    // UI pura
}

