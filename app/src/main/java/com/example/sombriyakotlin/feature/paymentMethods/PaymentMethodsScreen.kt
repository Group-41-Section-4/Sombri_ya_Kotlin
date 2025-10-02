package com.example.sombriyakotlin.feature.paymentMethods

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.sombriyakotlin.R

data class CardData(
    val brand: String,
    val number: String,
    val name: String,
    val expiry: String,
    val color: Color
)
//@Preview
@Composable
fun paymentMethopdsCard(
    navController: NavController,
    onBackClick: () -> Unit= {},
    onAddClick: () -> Unit= {},
    onManageMethodsClick: () -> Unit= {},
    onPendingPaymentsClick: () -> Unit= {},
){
    Scaffold (

        topBar = { topBar(

            onBackClick ={navController.navigate("main")},
            onAddClick= onAddClick,
        ) }

                ,bottomBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Administrar Métodos")
                }
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(50)
                ) {
                    Text("Pagos Pendientes")
                }
            }
                }

    ){
            innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            // Caouresel cards
            Spacer(modifier = Modifier.height(32.dp))
            val cards = listOf(
                CardData("VISA", "**** **** **** 1234", "Nombre Ejemplo", "12/26", Color(0xFF0D47A1)),
                CardData("MASTERCARD", "**** **** **** 5678", "Nombre Ejemplo", "10/25", Color(0xFFB71C1C)),
                CardData("NU", "**** **** **** 9876", "Nombre Ejemplo", "08/27", Color(0xFF8E24AA))
            )
            CardStack(cards)





        }

    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable()
fun topBar(
    onBackClick: () -> Unit,
    onAddClick: () -> Unit,
){
    TopAppBar(
        title = { Text("Métodos de Pago") },
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás"
                )
            }
        },
        actions = {
            IconButton(onClick = { onAddClick() }) {
                Image(
                    painter = painterResource(id = R.drawable.add_circle),
                    contentDescription = "Agregar Metodo de pago",
                    modifier = Modifier.size(32.dp)
                )

            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = colorResource(R.color.primary),
            titleContentColor = Color.Black
        )
    )
}

@Composable
fun CardStack(cards: List<CardData>) {
    var topCardIndex by remember { mutableStateOf(0) }
    var offsetY by remember { mutableStateOf(0f) }

    val THRESHOLD = 100f
    val MAX_DRAG = 600f

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp),
        contentAlignment = Alignment.Center
    ) {

        cards.asReversed().forEachIndexed { index, card ->
            val actualIndex = cards.size - 1 - index

            if (actualIndex >= topCardIndex) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .height(180.dp)
                        .offset {
                            if (actualIndex == topCardIndex) {
                                IntOffset(0, offsetY.toInt())
                            } else {
                                IntOffset(0, 40 * (actualIndex - topCardIndex))
                            }
                        }
                        .pointerInput(topCardIndex) {
                            if (actualIndex == topCardIndex) {
                                detectDragGestures(
                                    onDragEnd = {
                                        when {
                                            // up: next card
                                            offsetY < -THRESHOLD ->
                                                topCardIndex = (topCardIndex + 1)
                                                    .coerceAtMost(cards.lastIndex)

                                            // down: before card
                                            offsetY > THRESHOLD && topCardIndex > 0 ->
                                                topCardIndex = (topCardIndex - 1)

                                            // No more cards
                                            else -> { /*nothing */ }
                                        }
                                        offsetY = 0f
                                    },
                                    onDrag = { _, dragAmount ->
                                        // dragAmount.y < 0 up, > 0 down
                                        val next = (offsetY + dragAmount.y)
                                            .coerceIn(-MAX_DRAG, MAX_DRAG)
                                        offsetY = next
                                    }
                                )
                            }
                        },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = card.color)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(card.brand, color = Color.White)
                        Text(card.number, color = Color.White, fontSize = 20.sp)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(card.name, color = Color.White)
                            Text(card.expiry, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}
