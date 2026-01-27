package com.ai.alarav2.ui.view.components.drawer

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.lerp
import com.ai.alarav2.routes.AlaraRoutes
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * A Composable function that provides a drawer container with swipe gestures.
 *
 * @param modifier Modifier for customizing the layout.
 * @param isDrawerOpened Boolean flag to indicate if the drawer is open or closed.
 * @param drawerWidth The width of the drawer in Dp.
 * @param onSwipe Callback function invoked when a swipe gesture is detected.
 * @param content Composable function representing the main content.
 */
@Composable
fun AlaraDrawerContainer(
    modifier: Modifier = Modifier,
    isDrawerOpened: Boolean = true,
    drawerWidth: Dp = 200.dp,
    onSwipe: (Boolean) -> Unit = {},
    content: @Composable () -> Unit
) {
    val transition = updateTransition(targetState = isDrawerOpened, label = "")

    val collapseFraction =
        transition.animateFloat(
            transitionSpec = { tween(durationMillis = 400) },
            label = ""
        ) { state ->
            when (state) {
                false -> 0f
                true -> 1f
            }
        }

    Layout(
        modifier = modifier.pointerInput(Unit) {
            detectHorizontalDragGestures { _, dragAmount ->
                if (!transition.isRunning) {
                    onSwipe(dragAmount > 0 && abs(dragAmount) > 15)
                }
            }
        },
        content = content
    ) { measurables, constraints ->
        val drawerPlaceable = runCatching {
            measurables[0].measure(
                Constraints.fixed(
                    drawerWidth.roundToPx(),
                    constraints.maxHeight
                )
            )
        }.getOrNull()
        val contentPlaceable = runCatching { measurables[1].measure(constraints) }.getOrNull()

        val drawerX = lerp(
            -drawerWidth.roundToPx().toFloat(),
            0f,
            collapseFraction.value
        ).roundToInt()

        val contentX = lerp(
            0f,
            drawerWidth.roundToPx().toFloat(),
            collapseFraction.value
        ).roundToInt()


        layout(
            width = constraints.maxWidth,
            height = constraints.maxHeight
        ) {
            drawerPlaceable?.place(x = drawerX, y = 0)
            contentPlaceable?.place(x = contentX, y = 0)
        }
    }
}


@Composable
fun AlaraDrawer(selectedItem: AlaraRoutes, onItemClick: (AlaraRoutes) -> Unit) {
    Column(modifier = Modifier.background(Color.White)) {
        // Header Section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.LightGray)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.AccountCircle, contentDescription = "Profile", tint = Color.DarkGray)
        }

        // Drawer Items
        val items = listOf(AlaraRoutes.Dashboard)
        items.forEach { item ->
            val backgroundColor = if (item == selectedItem) Color.Gray else Color.White
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(16.dp)
                    .clickable { onItemClick(item) }
            ) {
                Text(text = item.toString(), color = Color.Black)
            }
        }


    }
}