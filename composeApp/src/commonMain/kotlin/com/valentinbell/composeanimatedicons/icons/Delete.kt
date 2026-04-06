package com.valentinbell.composeanimatedicons.icons

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
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
fun Delete(
    animate: Boolean,
    modifier: Modifier = Modifier,
    shouldLoop: Boolean = true,
    tint: Color = Color.Black,
    contentDescription: String? = null,
    loopDelayMs: Long = 1000,
) {
    val lidRotation = remember { Animatable(0f) }
    val lidTranslationY = remember { Animatable(0f) }

    LaunchedEffect(animate) {
        if (!animate) {
            launch { lidRotation.animateTo(0f, tween(200)) }
            launch { lidTranslationY.animateTo(0f, tween(200)) }
        } else {
            do {
                val translation = launch {
                    lidTranslationY.animateTo(-4f, tween(150, easing = FastOutSlowInEasing))
                    lidTranslationY.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
                }
                val rotation = launch {
                    lidRotation.animateTo(-30f, tween(250, easing = FastOutSlowInEasing))
                    lidRotation.animateTo(0f, spring(Spring.DampingRatioMediumBouncy))
                }

                translation.join()
                rotation.join()

                if (shouldLoop) delay(loopDelayMs)
            } while (animate && shouldLoop)
        }
    }

    val bodyPath = remember { PathParser().parsePathString("M6,19c0,1.1,0.9,2,2,2h8c1.1,0,2-0.9,2-2V7H6V19z").toPath() }
    val lidPath = remember { PathParser().parsePathString("M19,4h-3.5l-1-1h-5l-1,1H5v2h14V4z").toPath() }

    Box(
        modifier = modifier
            .size(24.dp)
            .semantics {
                if (contentDescription != null) this.contentDescription = contentDescription
            }
            .drawWithCache {
                val scaleX = size.width / 24f
                val scaleY = size.height / 24f

                onDrawWithContent {
                    withTransform({
                        scale(scaleX, scaleY, Offset.Zero)
                    }) {
                        drawPath(path = bodyPath, color = tint)

                        withTransform({
                            translate(top = lidTranslationY.value)
                            rotate(
                                degrees = lidRotation.value,
                                pivot = Offset(5f, 6f)
                            )
                        }) {
                            drawPath(path = lidPath, color = tint)
                        }
                    }
                }
            }
    )
}
