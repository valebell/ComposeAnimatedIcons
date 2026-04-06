package com.valentinbell.composeanimatedicons.icons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
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
fun Fullscreen(
    animate: Boolean,
    modifier: Modifier = Modifier,
    shouldLoop: Boolean = true,
    tint: Color = Color.Black,
    contentDescription: String? = null,
    loopDelayMs: Long = 1000,
) {
    val focusProgress = remember { Animatable(1f) }

    LaunchedEffect(animate) {
        if (!animate) {
            launch { focusProgress.animateTo(1f, tween(200)) }
        } else {
            do {
                val focusJob = launch {
                    focusProgress.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(
                            durationMillis = 250,
                            easing = LinearOutSlowInEasing
                        )
                    )
                    focusProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = spring(Spring.DampingRatioMediumBouncy)
                    )
                }

                focusJob.join()

                if (shouldLoop) delay(loopDelayMs)
            } while (animate && shouldLoop)
        }
    }

    val pTL = remember { PathParser().parsePathString("M120-840v200h80v-120h120v-80H120Z").toPath() }
    val pBL = remember { PathParser().parsePathString("M120-120v-200h80v120h120v80H120Z").toPath() }
    val pTR = remember { PathParser().parsePathString("M840-840v200h-80v-120h-120v-80H840Z").toPath() }
    val pBR = remember { PathParser().parsePathString("M840-120v-200h-80v120h-120v80H840Z").toPath() }

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
                        val maxContraction = 160f
                        val offset = (1f - focusProgress.value) * maxContraction

                        withTransform({ translate(left = -offset, top = -offset) }) {
                            drawPath(pTL, tint)
                        }

                        withTransform({ translate(left = -offset, top = offset) }) {
                            drawPath(pBL, tint)
                        }

                        withTransform({ translate(left = offset, top = -offset) }) {
                            drawPath(pTR, tint)
                        }

                        withTransform({ translate(left = offset, top = offset) }) {
                            drawPath(pBR, tint)
                        }
                    }
                }
            }
    )
}