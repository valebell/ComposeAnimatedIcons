package com.valentinbell.composeanimatedicons.icons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
fun Download(
    animate: Boolean,
    modifier: Modifier = Modifier,
    shouldLoop: Boolean = true,
    tint: Color = Color.Black,
    contentDescription: String? = null,
    loopDelayMs: Long = 1000,
) {
    val arrowTranslationY = remember { Animatable(0f) }
    val trayScaleY = remember { Animatable(1f) }

    LaunchedEffect(animate) {
        if (!animate) {
            launch { arrowTranslationY.animateTo(0f, tween(200)) }
            launch { trayScaleY.animateTo(1f, tween(200)) }
        } else {
            do {
                val drop = launch {
                    arrowTranslationY.animateTo(
                        targetValue = 100f,
                        animationSpec = tween(150, easing = FastOutLinearInEasing)
                    )
                }
                drop.join()

                launch {
                    trayScaleY.animateTo(0.8f, tween(80, easing = LinearEasing))
                    trayScaleY.animateTo(1f, spring(Spring.DampingRatioMediumBouncy))
                }

                val bounce = launch {
                    arrowTranslationY.animateTo(0f, spring(Spring.DampingRatioLowBouncy))
                }
                bounce.join()

                if (shouldLoop) delay(loopDelayMs)
            } while (animate && shouldLoop)
        }
    }

    val arrowPath = remember {
        PathParser().parsePathString(
            "M480-320 280-520l56-58 104 104v-326h80v326l104-104 56 58-200 200Z"
        ).toPath()
    }
    val trayPath = remember {
        PathParser().parsePathString(
            "M240-160q-33 0-56.5-23.5T160-240v-120h80v120h480v-120h80v120q0 33-23.5 56.5T720-160H240Z"
        ).toPath()
    }

    Box(
        modifier = modifier
            .size(24.dp)
            .semantics {
                if (contentDescription != null) this.contentDescription = contentDescription
            }
            .drawWithCache {
                val scaleX = size.width / 960f
                val scaleY = size.height / 960f

                onDrawWithContent {
                    withTransform({
                        scale(scaleX, scaleY, Offset.Zero)
                        translate(top = 960f)
                    }) {
                        withTransform({
                            scale(
                                scaleX = 1f,
                                scaleY = trayScaleY.value,
                                pivot = Offset(480f, -160f)
                            )
                        }) {
                            drawPath(path = trayPath, color = tint)
                        }

                        withTransform({
                            translate(top = arrowTranslationY.value)
                        }) {
                            drawPath(path = arrowPath, color = tint)
                        }
                    }
                }
            }
    )
}