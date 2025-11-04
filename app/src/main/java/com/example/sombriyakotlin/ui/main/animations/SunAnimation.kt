package com.example.sombriyakotlin.ui.main.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun SunRaysAnimation(modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "sun_rays")
    val rotation by transition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sun_rays_rot"
    )

    Canvas(modifier = modifier) {
        val center = Offset(-10f, -10f)
        val radius = size.minDimension * 0.1f
        val rayCount = 12
        val rayWidth = radius * 2f
        val rayLength = radius * 10f

        rotate(rotation, pivot = center) {
            for (i in 0 until rayCount) {
                val angle = (360f / rayCount) * i
                val rad = Math.toRadians(angle.toDouble()).toFloat()
                val start = Offset(
                    center.x + cos(rad) * (radius * 0.2f),
                    center.y + sin(rad) * (radius * 0.2f)
                )
                val end = Offset(
                    center.x + cos(rad) * rayLength,
                    center.y + sin(rad) * rayLength
                )

                val brush = Brush.linearGradient(
                    colors = listOf(Color(0xFFFFE082).copy(alpha = 0.8f), Color.Transparent),
                    start = start,
                    end = end
                )

                drawLine(
                    brush = brush,
                    strokeWidth = rayWidth,
                    cap = StrokeCap.Round,
                    start = start,
                    end = end
                )
            }
        }
    }
}
