package com.ai.alarav2.ui.view.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.VectorConverter
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun AlaraPushRefresh(
    isRefreshing: Boolean,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier,
    indicator: @Composable (pullFraction: Float) -> Unit,
    content: @Composable () -> Unit
) {
    val scope = rememberCoroutineScope()
    val offsetY = remember { Animatable(0f, Float.VectorConverter) }

    // Threshold to trigger refresh (80dp)
    val triggerPx = with(LocalDensity.current) { 80.dp.toPx() }

    // 1. Handle State Changes (Snap back when done)
    LaunchedEffect(isRefreshing) {
        if (!isRefreshing) {
            offsetY.animateTo(0f)
        }
    }

    val nestedScrollConnection = remember {
        object : NestedScrollConnection {

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // If refreshing, freeze interactions to prevent fighting animations
                if (isRefreshing) return Offset.Zero

                val delta = available.y
                // If the user is pulling DOWN (delta > 0) while we already have an offset
                // OR pushing UP (delta < 0) to close it:
                if (offsetY.value > 0 || (offsetY.value == 0f && delta > 0)) {
                    val newOffset =
                        (offsetY.value + delta * 0.5f).coerceAtLeast(0f) // 0.5f = drag resistance

                    scope.launch { offsetY.snapTo(newOffset) }

                    // We consume the scroll so LazyColumn doesn't move
                    return if (newOffset > 0) available else Offset.Zero
                }
                return Offset.Zero
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                if (isRefreshing) return available // Consume velocity so list doesn't fling

                if (offsetY.value > 0) {
                    // FIX: We are handling the fling via our animation.
                    // We MUST consume the available velocity so the LazyColumn doesn't
                    // try to scroll internally while we are animating the container.

                    if (offsetY.value >= triggerPx) {
                        onRefresh()
                        offsetY.animateTo(triggerPx)
                    } else {
                        offsetY.animateTo(0f)
                    }
                    // Return 'available' means "I ate all the velocity, child views get nothing"
                    return available
                }

                return super.onPreFling(available)
            }
        }
    }

    Box(
        modifier = modifier
            .nestedScroll(nestedScrollConnection)
    ) {
        // 1. Indicator Layer (Background)
        Box(modifier = Modifier.align(Alignment.TopCenter)) {
            val fraction = (offsetY.value / triggerPx).coerceIn(0f, 1f)
            indicator(fraction)
        }

        // 2. Content Layer (Foreground)
        Box(
            modifier = Modifier
                .offset { IntOffset(0, offsetY.value.roundToInt()) }
        ) {
            content()
        }
    }
}