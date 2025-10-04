package com.example.sombriyakotlin.ui.main

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.collection.emptyObjectList
import androidx.compose.foundation.Image
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.domain.model.Station
import com.example.sombriyakotlin.ui.layout.AppLayout
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState


//@Preview(showBackground = true, device = Devices.PIXEL_4)
@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardStations(navController: NavController,viewModel: LocationViewModel = viewModel(),
                 stationsViewModel: StationsViewModel = hiltViewModel()){
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

    val stationsUiState by stationsViewModel.stationsState.collectAsStateWithLifecycle()



    val scaffoldState = rememberBottomSheetScaffoldState()

    BottomSheetScaffold(
        scaffoldState = scaffoldState,
        sheetContent = { Column (//Modifier.padding(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("Estaciones cercanas", fontWeight = FontWeight.Bold)
            LazyColumn(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                when (val currentState = stationsUiState) {
                    is StationsViewModel.StationsState.Success -> {
                        Log.d("MainContent", "Stations2: ${currentState.stations}")
                        items(currentState.stations) { station ->
                            EstacionCard(estacion = station)
                        } // Use items for LazyColumn
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
        } },
        sheetPeekHeight = 200.dp // Altura inicial del BottomSheet
    ) {
        // Contenido principal de la pantalla (puede estar vacío o tener otros elementos)
        Column(modifier = Modifier.fillMaxSize().padding(0.dp)) {
            MainContent(navController = navController) // Reemplaza con tu composable principalna)
        }
    }
}



@Composable
fun EstacionCard(estacion: Station) {
    Card(modifier = Modifier.padding(0.dp),
        colors = CardDefaults.cardColors(colorResource(R.color.EstacionCard))) {
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pin_umbrella),
                contentDescription = "Pin de sombrilla",
                modifier = Modifier.padding(8.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = "${estacion.placeName}", fontWeight = FontWeight.Bold)

                Row(modifier = Modifier.fillMaxWidth()) {
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = "${estacion.distanceMeters} m")
                }

                Text(text = "\uFE0F ${estacion.availableUmbrellas} \uD83C\uDF02 ${estacion.totalUmbrellas - estacion.availableUmbrellas}")
                Text(text = "${estacion.description}")
            }


        }


    }
}

