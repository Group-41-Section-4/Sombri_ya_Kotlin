package com.example.sombriyakotlin.ui.main.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Preview
@Composable
fun RainAnimation() {
    val drops = remember { List(50) { Offset(Random.nextFloat(), Random.nextFloat()) } }
    val anim = rememberInfiniteTransition(label = "lluvias")
    val offsetY by anim.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "lluvia_offset"
    )

    Canvas(modifier = Modifier.fillMaxSize()) {
        drops.forEach { drop ->
            val x = drop.x * size.width
            val y = (drop.y + offsetY) % 1f * size.height
            drawLine(
                color = Color.Cyan.copy(alpha = 0.5f),
                start = Offset(x, y),
                end = Offset(x, y + 15f),
                strokeWidth = 2f
            )
        }
    }
}
