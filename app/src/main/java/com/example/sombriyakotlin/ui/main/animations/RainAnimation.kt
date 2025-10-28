package com.example.sombriyakotlin.ui.main.animations

import android.util.Log
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import com.example.sombriyakotlin.R
import kotlin.random.Random

@Composable
fun RainAnimation(modifier: Modifier = Modifier) {
    val drops = remember { List(50) { Offset(Random.nextFloat(), Random.nextFloat()) } }
    val anim = rememberInfiniteTransition(label = "lluvia")
    val offsetY by anim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lluvia_offset"
    )

    val rainColor = colorResource(R.color.estaciones_button).copy(alpha = 0.5f)

    Canvas(modifier = modifier) {
        drops.forEach { drop ->
            val x = drop.x * size.width
            val y = (drop.y + offsetY) % 1f * size.height
            drawLine(
                color = rainColor,
                start = Offset(x, y),
                end = Offset(x, y + 25f),
                strokeWidth = 6f
            )
        }
    }
}