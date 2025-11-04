package com.example.sombriyakotlin.ui.main.animations

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun CloudEmojiAnimation(
    modifier: Modifier = Modifier,
    emoji: String = "☁️",
    cloudCount: Int = 15
) {
    val transition = rememberInfiniteTransition(label = "cloud-emoji")
    val screenWidthPx = with(LocalDensity.current) { LocalConfiguration.current.screenWidthDp.dp.toPx() }
    val screenHeightPx = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    Box(modifier = modifier) {
        val clouds = remember {
            List(cloudCount) {
                CloudData(
                    xStart = Random.nextFloat() * screenWidthPx * 0.25f,
                    y = Random.nextFloat() * screenHeightPx * 0.8f,  // en la mitad superior
                    speed = (20f + Random.nextFloat() * 40f)
                )
            }
        }

        clouds.forEach { cloud ->
            val xOffset by transition.animateFloat(
                initialValue = cloud.xStart,
                targetValue = screenWidthPx + 100f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = ( (screenWidthPx + 200f) / cloud.speed * 1000 ).toInt(),
                        easing = LinearEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "cloud_move_${cloud.hashCode()}"
            )
            Text(
                text = emoji,
                fontSize = 60.sp,
                modifier = Modifier
                    .offset { IntOffset(xOffset.toInt(), cloud.y.toInt()) }
            )
        }
    }
}

private data class CloudData(
    val xStart: Float,
    val y: Float,
    val speed: Float
)

