package com.example.sombriyakotlin.ui.util

import android.content.Context
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG
import androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat

class BiometricAuthManager(
    private val activity: AppCompatActivity
) {
    // 1. Prepara la información que se mostrará en el diálogo del sistema
    private val promptInfo: BiometricPrompt.PromptInfo = BiometricPrompt.PromptInfo.Builder()
        .setTitle("Inicio de Sesión Biométrico")
        .setSubtitle("Usa tu huella o rostro para iniciar sesión")
        .setAllowedAuthenticators(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        .build()

    // 2. Función principal que lanza el diálogo
    fun showBiometricPrompt(
        onSuccess: (BiometricPrompt.AuthenticationResult) -> Unit,
        onError: (Int, CharSequence) -> Unit
    ) {
        val executor = ContextCompat.getMainExecutor(activity)

        val biometricPrompt = BiometricPrompt(
            activity, executor,
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess(result)
                }

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    // Se llama si hay un error irrecuperable (ej: muchos intentos)
                    // o si el usuario pulsa "Cancelar"
                    onError(errorCode, errString)
                }
            })

        biometricPrompt.authenticate(promptInfo)
    }

    // 3. Función para comprobar si el dispositivo es compatible
    fun canAuthenticate(): Boolean {
        val biometricManager = BiometricManager.from(activity)
        val canAuth = biometricManager.canAuthenticate(BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
        Log.e("Biometric","canAuthenticate: $canAuth")
        return canAuth == BiometricManager.BIOMETRIC_SUCCESS
    }
}
