package com.valentinbell.composeanimatedicons.icons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.PathParser
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun Rocket(
    animate: Boolean,
    modifier: Modifier = Modifier,
    shouldLoop: Boolean = true,
    tint: Color = Color.Black,
    contentDescription: String? = null,
    loopDelayMs: Long = 1000,
) {
    val activationFactor by animateFloatAsState(
        targetValue = if (animate) 1f else 0f,
        animationSpec = tween(150),
        label = "activation"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "rocket_idle")

    val engineHumRaw by infiniteTransition.animateFloat(
        initialValue = -1.2f,
        targetValue = 1.2f,
        animationSpec = infiniteRepeatable(
            animation = tween(35, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "hum"
    )

    val flameFlickerRaw by infiniteTransition.animateFloat(
        initialValue = 0.94f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(70, easing = FastOutLinearInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flicker"
    )

    val thrustOffset = remember { Animatable(0f) }
    val thrustScale = remember { Animatable(1f) }
    val flightSway = remember { Animatable(0f) }
    val bodyScale = remember { Animatable(1f) }

    LaunchedEffect(animate) {
        if (!animate) {
            launch { thrustOffset.animateTo(0f) }
            launch { thrustScale.animateTo(1f) }
            launch { flightSway.animateTo(0f) }
            launch { bodyScale.animateTo(1f) }
        } else {
            do {
                launch {
                    bodyScale.animateTo(0.92f, tween(100, easing = LinearOutSlowInEasing))
                    bodyScale.animateTo(1f, spring(Spring.DampingRatioLowBouncy, Spring.StiffnessLow))
                }

                launch {
                    delay(50)
                    thrustScale.animateTo(1.15f, tween(120, easing = FastOutLinearInEasing))
                    thrustScale.animateTo(1f, spring(Spring.DampingRatioLowBouncy))
                }

                launch {
                    delay(50)
                    thrustOffset.animateTo(40f, tween(200, easing = FastOutLinearInEasing))
                    thrustOffset.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
                }

                val sway = launch {
                    delay(50)
                    flightSway.animateTo(15f, tween(600, easing = FastOutSlowInEasing))
                    flightSway.animateTo(-15f, tween(1200, easing = FastOutSlowInEasing))
                    flightSway.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
                }

                sway.join()

                if (shouldLoop) delay(loopDelayMs)
            } while (animate && shouldLoop)
        }
    }

    val bodyPath = remember { PathParser().parsePathString("m226-559 78 33q14-28 29-54t33-52l-56-11-84 84Zm142 83 114 113q42-16 90-49t90-75q70-70 109.5-155.5T806-800q-72-5-158 34.5T492-656q-42 42-75 90t-49 90Zm155-121.5q0-33.5 23-56.5t57-23q34 0 57 23t23 56.5q0 33.5-23 56.5t-57 23q-34 0-57-23t-23-56.5ZM565-220l84-84-11-56q-26 18-52 32.5T532-299l33 79Zm313-653q19 121-23.5 235.5T708-419l20 99q4 20-2 39t-20 33L538-80l-84-197-171-171-197-84 167-168q14-14 33.5-20t39.5-2l99 20q104-104 218-147t235-24Z").toPath() }
    val flamePath = remember { PathParser().parsePathString("M157-321q35-35 85.5-35.5T328-322q35 35 34.5 85.5T327-151q-25 25-83.5 43T82-76q14-103 32-161.5t43-83.5Zm57 56q-10 10-20 36.5T180-175q27-4 53.5-13.5T270-208q12-12 13-29t-11-29q-12-12-29-11.5T214-265Z").toPath() }

    Box(
        modifier = modifier
            .size(24.dp)
            .semantics { if (contentDescription != null) this.contentDescription = contentDescription }
            .drawWithCache {
                val scaleX = size.width / 960f
                val scaleY = size.height / 960f
                val pivotFlame = Offset(192f, -198f)
                val pivotBodyCenter = Offset(480f, -480f)

                onDrawWithContent {
                    withTransform({
                        scale(scaleX, scaleY, Offset.Zero)
                        translate(0f, 960f)
                    }) {
                        withTransform({
                            val hum = engineHumRaw * activationFactor
                            translate(hum, -hum)

                            translate(thrustOffset.value, -thrustOffset.value)
                            translate(flightSway.value, flightSway.value)

                            scale(bodyScale.value, bodyScale.value, pivotBodyCenter)
                        }) {
                            drawPath(path = bodyPath, color = tint)

                            withTransform({
                                val currentFlicker = 1f + (flameFlickerRaw - 1f) * activationFactor
                                val scale = thrustScale.value * currentFlicker
                                scale(scale, scale, pivotFlame)
                            }) {
                                drawPath(path = flamePath, color = tint)
                            }
                        }
                    }
                }
            }
    )
}