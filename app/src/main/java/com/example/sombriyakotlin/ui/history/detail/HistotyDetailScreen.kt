package com.example.sombriyakotlin.ui.history.detail

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.compose.material3.Scaffold
import com.example.sombriyakotlin.domain.model.Rental
import com.example.sombriyakotlin.feature.history.HistoryViewModel
import com.example.sombriyakotlin.ui.layout.TopBarMini
import java.time.Duration
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryDetailScreen(
    navController: NavHostController,
    historyVM: HistoryViewModel
) {
    val state by historyVM.detailState.collectAsState()
    val bg = Color(0xFFF7F7F7)

    Scaffold(
        containerColor = bg,
        topBar = { TopBarMini(navController, "Detalles") },
    ) { inner ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bg)
                .padding(inner)
        ) {
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Text(
                        text = state.error!!,
                        modifier = Modifier.padding(16.dp),
                        color = MaterialTheme.colors.error
                    )
                }

                state.detail != null -> {
                    HistoryDetailContent(detail = state.detail!!)
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun HistoryDetailContent(
    detail: Rental,
) {
    // Colores del mockup
    val primaryBlue = Color(0xFF00B2F6)
    val accentRed = Color(0xFFEF4444)
    val surface = Color(0xFFFFFFFF)
    val textPrimary = Color(0xFF1F2937)
    val textSecondary = Color(0xFF6B7280)

    // Parseo básico de fechas/horas
    val startDateTime = detail.startedAt.toLocalDateTimeOrNull()
    val endDateTime = detail.endedAt?.toLocalDateTimeOrNull()

    val dateFormatter = DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale("es", "ES"))
    val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale("es", "ES"))

    val dateText = startDateTime?.format(dateFormatter)
        ?.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("es", "ES")) else it.toString() }
        ?: detail.startedAt

    val startTimeText = startDateTime?.format(timeFormatter) ?: "--:--"
    val endTimeText = endDateTime?.format(timeFormatter) ?: "--:--"

    val durationText = if (startDateTime != null && endDateTime != null) {
        val minutes = Duration.between(startDateTime, endDateTime)
            .toMinutes()
            .coerceAtLeast(0)
        "Duración total: $minutes minutos"
    } else {
        "Duración total: --"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 24.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    elevation = 8.dp,
                    shape = RoundedCornerShape(24.dp),
                    clip = true
                )
                .background(surface)
                .padding(horizontal = 24.dp, vertical = 24.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Fecha + duración
                Text(
                    text = dateText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textPrimary,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = durationText,
                    fontSize = 14.sp,
                    color = textSecondary,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Línea de tiempo (inicio / fin)
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "INICIO",
                            fontSize = 10.sp,
                            color = textSecondary
                        )
                        Text(
                            text = startTimeText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Punto azul
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = primaryBlue.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(primaryBlue, CircleShape)
                            )
                        }

                        // Línea
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(2.dp)
                                .padding(horizontal = 4.dp)
                                .background(Color(0xFFD1D5DB))
                        )

                        // Punto rojo
                        Box(
                            modifier = Modifier
                                .size(16.dp)
                                .background(
                                    color = accentRed.copy(alpha = 0.15f),
                                    shape = CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(10.dp)
                                    .background(accentRed, CircleShape)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "FIN",
                            fontSize = 10.sp,
                            color = textSecondary
                        )
                        Text(
                            text = endTimeText,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = textPrimary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    primaryBlue.copy(alpha = 0.15f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = primaryBlue
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Punto de inicio",
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = detail.stationStartId.ifBlank { "Información de estación no disponible" },
                                color = textSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                            .width(1.dp)
                            .height(32.dp)
                            .background(Color(0xFFD1D5DB))
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.Top
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(
                                    accentRed.copy(alpha = 0.15f),
                                    CircleShape
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            androidx.compose.material.Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = accentRed
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Punto final",
                                fontWeight = FontWeight.SemiBold,
                                color = textPrimary,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Información no disponible", // aquí puedes poner dirección / estación de destino
                                color = textSecondary,
                                fontSize = 13.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // "Reportar un Problema"
//                Button(
//                    onClick = { /* TODO: acción de reporte */ },
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(48.dp),
//                    shape = RoundedCornerShape(12.dp),
//                    elevation = ButtonDefaults.elevation(0.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        backgroundColor = Color(0xFFFFE4E6),
//                        contentColor = accentRed
//                    )
//                ) {
//                    Text(
//                        text = "Reportar un Problema",
//                        fontWeight = FontWeight.SemiBold,
//                        fontSize = 14.sp
//                    )
//                }
            }
        }
    }
}

/**
 * Intenta parsear un String a LocalDateTime con algunos formatos comunes.
 * Si falla, devuelve null.
 */
@RequiresApi(Build.VERSION_CODES.O)
private fun String.toLocalDateTimeOrNull(): LocalDateTime? {
    if (this.isBlank()) return null
    return runCatching {
        OffsetDateTime.parse(this).toLocalDateTime()
    }.getOrElse {
        runCatching {
            LocalDateTime.parse(this)
        }.getOrNull()
    }
}
