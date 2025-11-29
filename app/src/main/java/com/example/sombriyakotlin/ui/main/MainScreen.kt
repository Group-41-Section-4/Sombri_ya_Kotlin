package com.example.sombriyakotlin.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SignalWifiOff
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.data.worker.GeoUtils
import com.example.sombriyakotlin.domain.model.WeatherType
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.example.sombriyakotlin.ui.main.animations.CloudEmojiAnimation
import com.example.sombriyakotlin.ui.main.animations.RainAnimation
import com.example.sombriyakotlin.ui.main.animations.SunRaysAnimation
import com.example.sombriyakotlin.ui.popup.ConsentDialog
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainContent(
    navController: NavController,
    onBottomBarVisibilityChange: ((Boolean) -> Unit)? = null,
    homeViewModel: HomeViewModel = hiltViewModel(),
    viewModel: LocationViewModel = viewModel(),
    stationsViewModel: StationsViewModel = hiltViewModel()
) {
    val context = LocalContext.current

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

    // Para mover la cámara solo una vez al obtener la primera ubicación
    var isInitialLocationSet by remember { mutableStateOf(false) }

    val weather by homeViewModel.weatherState.collectAsState()

    val stationsUiState by stationsViewModel.stationsState.collectAsStateWithLifecycle()

    val connection by homeViewModel.isConnected.collectAsState(initial = true)

    var showFallbackOverlay by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope ()
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.Hidden, // 👈 arranca colapsado
            skipHiddenState = false
        )
    )

    var showConsentPopUp by remember { mutableStateOf(false) }

    val alreadySent = rememberSaveable { mutableStateOf(false) }

    val consent by homeViewModel.consentState.collectAsState() // Boolean? (null = no preguntado)

    var bottomPadding by remember { mutableStateOf(72.dp) }

    // Detectar automáticamente los gestos del usuario
    LaunchedEffect(scaffoldState.bottomSheetState.currentValue) {
        val state = scaffoldState.bottomSheetState.currentValue

        when (state) {
            SheetValue.PartiallyExpanded -> {
                onBottomBarVisibilityChange?.invoke(false)
                bottomPadding = 72.dp
            }
            SheetValue.Expanded -> {
                onBottomBarVisibilityChange?.invoke(false)
                bottomPadding = 72.dp
            }
            SheetValue.Hidden -> {
                onBottomBarVisibilityChange?.invoke(true)
                bottomPadding = 0.dp
            }
            else -> { /* nada */ }
        }
    }


    // Ejemplo: mostrar el bottom bar normalmente
    LaunchedEffect(Unit) {
        onBottomBarVisibilityChange?.invoke(true)
        bottomPadding = 0.dp
    }

    LaunchedEffect(connection, isMapLoaded) {
        // si ambas condiciones se cumplen, esperamos un poco antes de mostrar
        if (!connection && !isMapLoaded) {
            delay(300) // ajusta el tiempo si quieres más/menos tolerancia
            if (!connection && !isMapLoaded) {
                showFallbackOverlay = true
            }
        } else {
            showFallbackOverlay = false
        }
    }

    LaunchedEffect(Unit) {
        delay(300)
        if (consent == null) {
            showConsentPopUp = true
        }
    }

    var lastSentLat by remember { mutableStateOf<Double?>(null) }
    var lastSentLon by remember { mutableStateOf<Double?>(null) }

    LaunchedEffect(location) {
        location?.let { loc ->
            // Mueve la cámara solo la primera vez
            if (!isInitialLocationSet) {
                val newPos = CameraPosition.fromLatLngZoom(
                    LatLng(loc.latitude, loc.longitude),
                    cameraPositionState.position.zoom
                )
                cameraPositionState.position = newPos
                isInitialLocationSet = true
                stationsViewModel.getStations(loc.latitude, loc.longitude)
            }

            // update weather state
            val now = System.currentTimeMillis()
            val lastUpdate = homeViewModel.lastWeatherUpdateTime
            if (now - lastUpdate > 60_000) { // 1 minute
                Log.d("HomeViewModel", "Updating weather")
                homeViewModel.checkWeatherAt(loc.latitude, loc.longitude)
            }
        }
        val loc = location ?: return@LaunchedEffect
        val lastLat = lastSentLat
        val lastLon = lastSentLon

        val shouldSend = lastLat == null || lastLon == null ||
                GeoUtils.distanceMeters(
                            lastLat, lastLon,
                            loc.latitude, loc.longitude
                                ) > 20.0 // threshold ~20m

        if (shouldSend) {
                lastSentLat = loc.latitude
                lastSentLon = loc.longitude
                homeViewModel.sendCurrentLocation(
                        loc.latitude,
                        loc.longitude,
                    )
            }


    }

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetPeekHeight = 400.dp,
        sheetContent = {
            Box(
                Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                StationsSheet(
                    stationsUiState = stationsUiState,
                    isConnected = connection,
                    onStationClick = { station ->
                        scope.launch {
                            // 1. Move camara
                            val newPosition = LatLng(station.latitude, station.longitude)
                            cameraPositionState.animate(
                                com.google.android.gms.maps.CameraUpdateFactory
                                    .newLatLngZoom(newPosition, 17f)
                            )

                            // 2. close sheet
                            val bottomState = scaffoldState.bottomSheetState

                            // avoid animarion
                            if (bottomState.currentValue != bottomState.targetValue) return@launch

                            // PartiallyExpanded
                            if (bottomState.currentValue == SheetValue.Expanded) {
                                bottomState.partialExpand()
                            }
                        }
                    }
                )
            }
        },
        //modifier = Modifier.fillMaxSize(),
        //sheetShadowElevation = 4.dp,
        sheetContainerColor = Color.White,
        sheetSwipeEnabled = true
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding( bottom = bottomPadding)
        )
        {
            GoogleMap(
                modifier = Modifier.fillMaxSize(),
                cameraPositionState = cameraPositionState,
                onMapLoaded = { isMapLoaded = true },
                properties = MapProperties(
                    isMyLocationEnabled = hasLocationPermission // Esto activa el "punto azul"
                ),
            )
            {
                Log.d("MainContent", "stationsUiState: $isMapLoaded")
                when (val currentState = stationsUiState) {
                    is StationsViewModel.StationsState.Success -> {
                        Log.d("MainContent", "Stations: ${currentState.stations}")
                        // Dibuja un marcador para cada estación en la lista
                        val scaledIcon = remember(context) {
                            // Crear un bitmap escalado para los iconos del mapa
                            val originalBitmap = (ContextCompat.getDrawable(context, R.drawable.pin_umbrella) as BitmapDrawable).bitmap
//                            val width = 75 // Nuevo ancho en píxeles
//                            val height = 100 // Nuevo alto en píxeles
//                            val scaledBitmap = Bitmap.createScaledBitmap(originalBitmap, width, height, false)
                            BitmapDescriptorFactory.fromBitmap(originalBitmap)
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
                    is StationsViewModel.StationsState.Loading -> {
                        // Opcional: Podrías mostrar un indicador de carga en algún lugar de la UI
                    }
                    is StationsViewModel.StationsState.Error -> {
                        // Opcional: Podrías mostrar un mensaje de error
                    }
                    else -> {
                        // Estado 'Idle' o inicial, no se hace nada.
                    }
                }
            }

            when (weather){
                WeatherType.SOLEADO -> {}
                WeatherType.NUBLADO -> CloudEmojiAnimation(modifier = Modifier.fillMaxSize())
                WeatherType.LLUVIA -> RainAnimation(modifier = Modifier.fillMaxSize())
                null -> {}
            }

            Button(
                onClick = {
                    scope.launch {
                        onBottomBarVisibilityChange?.invoke(false)
                        bottomPadding = 72.dp
                        val bottomState = scaffoldState.bottomSheetState

                        if (bottomState.currentValue != bottomState.targetValue) return@launch

                        when (bottomState.currentValue) {
                            SheetValue.PartiallyExpanded -> {
                                bottomState.hide()
                            }
                            SheetValue.Expanded -> {
                                bottomState.partialExpand()
                            }
                            SheetValue.Hidden -> {
                                bottomState.partialExpand()
                            }
                            else -> {}
                        }
                    }
                },
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.estaciones_button)
                )
            ) {
                Text("ESTACIONES")
                if (!connection) {
                    Icon(
                        Icons.Rounded.SignalWifiOff,
                        contentDescription = "Sin conexión",
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }
            if (showFallbackOverlay) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(R.drawable.black_cat),
                        "No connection"
                    )
                    Text("Sin conexión a internet")
                }
            }

//            if (connection && showConsentPopUp) {
//                ConsentDialog(
//                    onAllow = {
//                        // antes de pedir permiso de runtime, podemos chequear si ya se concedió
//                        homeViewModel.onConsentAnswered(true)
//                        if (location != null)
//                        {
//                            homeViewModel.sendCurrentLocation(
//                                location?.latitude ?: 0.0,
//                                location?.longitude ?: 0.0)
//                        }
//                    },
//                    onDeny = {
//                        homeViewModel.onConsentAnswered(false)
//                    }
//                )
//            }
        }

    }
}

@Composable
fun MainWithDrawer(
    navController: NavController,
    navHostController: NavHostController
) {
    AppLayout(navController, navHostController) { setBottomBarVisible ->
        MainContent(
            navController = navController,
            onBottomBarVisibilityChange = setBottomBarVisible
        )
    }
}