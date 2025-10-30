package com.example.sombriyakotlin.ui.rent.scan

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.sombriyakotlin.ui.rent.RentViewModel
import com.example.sombriyakotlin.ui.rent.QrViewModel

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun QrScannerScreen(
    modifier: Modifier = Modifier,
    viewModel: QrViewModel = hiltViewModel(),
    rentViewModel: RentViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val qrCode by viewModel.qrCode.collectAsState()
    val isDarkMode = isSystemInDarkTheme()


    // Cada vez que se detecta un código QR, procesamos la reserva
    LaunchedEffect(qrCode) {
        qrCode?.let { stationId ->
            Log.d("RENT", "Código QR detectado: $stationId")
            rentViewModel.setQr()
            rentViewModel.handleScan(stationId)
        }
    }

    // 🔹 Manejo del permiso de cámara
    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted -> hasCameraPermission = granted }
    )

    LaunchedEffect(Unit) {
        if (!hasCameraPermission) launcher.launch(Manifest.permission.CAMERA)
    }


    Box(modifier = modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            // Vista de la cámara
            AndroidView(
                factory = { ctx ->
                    val previewView = PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)

                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = Preview.Builder().build().also {
                            it.setSurfaceProvider(previewView.surfaceProvider)
                        }

                        val imageAnalyzer = ImageAnalysis.Builder()
                            .build()
                            .also {
                                it.setAnalyzer(
                                    ContextCompat.getMainExecutor(ctx),
                                    viewModel.getAnalyzer()
                                )
                            }

                        val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(
                                lifecycleOwner,
                                cameraSelector,
                                preview,
                                imageAnalyzer
                            )
                        } catch (e: Exception) {
                            Log.e("CameraX", "Error binding camera", e)
                        }
                    }, ContextCompat.getMainExecutor(ctx))

                    previewView
                },
                modifier = Modifier.fillMaxSize()
            )


            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer(alpha = 0.99f)
                    .drawWithCache {
                        val squareSize = size.minDimension * 0.7f
                        val left = (size.width - squareSize) / 2
                        val top = (size.height - squareSize) / 2
                        val rect = Rect(left, top, left + squareSize, top + squareSize)
                        val cornerRadius = CornerRadius(32f)

                        onDrawWithContent {
                            drawContent()
                            drawRect(Color.Black.copy(alpha = 0.6f)) // Fondo oscuro
                            drawRoundRect( // Área transparente
                                color = Color.Transparent,
                                topLeft = rect.topLeft,
                                size = rect.size,
                                cornerRadius = cornerRadius,
                                blendMode = BlendMode.Clear
                            )
                            drawRoundRect( // Borde punteado
                                color = Color.White,
                                topLeft = rect.topLeft,
                                size = rect.size,
                                style = Stroke(
                                    width = 4.dp.toPx(),
                                    pathEffect = PathEffect.dashPathEffect(floatArrayOf(30f, 30f), 0f)
                                ),
                                cornerRadius = cornerRadius
                            )
                        }
                    }
            )

            // Texto de ayuda
            Text(
                text = "Apunta la cámara al código QR",
                color = Color.White,
                fontSize = 18.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 48.dp)
            )

            // Mostrar el QR detectado
            qrCode?.let {
                Text(
                    text = "QR detectado: $it",
                    color = Color.White,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 32.dp)
                )
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                var cColor = Color.Black
                if (isDarkMode) {cColor =Color.White}
                Text(
                    text = "¡Upsss! \nParece que necesitas la cámara para esto.",
                    color = cColor,
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Coso de la camara como instagram
                androidx.compose.material3.Text(
                    text = "Acepta los permisos de cámara en Configuración",
                    color = Color(0xFF80CBC4), // tono turquesa claro
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .clickable {
                            val intent = android.content.Intent(
                                android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                android.net.Uri.fromParts("package", context.packageName, null)
                            )
                            context.startActivity(intent)
                        }
                        .padding(8.dp)
                )
            }
        }

    }
}
