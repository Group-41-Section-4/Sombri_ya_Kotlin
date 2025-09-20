package com.example.sombriyakotlin.feature.main

import androidx.compose.foundation.background
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.feature.inferiorbar.Bar
import com.example.sombriyakotlin.feature.rent.TopBar

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardMain(navController: NavController){
    Column (
        modifier = Modifier.fillMaxHeight(1f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {


//        TopAppBar(title={Text("")},
//            colors= TopAppBarDefaults.topAppBarColors(
//            containerColor = colorResource(id = R.color.BlueInterface),
//            titleContentColor = Color.White
//        ))

        TopBar(navController)
        Box(
            modifier = Modifier
                .fillMaxHeight(0.9f)
                .fillMaxWidth()
                .background(Color.Black)
                .padding(10.dp),
            contentAlignment = Alignment.TopCenter,
            //color = Color.Black
        ){
            Button(onClick = { navController.navigate("stations")},
                modifier = Modifier.padding(top=5.dp),
                colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.estaciones_button)), // Color del botón
            ) {
                Text(text = "ESTACIONES")
            }

        }
        Bar(navController)
    }
}