package com.example.sombriyakotlin.ui.main

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.feature.rent.TopBar
import com.example.sombriyakotlin.ui.inferiorbar.Bar
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.example.sombriyakotlin.ui.navigation.Routes
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@Composable
fun MainContent(navController: NavController) {
    var isMapLoaded by remember { mutableStateOf(false) }
    val bogota = LatLng(4.602742, -74.065403)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bogota, 17f) // Zoom de 12 es bueno para una ciudad.
    }
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ){
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            onMapLoaded = {isMapLoaded = true}
        ){

        }
        Button(
            onClick = { navController.navigate(Routes.STATIONS) },
            modifier = Modifier.padding(top = 5.dp),
            colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.estaciones_button)), // Color del botón
        ) {
            Text(text = "ESTACIONES")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainWithDrawer(navController: NavController) {
    AppLayout(navController = navController) {
        MainContent(navController)
    }
}
