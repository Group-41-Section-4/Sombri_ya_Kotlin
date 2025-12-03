package com.example.sombriyakotlin.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.ui.layout.TopBar
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardStations(navController: NavHostController,
                 viewModel: LocationViewModel = viewModel(),
                 stationsViewModel: StationsViewModel = hiltViewModel()){


    val stationsUiState by stationsViewModel.stationsState.collectAsStateWithLifecycle()
    // Verify location permission
    val context = LocalContext.current
    var hasLocationPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        )
    }

    var isMapLoaded by remember { mutableStateOf(false) }

    val loc by viewModel.locationState.collectAsState(initial = null)

    LaunchedEffect(loc) {
        // Solo llamamos al ViewModel si `loc` NO es null.
        loc?.let { currentLocation ->
            Log.d("CardStations", "Ubicación obtenida: $currentLocation. Llamando a getStations...")
            stationsViewModel.getStations(currentLocation.latitude, currentLocation.longitude)
        }
    }

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(LatLng(4.602742, -74.065403), 17f)
    }

    val coroutineScope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = {
            Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text("Estaciones cercanas", fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when (val currentState = stationsUiState) {
                    is StationsViewModel.StationsState.Success -> {
                        Log.d("StationsUI", "Estado: Success. Número de estaciones: ${currentState.stations.size}")
                        items(currentState.stations) { station ->
                            EstacionCard(estacion = station,
                            onCardClick = { clickedStation ->
                                // 3. Actualizar la posición de la cámara al hacer clic
                                val newPosition =
                                    LatLng(station.latitude, station.longitude)
                                coroutineScope.launch {
                                    cameraPositionState.animate(
                                        CameraUpdateFactory.newLatLngZoom(newPosition, 17f)
                                    )
                                }
                            }
                            )
                        } // Use items for LazyColumn
                    }
                    is StationsViewModel.StationsState.Loading -> {
                        Log.d("StationsUI", "BottomSheet: Loading")
                        // Informar al usuario que se está cargando
                        item {
                            Column(
                                modifier = Modifier
                                    .fillParentMaxHeight(0.5f)
                                    .fillParentMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                // CircularProgressIndicator() // Descomenta para añadir un spinner
                                Text("Cargando estaciones...")
                            }
                        }
                    }
                    is StationsViewModel.StationsState.Error -> {
                        Log.e("StationsUI", "BottomSheet: Error - ${currentState.message}")
                        // Informar al usuario del error
                        item {
                            Text("Error al cargar: ${currentState.message}")
                        }
                    }
                    else -> {
                        Log.d("StationsUI", "BottomSheet: Estado inicial o no implementado")
                        //  Nothing
                    }
                }
            }
        } },
        sheetPeekHeight = 200.dp // Altura inicial del BottomSheet
    ) {
        // Contenido principal de la pantalla (puede estar vacío o tener otros elementos)
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(0.dp)) {
            TopBar(navController = navController)
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isMapLoaded = true },
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission // Esto activa el "punto azul"
                ),
            ) {
                when (val currentState = stationsUiState) {
                    is StationsViewModel.StationsState.Success -> {
                        Log.d("Stations", "Stations: ${currentState.stations}")
                        // Dibuja un marcador para cada estación en la lista
                        val scaledIcon = remember(context) {
                            // Crear un bitmap escalado para los iconos del mapa
                            val originalBitmap = (ContextCompat.getDrawable(context, R.drawable.pin_umbrella) as BitmapDrawable).bitmap
                            val width = 75 // Nuevo ancho en píxeles
                            val height = 100 // Nuevo alto en píxeles
                            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false)
                            BitmapDescriptorFactory.fromBitmap(scaledBitmap)
                        }
                        currentState.stations.forEach { station ->
                            Marker(
                                state = MarkerState(position = LatLng(station.latitude, station.longitude)),
                                title = station.placeName,
                                snippet = "${station.description} \n- ${station.availableUmbrellas} available",
                                icon = scaledIcon
                            )
                        }
                    }
                    else -> {
                        // Estado 'Idle' o inicial, no se hace nada.
                    }
                }
            }
        }
    }
}



@Composable
fun EstacionCard(estacion: Station,
                 isconected: Boolean = true,
                 onCardClick: (Station) -> Unit = {}) {
    Card(modifier = Modifier
        .padding(0.dp)
        .clickable { onCardClick(estacion) },
        colors = CardDefaults.cardColors(colorResource(R.color.EstacionCard))) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.pin_umbrella),
                contentDescription = "Pin de sombrilla",
                modifier = Modifier.padding(2.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${estacion.placeName}", fontWeight = FontWeight.Bold)
                if (isconected) {
                    Row(modifier = Modifier.fillMaxWidth(),verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter=painterResource(R.drawable.umbrella_available),
                            contentDescription = "Sombrilla disponible",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text(text = " ${estacion.availableUmbrellas} ", color= colorResource(R.color.green), fontWeight = FontWeight.Bold)
                        Image(
                            painter=painterResource(R.drawable.no_umbrella),
                            contentDescription = "Sombrilla no disponible",
                            modifier = Modifier.padding(8.dp)
                        )
                        Text("${estacion.totalUmbrellas - estacion.availableUmbrellas}",color=colorResource(R.color.red), fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "${estacion.distanceMeters} m")

                    }
                }

                Text(text = "${estacion.description}")
            }

        }


    }
}


@Composable
fun StationsSheet(
    stationsUiState: StationsViewModel.StationsState,
    isConnected: Boolean,
    onStationClick: (Station) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp, start = 16.dp, end = 16.dp, bottom = 8.dp)
    ) {
        Text("Estaciones cercanas", fontWeight = FontWeight.Bold)

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally

        ) {
            when (stationsUiState) {
                is StationsViewModel.StationsState.Success -> {
                    items(
                        items = stationsUiState.stations,
                        key = { station -> station.id }
                    ) { station ->
                        EstacionCard(
                            estacion = station,
                            isconected = isConnected,
                            onCardClick = { onStationClick(station) }
                        )
                    }
                }
                is StationsViewModel.StationsState.Loading -> {
                    item {
                        Text("Cargando estaciones...")
                    }
                }
                is StationsViewModel.StationsState.Error -> {
                    item {
                        Text("Error al cargar: ${stationsUiState.message}")
                    }
                }
                else -> {}
            }
        }
    }
}


