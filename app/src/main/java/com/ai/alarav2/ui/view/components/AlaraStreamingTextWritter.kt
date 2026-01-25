package com.ai.alarav2.ui.view.components

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun StreamingFadeText(
    text: String,
    modifier: Modifier = Modifier
) {
    var stable by remember { mutableStateOf("") }
    var animatedDelta by remember { mutableStateOf("") }

    val alpha = remember { androidx.compose.animation.core.Animatable(1f) }
    val offset = remember { androidx.compose.animation.core.Animatable(0f) }

    LaunchedEffect(text) {
        // compute delta safely
        val delta = if (text.startsWith(stable)) {
            text.removePrefix(stable)
        } else {
            // non-monotonic update: replace everything
            stable = ""
            text
        }

        if (delta.isEmpty()) return@LaunchedEffect

        // ✅ move previous delta into stable (so only the new part animates)
        stable += animatedDelta
        animatedDelta = delta

        // animate only new delta
        alpha.snapTo(0.2f)
        offset.snapTo(6f)
        alpha.animateTo(1f, animationSpec = tween(140))
        offset.animateTo(0f, animationSpec = tween(140))
    }

    Column(modifier = modifier) {
        // stable text (no animation)
        if (stable.isNotEmpty()) {
            Text(
                text = stable,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        // animated new part
        if (animatedDelta.isNotEmpty()) {
            Text(
                text = animatedDelta,
                modifier = Modifier.graphicsLayer {
                    this.alpha = alpha.value
                    translationY = offset.value
                },
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}


//@Composable
//fun StreamingFadeText(
//    fullText: String,
//    modifier: Modifier = Modifier,
//    charDelayMs: Long = 1L,          // speed
//    fadeInDurationMs: Int = 10       // fade of new chars
//) {
//    var shown by remember { mutableStateOf("") }
//    var lastTarget by remember { mutableStateOf("") }
//
//    // When fullText grows, append only the delta with animation
//    LaunchedEffect(fullText) {
//        if (fullText.startsWith(lastTarget)) {
//            val delta = fullText.removePrefix(lastTarget)
//            for (c in delta) {
//                shown += c
//                kotlinx.coroutines.delay(charDelayMs)
//            }
//            lastTarget = fullText
//        } else {
//            // if text changed non-monotonically, reset
//            shown = fullText
//            lastTarget = fullText
//        }
//    }
//
//    // Fade in the whole block gently (Gemini vibe)
//    val alpha by androidx.compose.animation.core.animateFloatAsState(
//        targetValue = 1f,
//        animationSpec = androidx.compose.animation.core.tween(durationMillis = fadeInDurationMs),
//        label = "fadeIn"
//    )
//
//    Text(
//        text = shown,
//        modifier = modifier.graphicsLayer(alpha = alpha),
//        style = MaterialTheme.typography.bodyMedium
//    )
//}

//
//@Composable
//fun StreamingFadeText(
//    text: String,
//    modifier: Modifier = Modifier
//) {
//    var lastText by remember { mutableStateOf("") }
//    var displayText by remember { mutableStateOf("") }
//
//    // Detect only newly added chunk
//    LaunchedEffect(text) {
//        if (text.startsWith(lastText)) {
//            displayText = text
//        } else {
//            displayText = text
//        }
//        lastText = text
//    }
//
//    val alpha by animateFloatAsState(
//        targetValue = 1f,
//        animationSpec = tween(
//            durationMillis = 90,     // 🔥 FAST (Gemini-like)
//            easing = FastOutSlowInEasing
//        ),
//        label = "fade"
//    )
//
//    val offsetY by animateDpAsState(
//        targetValue = 0.dp,
//        animationSpec = tween(
//            durationMillis = 90,
//            easing = FastOutSlowInEasing
//        ),
//        label = "slide"
//    )
//
//    Text(
//        text = displayText,
//        modifier = modifier
//            .graphicsLayer {
//                this.alpha = alpha
//                translationY = offsetY.toPx()
//            },
//        style = MaterialTheme.typography.bodyMedium
//    )
//}
//
