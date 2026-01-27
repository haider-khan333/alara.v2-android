package com.ai.alarav2.ui.view.splashscreen


import android.view.animation.OvershootInterpolator
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ai.alarav2.R
import kotlinx.coroutines.delay

@Composable
fun AlaraSplashScreen(onSplashFinished: () -> Unit) {
    val scale = remember { Animatable(0f) }

    // Animation Logic
    LaunchedEffect(key1 = true) {
        // 1. Animate the logo "popping" in
        scale.animateTo(
            targetValue = 1f,
            animationSpec = tween(
                durationMillis = 800,
                easing = {
                    OvershootInterpolator(1.5f).getInterpolation(it)
                }
            )
        )

        // 2. Wait for a few seconds
        delay(2000L) // 2 seconds delay

        // 3. Navigate to main screen
        onSplashFinished()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface) // Use your app's background color
    ) {
        // REPLACE R.drawable.logo with your actual logo resource ID
        Image(
            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
            contentDescription = "App Logo",
            modifier = Modifier
                .size(200.dp) // Adjust size as needed
                .scale(scale.value)
        )
    }
}