package com.example.sombriyakotlin.feature.rent

import android.annotation.SuppressLint
import android.window.BackEvent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.feature.inferiorbar.Bar

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardRent(navController: NavController) {
    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        TopBar(navController)
        Box(Modifier.weight(1f).fillMaxSize()) {
            ContentCard(Modifier.matchParentSize())
            BotonNFC(onClick = { /* activar nfc */ }, modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 80.dp) )
        }
        Bar(navController)
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
