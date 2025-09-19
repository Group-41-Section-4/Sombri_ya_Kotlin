package com.example.sombriyakotlin.feature.rent

import android.annotation.SuppressLint
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
import com.example.sombriyakotlin.R


@Preview()
@Composable
fun cardRent() {

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {TopBar()},


        bottomBar = {bottomAppBar()},
        floatingActionButton = {botonNFC(onClick = { /* activar nfc */ })},
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center

    ){ innerPadding ->
        Box(Modifier.fillMaxSize().padding(innerPadding)) {
            ContentCard(Modifier.matchParentSize())
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun TopBar(){
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
            IconButton(onClick = { /* do something */ }) {
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
fun botonNFC(onClick: () -> Unit, /*modifier: Modifier = Modifier*/){
    ExtendedFloatingActionButton(onClick = {onClick},
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
        modifier = Modifier
            .width(199.dp)
            .height(40.dp)
            .padding(
                horizontal = 25.dp,
                vertical = 5.dp
            )

    )
}

@Composable()
fun bottomAppBar(){
    Box(Modifier.fillMaxWidth()) {
    BottomAppBar(
        containerColor = colorResource(id = R.color.primary),
        modifier = Modifier.height(71.dp),
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.mapa),
                    contentDescription = "Map",
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "Menu",
                    modifier = Modifier.size(24.dp)
                )
            }

        },
        floatingActionButton = {ButtonUmbrella()},

        )
}
}
@Composable()
fun ButtonUmbrella(

){
    FloatingActionButton(
        onClick = {/*ACTION WHEN DO CLICKS*/},
        containerColor = colorResource(R.color.red),
        contentColor = colorResource(R.color.white),
        modifier = Modifier.size(64.dp).offset(y = (-28).dp)
    ) {

            Icon(
                painter = painterResource(id = R.drawable.umbrella),
                contentDescription = "umbrella",
                modifier = Modifier.size(28.dp),
            )
        

    }


}
