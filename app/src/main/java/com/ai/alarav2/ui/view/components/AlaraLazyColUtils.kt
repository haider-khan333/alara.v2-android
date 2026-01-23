package com.ai.alarav2.ui.view.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.unit.Velocity
import kotlinx.coroutines.launch
import kotlin.math.sign

val CustomEasing: Easing = CubicBezierEasing(0.5f, 0.5f, 1.0f, 0.25f)

@Composable
fun Modifier.customOverscroll(
    orientation: Orientation,
    onNewOverscrollAmount: (Float) -> Unit,
    animationSpec: SpringSpec<Float> = spring(stiffness = Spring.StiffnessLow)
): Modifier {
    val overscrollAmountAnimatable = remember { Animatable(0f) }
    var length by remember { mutableFloatStateOf(1f) }

    LaunchedEffect(Unit) {
        snapshotFlow { overscrollAmountAnimatable.value }.collect {
            onNewOverscrollAmount(
                CustomEasing.transform(it / (length * 1.5f)) * length
            )
        }
    }

    fun calculateOverscroll(available: Offset): Float {
        val previous = overscrollAmountAnimatable.value
        val newValue = previous + when (orientation) {
            Orientation.Vertical -> available.y
            Orientation.Horizontal -> available.x
        }
        return when {
            previous > 0 -> newValue.coerceAtLeast(0f)
            previous < 0 -> newValue.coerceAtMost(0f)
            else -> newValue
        }
    }

    val scope = rememberCoroutineScope()
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            // we will override the
            // functions here (onPostSroll, onPreFling, etc.)
            override fun onPostScroll(
                consumed: Offset,
                available: Offset,
                source: NestedScrollSource
            ): Offset {
                scope.launch {
                    overscrollAmountAnimatable.snapTo(targetValue = calculateOverscroll(available))

                }
                val delta = if (orientation == Orientation.Vertical) available.y else available.x

                // If pulling DOWN (Top Edge), return Zero.
                // This tells the system "I didn't use this", so the PullToRefreshBox
                // above receives it and triggers the refresh!
                if (delta > 0) {
                    return Offset.Zero
                }

                // For bottom (delta < 0), we consume it so it bounces normally
                return available
//                return Offset.Zero
            }

            override suspend fun onPostFling(consumed: Velocity, available: Velocity): Velocity {
                val availableVelocity = when (orientation) {
                    Orientation.Vertical -> available.y
                    Orientation.Horizontal -> available.x
                }

                overscrollAmountAnimatable.animateTo(
                    targetValue = 0f,
                    initialVelocity = availableVelocity,
                    animationSpec = animationSpec
                )

                return available
            }

            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                if (overscrollAmountAnimatable.value != 0f && source != NestedScrollSource.SideEffect) {
                    scope.launch {
                        overscrollAmountAnimatable.snapTo(calculateOverscroll(available))
                    }
                    // If bouncing at the top (value > 0), return Zero to let Refresh see it too
                    if (overscrollAmountAnimatable.value > 0) {
                        return Offset.Zero
                    }
//                    return available
                    return available
                }

                return super.onPreScroll(available, source)
            }

            override suspend fun onPreFling(available: Velocity): Velocity {
                val availableVelocity = when (orientation) {
                    Orientation.Vertical -> available.y
                    Orientation.Horizontal -> available.x
                }

                if (overscrollAmountAnimatable.value != 0f && availableVelocity != 0f) {
                    val previousSign = overscrollAmountAnimatable.value.sign
                    var consumedVelocity = availableVelocity
                    val predictedEndValue = exponentialDecay<Float>().calculateTargetValue(
                        initialValue = overscrollAmountAnimatable.value,
                        initialVelocity = availableVelocity,
                    )
                    if (predictedEndValue.sign == previousSign) {
                        overscrollAmountAnimatable.animateTo(
                            targetValue = 0f,
                            initialVelocity = availableVelocity,
                            animationSpec = animationSpec,
                        )
                    } else {
                        try {
                            overscrollAmountAnimatable.animateDecay(
                                initialVelocity = availableVelocity,
                                animationSpec = exponentialDecay()
                            ) {
                                if (value.sign != previousSign) {
                                    consumedVelocity -= velocity
                                    scope.launch {
                                        overscrollAmountAnimatable.snapTo(0f)
                                    }
                                }
                            }
                        } catch (e: Exception) {
                        }
                    }

                    return when (orientation) {
                        Orientation.Vertical -> Velocity(0f, consumedVelocity)
                        Orientation.Horizontal -> Velocity(consumedVelocity, 0f)
                    }
                }

                return super.onPreFling(available)
            }
        }
    }


    return this
        .onSizeChanged {
            length = when (orientation) {
                Orientation.Vertical -> it.height.toFloat()
                Orientation.Horizontal -> it.width.toFloat()
            }
        }
        .nestedScroll(nestedScrollConnection)
}


@Composable
fun Modifier.customOverscroll(
    listState: LazyListState,
    onNewOverscrollAmount: (Float) -> Unit,
    animationSpec: SpringSpec<Float> = spring(stiffness = Spring.StiffnessLow)
) = customOverscroll(
    orientation = remember { listState.layoutInfo.orientation },
    onNewOverscrollAmount = onNewOverscrollAmount,
    animationSpec = animationSpec
)