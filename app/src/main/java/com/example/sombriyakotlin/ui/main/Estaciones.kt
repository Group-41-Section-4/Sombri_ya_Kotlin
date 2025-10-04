package com.example.sombriyakotlin.ui.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R

data class Estacion(val id: Int, val nombre: String, val descripcion: String)

//@Preview(showBackground = true, device = Devices.PIXEL_4)
@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardEstaciones(navController: NavController){
    val estaciones = listOf(
        Estacion(1, "Estación A", "Descripción de la estación A"),
        Estacion(2, "Estación B", "Descripción de la estación B"),
        Estacion(3, "Estación C", "Descripción de la estación C"),
        Estacion(4, "Estación D", "Descripción de la estación D"),
        Estacion(5, "Estación E", "Descripción de la estación E"),
        Estacion(6, "Estación F", "Descripción de la estación F"),
        Estacion(7, "Estación G", "Descripción de la estación G"),
        Estacion(8, "Estación H", "Descripción de la estación H"),
        Estacion(9, "Estación I", "Descripción de la estación I"),
        Estacion(10, "Estación J", "Descripción de la estación J")
    )

    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { ScrollableCardList(estaciones = estaciones) },
        sheetPeekHeight = 200.dp // Altura inicial del BottomSheet
    ) {
        // Contenido principal de la pantalla (puede estar vacío o tener otros elementos)
        Column(modifier = Modifier.fillMaxSize().padding(0.dp)) {
            MainContent(navController = navController) // Reemplaza con tu composable principalna)
        }
    }
}

@Composable
fun ScrollableCardList(estaciones: List<Estacion>) {
    Column (//Modifier.padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
        ) {
        Text("Estaciones cercanas", fontWeight = FontWeight.Bold)
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            items(estaciones) { estacion ->
                EstacionCard(estacion = estacion)
            }
        }
    }
}

@Composable
fun EstacionCard(estacion: Estacion) {
    Card(modifier = Modifier.padding(0.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.EstacionCard))) {
        Row () {
            Image(
                painter = painterResource(id = R.drawable.pin_umbrella),
                contentDescription = "Pin de sombrilla",
                modifier = Modifier.padding(8.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${estacion.nombre}")
                Text(text = "${estacion.descripcion}")
            }

        }


    }
}
