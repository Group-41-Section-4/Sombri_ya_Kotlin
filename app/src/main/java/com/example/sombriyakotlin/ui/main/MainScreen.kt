package com.example.sombriyakotlin.ui.main

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.inferiorbar.Bar
import com.example.sombriyakotlin.feature.rent.TopBar
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardMain(navController: NavController,
             viewModel: LocationViewModel = viewModel()){

    val context = LocalContext.current

    // Permisos launcher
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { perms ->
        // Nada explícito aquí: cuando se concede, el collector de locationState empezará a emitir
    }

    // verificar permiso actual
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    // Observa la ubicación desde el ViewModel (StateFlow -> compose state)
    val location by viewModel.locationState.collectAsState(initial = null)

    var isMapLoaded by remember { mutableStateOf(false) }
    val bogota = LatLng(4.602742, -74.065403)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(bogota, 17f)
    }
    LaunchedEffect(location) {
        location?.let { loc ->
            // mueve la cámara a la ubicación del usuario con el mismo zoom
            val newPos = CameraPosition.fromLatLngZoom(LatLng(loc.latitude, loc.longitude), 17f)
            // Intentamos animar/mover la cámara; setear la posición directamente
            cameraPositionState.position = newPos
        }
    }
    Column (
        modifier = Modifier.fillMaxHeight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopBar(navController)
        Box(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth(),
            contentAlignment = Alignment.TopCenter,
        ){

            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = {isMapLoaded = true},
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission // Esto activa el "punto azul"
                ),
            ){

            }
            Button(
                onClick = { navController.navigate("stations") },
                modifier = Modifier.padding(top = 5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.estaciones_button)), // Color del botón
            ) {
                Text(text = "ESTACIONES")
            }

        }
        Bar(navController)
    }
}