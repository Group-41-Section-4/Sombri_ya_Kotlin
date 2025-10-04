package com.example.sombriyakotlin.ui.rent


import android.app.Activity
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.ui.rent.Scan.ScanStrategy
import com.example.sombriyakotlin.feature.rent.NfcScanStrategy
import com.example.sombriyakotlin.ui.layout.AppLayout

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardRent(navController: NavController) {
    val ctx = LocalContext.current
    val activity = remember(ctx) { ctx as Activity }

    var strategy: ScanStrategy? by remember { mutableStateOf(null) }
    val nfc by remember { mutableStateOf(
        NfcScanStrategy(

        onTagDetected = {}

    )) }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(Modifier.weight(1f).fillMaxSize()) {
            ContentCard(Modifier.matchParentSize())
            BotonNFC(onClick = {
                if (strategy == null) {
                    nfc.start(activity)   // habilita Reader Mode
                    strategy = nfc
                    Log.d("Rent", "NFC Activado")
                } else {
                    strategy?.stop(activity)
                    strategy = null
                    Log.d("Rent", "NFC Desactivado")
                }

            }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp) )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun TopBar(navController : NavController){
    TopAppBar(
        title = { Text("") },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.primary),
            navigationIconContentColor = Color.Black
        ),

        navigationIcon = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    imageVector = Icons.Outlined.Notifications,
                    contentDescription = "Atrás"
                )
            }
        },
        actions = {
            IconButton(onClick = { navController.navigate("profile") }) {
                Icon(
                    imageVector = Icons.Filled.AccountCircle,
                    contentDescription = "Perfil"
                )
            }
        }

    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun ContentCard(modifier: Modifier = Modifier){
    Image(
        painter = painterResource(id = R.drawable.simulacionqr),
        contentDescription = "simulacion qr",
        modifier = modifier.fillMaxSize(),
        contentScale = ContentScale.Crop
    )
}

@Composable()
fun BotonNFC(onClick: () -> Unit, modifier: Modifier = Modifier){
    ExtendedFloatingActionButton(
        onClick = onClick,
        icon = {
            Icon(
                painter = painterResource(id = R.drawable.vector),
                contentDescription = "NFC",
                modifier = Modifier.size(24.dp)
            )
        },
        contentColor = colorResource(R.color.botonNfc),
        containerColor = colorResource(R.color.white),
        text = { Text("Activar NFC") },
        modifier = modifier
            .width(199.dp)
            .height(40.dp)
            .padding(
                horizontal = 25.dp,
                vertical = 5.dp
            )
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainRenta(navController: NavController) {
    AppLayout(navController = navController) {
        CardRent(navController)
    }
}