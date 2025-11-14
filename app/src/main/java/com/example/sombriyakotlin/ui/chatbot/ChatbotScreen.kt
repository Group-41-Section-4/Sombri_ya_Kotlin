package com.example.sombriyakotlin.ui.chatbot

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.sombriyakotlin.R
import com.example.sombriyakotlin.domain.model.Message
import com.example.sombriyakotlin.ui.layout.TopBarMini

//@Preview

@Composable
fun ChatbotScreen(
    navController: NavHostController,
    viewModel: ChatbotViewModel=hiltViewModel()
)
{
    val chatState by viewModel.chat.collectAsState()
    val uiState by viewModel.chatbotState.collectAsState()
    val messages = chatState.messages // asumo `messages: List<Message>`
    val listState = rememberLazyListState()

    var mensaje by remember { mutableStateOf("") }

    val isConnected by viewModel.isConnected.collectAsState()



    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }


    Column(
        Modifier
            .fillMaxSize()
            .background(color = colorResource(id = R.color.EstacionCard))
            .navigationBarsPadding()
            .imePadding(),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    )
    {
        TopBarMini(navController, "Sombri-IA")
        if (!isConnected){
            Card(
                Modifier.wrapContentSize().padding(top=10.dp),
                shape = RoundedCornerShape(50.dp),
                colors= CardColors(colorResource(R.color.light_gray),colorResource(R.color.black),colorResource(R.color.gray),colorResource(R.color.gray))

            ) {
                Text("No hay conexión", modifier = Modifier.padding(5.dp))
            }
        }
        LazyColumn(
            modifier = Modifier
                .weight(1f)
//                .padding(top=50.dp)

                .fillMaxWidth(),
            state = listState,
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = false
        ) {
            itemsIndexed(messages, key = { index, item ->
                // si Message tiene id, úsalo aquí en lugar de index
                index
            }) { _, message ->
                MessageRow(message)
            }
        }

        when (uiState) {
            is ChatbotViewModel.ChatState.Loading -> {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth(),colorResource(R.color.HomeBlue))
            }
            else -> { /* nada */ }
        }
        // Send messages, text field and send
        Row(
            modifier = Modifier.padding(bottom = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround
        )
        {
            OutlinedTextField(
                value = mensaje,
                onValueChange = { mensaje = it },
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .weight(1f)                       // ocupa el espacio sobrante
                    .heightIn(min = 48.dp)            // altura mínima razonable
                    .background(colorResource(id = R.color.white_FFFDFD), shape = RoundedCornerShape(8.dp)),
                placeholder = { Text(if (isConnected) "Escribe un mensaje..." else "Sin conexión") },
                singleLine = false,
                maxLines = 2,                         // límite de líneas para evitar crecer demasiado
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = colorResource(R.color.HomeBlue))
                )

            val isLoading = uiState is ChatbotViewModel.ChatState.Loading
            Button({
                if (mensaje.isNotBlank() && !isLoading && isConnected) {
                    viewModel.sendMessage(mensaje)
                    mensaje = ""
                }
            },
                enabled = !isLoading && isConnected,
                modifier = Modifier.padding(start = 1.dp, end = 10.dp)
                ,
                colors = ButtonDefaults.buttonColors(
                    containerColor = colorResource(R.color.HomeBlue),
                    contentColor = Color.White,
                )
            )
            {
                Text("Enviar")
            }
        }
    }
}

@Composable
fun MessageRow(message: Message)
{
    val isUser = message.isUser
    val colorCard = if (isUser) colorResource(R.color.light_blue) else colorResource(R.color.naranja_light)

    Row(
        Modifier
            .wrapContentHeight()
            .fillMaxWidth(1f),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start

    )
    {
        Card(Modifier
//            .background(colorResource(R.color.primary))
            .fillMaxWidth(0.90f)
            .padding(horizontal = 20.dp, vertical = 5.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardColors(colorCard,colorResource(R.color.black),colorCard,colorCard)
        )
        {
            Text(message.content, Modifier.padding(horizontal = 10.dp, vertical = 8.dp))
        }

    }
}