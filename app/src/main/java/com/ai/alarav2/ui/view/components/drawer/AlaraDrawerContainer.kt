package com.ai.alarav2.ui.view.components.drawer

import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.ai.alarav2.routes.AlaraRoutes
import kotlin.math.roundToInt
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle


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
    isDrawerOpened: Boolean = false,
    drawerWidth: Dp = 280.dp,
    onSwipe: (Boolean) -> Unit = {},
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {
    val density = LocalDensity.current

    // 1. Detect Keyboard State (IME)
    // Note: Ensure your Activity in manifest has android:windowSoftInputMode="adjustResize"
    @OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
    val isImeVisible = WindowInsets.isImeVisible

    BoxWithConstraints(modifier = modifier.fillMaxSize()) {
        val screenWidth = maxWidth

        // 2. Dynamic Width Calculation
        // If keyboard is open AND drawer is open, width = Screen Width. Else 280dp.
        val targetWidth = if (isImeVisible && isDrawerOpened) screenWidth else drawerWidth

        // Animate the width change smoothly
        val animatedDrawerWidth by animateDpAsState(
            targetValue = targetWidth,
            animationSpec = tween(300),
            label = "Width"
        )

        val drawerWidthPx = with(density) { animatedDrawerWidth.toPx() }

        val transition = updateTransition(targetState = isDrawerOpened, label = "Drawer")
        val slidePercent by transition.animateFloat(
            transitionSpec = { tween(300) },
            label = "Slide"
        ) { if (it) 1f else 0f }

        val drawerTranslationX = (slidePercent * drawerWidthPx) - drawerWidthPx
        val contentTranslationX = slidePercent * drawerWidthPx

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { change, dragAmount ->
                        change.consume()
                        // Disable swipe if in full-screen search mode
                        if (!isImeVisible) {
                            if (dragAmount > 20) onSwipe(true)
                            else if (dragAmount < -20) onSwipe(false)
                        }
                    }
                }
        ) {
            // --- DRAWER ---
            Box(
                modifier = Modifier
                    .width(animatedDrawerWidth)
                    .fillMaxHeight()
                    .offset { IntOffset(drawerTranslationX.roundToInt(), 0) }
                    .zIndex(1f)
            ) {
                drawerContent()
            }

            // --- MAIN CONTENT ---
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(contentTranslationX.roundToInt(), 0) }
                    .zIndex(0f)
            ) {
                content()

                // Dimming Overlay / Interaction Blocker
                if (slidePercent > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f * slidePercent))
                            .clickable(
                                enabled = true, // Always capture clicks when open
                                onClick = {
                                    // Only close if NOT in full-screen search mode
                                    if (!isImeVisible) {
                                        onSwipe(false)
                                    }
                                }
                            )
                    )
                }
            }
        }
    }
}

@Composable
fun AlaraDrawer(
    selectedItem: AlaraRoutes,
    onItemClick: (AlaraRoutes) -> Unit,
    onNewChatClick: () -> Unit = {}
) {
    val drawerBg = MaterialTheme.colorScheme.surface
    val contentColor = MaterialTheme.colorScheme.onSurface
    val searchBarBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)

    // State for the search field
    var searchText by remember { mutableStateOf("") }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = drawerBg,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding() // Ensures UI respects safe areas
        ) {
            // --- TOP HEADER ---
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, start = 12.dp, end = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // REAL SEARCH FIELD (Triggers Keyboard)
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(searchBarBg)
                        .padding(horizontal = 12.dp),
                    singleLine = true,
                    textStyle = TextStyle(color = contentColor, fontSize = 14.sp),
                    cursorBrush = SolidColor(contentColor),
                    decorationBox = { innerTextField ->
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = contentColor.copy(alpha = 0.6f),
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Box(modifier = Modifier.weight(1f)) {
                                if (searchText.isEmpty()) {
                                    Text(
                                        text = "Search",
                                        color = contentColor.copy(alpha = 0.6f),
                                        fontSize = 14.sp
                                    )
                                }
                                innerTextField()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                // New Chat Button
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(searchBarBg)
                        .clickable { onNewChatClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Chat",
                        tint = contentColor
                    )
                }
            }

            // --- LIST (Pushes footer down) ---
            LazyColumn(
                modifier = Modifier.weight(1f), // KEY CHANGE: Fills available space
                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 12.dp)
            ) {
                item {
                    Text(
                        text = "Recent",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = contentColor.copy(alpha = 0.5f),
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                    )
                }

                items(5) { index -> // Increased count to test scrolling
                    DrawerChatItem(
                        text = "Project Alara Discussion $index",
                        isSelected = index == 0,
                        onClick = { /* Navigate */ }
                    )
                }
            }

            // --- BOTTOM FOOTER ---
            Column(modifier = Modifier.fillMaxWidth()) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 12.dp),
                    color = contentColor.copy(alpha = 0.1f)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .clickable { /* Open Settings */ }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "MK",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Muhammad Khan",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "Pro Plan",
                            fontSize = 12.sp,
                            color = contentColor.copy(alpha = 0.6f)
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.MoreHoriz,
                        contentDescription = "Settings",
                        tint = contentColor.copy(alpha = 0.6f)
                    )
                }
            }
        }
    }
}

@Composable
fun DrawerChatItem(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val backgroundColor = if (isSelected)
        MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    else
        Color.Transparent

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
            .clip(RoundedCornerShape(6.dp))
            .background(backgroundColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}