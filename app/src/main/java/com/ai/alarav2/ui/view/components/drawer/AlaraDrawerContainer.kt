package com.ai.alarav2.ui.view.components.drawer

import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.isImeVisible
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.rounded.EditNote
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.LoadingIndicator
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import com.ai.alarav2.data.models.ui.AlaraChatUiModels
import com.ai.alarav2.ui.theme.AlaraColors
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.vm.chathistory.AlaraChatHistoryState
import com.ai.alarav2.vm.chathistory.AlaraChatHistoryUiState
import com.ai.alarav2.vm.drawer.AlaraDrawerViewModel


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
    drawerWidth: Dp = 320.dp,
    viewModel: AlaraDrawerViewModel,
    drawerContent: @Composable () -> Unit,
    content: @Composable () -> Unit
) {


    val isDrawerOpen = viewModel.drawerState.collectAsState().value


    BackHandler(enabled = isDrawerOpen) {
        viewModel.closeDrawer()

    }

    val density = LocalDensity.current
    val drawerState = viewModel.drawerState.collectAsState().value

    @OptIn(ExperimentalLayoutApi::class)
    val isImeVisible = WindowInsets.isImeVisible

    BoxWithConstraints(
        modifier = modifier
            .fillMaxSize()
    ) {
        val screenWidth = maxWidth

        val targetWidth = if (isImeVisible && drawerState) screenWidth else drawerWidth

        val animatedDrawerWidth by animateDpAsState(
            targetValue = targetWidth,
            animationSpec = tween(300),
            label = "Width"
        )

        val drawerWidthPx = with(density) { animatedDrawerWidth.toPx() }

        val transition = updateTransition(targetState = drawerState, label = "Drawer")
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
                        if (!isImeVisible) {
                            if (dragAmount > 20) viewModel.openDrawer()
                            else if (dragAmount < -20) viewModel.closeDrawer()
                        }
                    }
                }
        ) {
            Box(
                modifier = Modifier
                    .width(animatedDrawerWidth)
                    .fillMaxHeight()
                    .offset { IntOffset(drawerTranslationX.roundToInt(), 0) }
                    .zIndex(1f)
            ) {
                drawerContent()
            }

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .offset { IntOffset(contentTranslationX.roundToInt(), 0) }
                    .zIndex(0f)
            ) {
                content()

                if (slidePercent > 0) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.3f * slidePercent))
                            .clickable(
                                enabled = true,
                                onClick = {
                                    if (!isImeVisible) {
                                        viewModel.closeDrawer()
                                    }
                                }
                            )
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlaraDrawer(
    selectedItem: AlaraRoutes,
    onItemClick: (AlaraRoutes) -> Unit,
    onNewChatClick: () -> Unit = {},
    drawerVm: AlaraDrawerViewModel
) {
    val state = drawerVm.state.collectAsState().value
    val drawerBg = AlaraColors.Background
    val contentColor = AlaraColors.MainContent
    val searchBarBg = AlaraColors.Input
    val isLoading = remember { mutableStateOf(false) }
    val list = remember { mutableStateListOf<AlaraChatUiModels>() }

    when (state) {
        is AlaraChatHistoryUiState.Loading -> {
            isLoading.value = true
        }

        is AlaraChatHistoryUiState.Success -> {
            isLoading.value = false
            list.clear()
            list.addAll(state.response)
        }

        else -> {
            isLoading.value = false
            list.clear()
        }
    }

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
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, start = 12.dp, end = 12.dp, bottom = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                BasicTextField(
                    value = searchText,
                    onValueChange = { searchText = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(20.dp))
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
                                    AlaraText(
                                        text = "Search",
                                        color = contentColor.copy(alpha = 0.6f),

                                        )
                                }
                                innerTextField()
                            }
                        }
                    }
                )

                Spacer(modifier = Modifier.width(8.dp))

                AlaraIconButton(
                    onClick = {},
                    imageVector = Icons.Rounded.EditNote,
                    contentDescription = "Eidt",
                    painter = null,
                    iconTint = AlaraColors.MainContent
                )
            }

            LazyColumn(
                modifier = Modifier.weight(1f), // KEY CHANGE: Fills available space
                contentPadding = PaddingValues(horizontal = 12.dp)
            ) {
                if (isLoading.value) {
                    item {
                        LoadingIndicator()
                    }
                } else {
                    item {
                        AlaraText(
                            text = "Recent",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = contentColor.copy(alpha = 0.5f),
                            modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp)
                        )
                    }

                    items(
                        items = list,
                        key = { it.id }) { msg -> // Increased count to test scrolling
                        DrawerChatItem(
                            text = msg.message,
                            isSelected = false,
                            onClick = { /* Navigate */ }
                        )
                    }
                }

            }

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
                        .clickable {
                            onItemClick(AlaraRoutes.Settings)

                        }
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(36.dp),
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            AlaraText(
                                text = "MK",
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        AlaraText(
                            text = "Muhammad Khan",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 14.sp,
                            color = contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        AlaraText(
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

        AlaraText(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}