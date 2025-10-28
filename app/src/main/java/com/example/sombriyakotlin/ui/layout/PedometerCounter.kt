package com.example.sombriyakotlin.ui.layout


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel

//@Preview
@Composable
fun PedometerCounter(
    viewModel: PedometerViewModel = hiltViewModel()
){
    val count by viewModel.count.collectAsState()
    var running by remember { mutableStateOf(true) }

    if (running){
        Text(text = "Pasos: $count", style = MaterialTheme.typography.headlineMedium, modifier = Modifier.fillMaxWidth())
    }
    else { }
}