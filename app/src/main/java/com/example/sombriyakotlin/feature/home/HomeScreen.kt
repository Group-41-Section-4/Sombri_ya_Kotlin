package com.example.sombriyakotlin.feature.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R

@OptIn(ExperimentalMaterial3Api::class)
//@Preview()
@Composable
fun CardHome(navController: NavController) {
    Surface(modifier = Modifier.fillMaxSize(),
        color= colorResource(id=R.color.BlueInterface),
    ) {
        Column (
            modifier = Modifier.fillMaxHeight(0.7f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            Image(
                painter = painterResource(id = R.drawable.logo_no_bg),
                contentDescription = "Logo",
                modifier = Modifier.fillMaxWidth(0.75f).fillMaxHeight(0.75f)
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .fillMaxHeight(0.2f)
                ,
                onClick = { navController.navigate("login") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.HomeBlue),
                    contentColor = Color.White,
                    )
            ) { Text("Iniciar Sesión con Uniandes",
                fontSize = 20.sp)}
        }
    }
    //Text(text = "Hola")
}