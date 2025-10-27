package com.example.sombriyakotlin.ui.voice

import android.content.Context
import android.content.Intent
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.collect
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.compose.rememberNavController
import com.example.sombriyakotlin.domain.usecase.rental.RentalUseCases
import com.example.sombriyakotlin.domain.usecase.user.UserUseCases
import com.example.sombriyakotlin.ui.rent.RentViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val rentalUseCases: RentalUseCases,
) : ViewModel() {
    private var speechRecognizer: SpeechRecognizer? = null

    fun startListening(context: Context,
                       onFinished: () -> Unit,
                       onNavigateToRent: () -> Unit) {
        viewModelScope.launch {
            val recognizer = SpeechRecognizer.createSpeechRecognizer(context)
            speechRecognizer = recognizer

            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "es-ES")
                putExtra(RecognizerIntent.EXTRA_PROMPT, "Diga un comando...")
            }

            recognizer.setRecognitionListener(object : RecognitionListener {
                override fun onResults(results: Bundle?) {
                    val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                    val spokenText = matches?.firstOrNull()?.lowercase() ?: ""
                    Log.d("Voice", "Detectado: $spokenText")

                    when {
                        "iniciar reserva" in spokenText ||
                                ("empezar" in spokenText && "reserva" in spokenText) -> {
                            // Lanza una coroutine para poder usar collect
                            viewModelScope.launch {
                                rentalUseCases.getCurrentRentalUseCase()
                                    .collect { rental ->
                                        Log.d("WATTTT", "Rental actual: $rental")

                                        if (rental != null) {
                                            Log.d("Voice", "Renta activa: $rental")
                                        } else {
                                            onNavigateToRent()
                                            Log.d("Voice", "No hay renta activa")
                                        }
                                    }
                            }
                        }

                        "terminar reserva" in spokenText ||
                                ("finalizar" in spokenText && "reserva" in spokenText) -> {
                            // TODO: intentar que al ingresr a terminar reserva, se salte el pop up
                            Log.d("Voice", "Terminar reserva")
                                    onNavigateToRent()


                        }

                        else -> {
                            Log.d("Voice", "Comando no reconocido")
                        }
                    }

                    onFinished()
                }

                override fun onError(error: Int) {
                    Log.e("Voice", "Error: $error")
                    onFinished()
                }

                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
                override fun onPartialResults(partialResults: Bundle?) {}
                override fun onEvent(eventType: Int, params: Bundle?) {}
            })

            recognizer.startListening(intent)
        }
    }

    override fun onCleared() {
        speechRecognizer?.destroy()
        super.onCleared()
    }
}
