package com.ai.alarav2.ui.view.chat

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.MenuBook
import androidx.compose.material.icons.automirrored.rounded.MenuOpen
import androidx.compose.material.icons.automirrored.rounded.ViewSidebar
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ThumbDownAlt
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import com.ai.alarav2.R
import com.ai.alarav2.data.models.ui.AlaraChatUiModels
import com.ai.alarav2.ui.theme.AlaraColors
import com.ai.alarav2.ui.view.chat.components.AlaraClickType
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.chat.components.AlaraTextBar
import com.ai.alarav2.ui.view.components.AlaraAnimatedBottomSheet
import com.ai.alarav2.ui.view.components.AlaraHeader
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.ui.view.components.clickableWithOpaqueText
import com.ai.alarav2.ui.view.components.markdown.AlaraMarkdownText
import com.ai.alarav2.vm.chat.AlaraChatUiState
import com.ai.alarav2.vm.chat.AlaraChatViewModel
import com.mikepenz.markdown.compose.components.MarkdownComponents
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.MarkdownColors
import com.mikepenz.markdown.model.MarkdownState
import com.mikepenz.markdown.model.MarkdownTypography
import com.mikepenz.markdown.model.State
import com.mikepenz.markdown.model.rememberMarkdownState
import customOverscroll
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlin.math.log
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlaraChatComposable(windowWidthSizeClass: WindowWidthSizeClass) {
    var chatMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val chatViewModel: AlaraChatViewModel = hiltViewModel()
    val messages = chatViewModel.messages.collectAsState().value
    val sheetVisible = chatViewModel.showSheet.collectAsState().value
    val selectedModel = chatViewModel.selectedModel.collectAsState().value
    val chatState = chatViewModel.chatState.collectAsState().value
    val isStreaming = chatState is AlaraChatUiState.Streaming
    val lastBotId = messages.lastOrNull { !it.isUser }?.id
    val markdownCache = remember { mutableStateMapOf<String, MarkdownState>() }


    Scaffold(
        topBar = {
            AlaraHeader(
                containerColor = MaterialTheme.colorScheme.background,
                content = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        IconButton(
                            onClick = { },
                            Modifier.size(30.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = null
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        AlaraText(text = stringResource(R.string.app_name))

                    }
                },
                actions = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(MaterialTheme.colorScheme.background)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickableWithOpaqueText {
                                    chatViewModel.setClickType(AlaraClickType.MODELS)
                                    chatViewModel.showSheet()
                                }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            AlaraText(
                                text = selectedModel,
                                fontSize = 16.sp,
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowDown,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            AlaraTextBar(
                value = chatMessage,
                onValueChange = { chatMessage = it },
                onClick = { type ->
                    chatViewModel.setClickType(type)
                    chatViewModel.showSheet()
                },
                onSend = {
                    chatViewModel.sendMessage(message = chatMessage)
                    chatMessage = ""
                },
                onStop = {
                    chatViewModel.stopStreaming()
                },
                isLoading = chatState is AlaraChatUiState.Loading || isStreaming
            )
        },
        content = { contentPadding ->
            var animatedOverscrollAmount by remember { mutableFloatStateOf(0f) }

            Box(
                modifier = Modifier
                    .customOverscroll(
                        listState,
                        onNewOverscrollAmount = { animatedOverscrollAmount = it }
                    )
                    .offset { IntOffset(0, animatedOverscrollAmount.roundToInt()) }
            ) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(contentPadding)
                        .padding(5.dp)
                ) {
                    if (messages.isEmpty()) {
                        item {
                            Box(
                                modifier = Modifier.fillParentMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                AlaraInitMessage(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 10.dp),
                                    text = "How can I help you today?"
                                )
                            }
                        }
                    }

                    items(
                        items = messages,
                        key = { it.id },
                        contentType = { if (it.isUser) "user" else "bot" }
                    ) { msg ->
                        if (msg.isUser) {
                            AlaraUserMessage(message = msg.message)
                        } else {
                            // Use key to prevent recreation
                            key(msg.id) {
                                AlaraBotMessage(
                                    msg = msg,
                                    isStreaming = isStreaming && msg.id == lastBotId,
                                    markdownState = markdownCache
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                    }

                    if (chatState is AlaraChatUiState.Loading) {
                        item {
                            AlaraBotMessage(
                                isLoading = true,
                            )
                        }
                    }

                }
            }


            AlaraAnimatedBottomSheet(
                containerColor = AlaraColors.SheetColors,
                isVisible = sheetVisible,
                onDismissRequest = { chatViewModel.hideSheet() }
            ) {
                val uploadOptions = chatViewModel.uploadOptions.collectAsState().value
                val models = chatViewModel.models.collectAsState().value

                // Use a single LazyColumn for the entire sheet to handle scrolling properly
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {

                    // SECTION 1: Upload Options (Horizontal Rounded Blocks, filling the row evenly)
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            uploadOptions.take(3).forEach { option -> // Take first 3 to fit evenly
                                AlaraUploadBlock(
                                    icon = option.icon,
                                    text = option.text,
                                    onClick = { /* Handle click */ },
                                    modifier = Modifier.weight(1f) // Each block gets equal weight to fill row
                                )
                            }
                        }
                    }

                    // SECTION 2: Divider
                    item {
                        Divider(
                            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f)
                        )
                    }

                    // SECTION 3: Models List
                    item {
                        AlaraText(
                            text = "Alara Models",
                            fontSize = 14.sp,
                            color = AlaraColors.TextSecondary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }

                    items(models.size) { index ->
                        AlaraModelSelection(
                            cardColor = Color.Transparent, // Transparent to blend with sheet
                            heading = models[index].heading,
                            subHeading = models[index].subHeading,
                            isSelected = models[index].isSelected,
                            onClick = { selectedModel ->
                                chatViewModel.updateSelection(index)
                                chatViewModel.setModel(selectedModel)
                                // Optional: Close sheet on model selection
                                // chatViewModel.hideSheet()
                            }
                        )
                    }
                }
            }

        }
    )
}


@Composable
fun AlaraUploadBlock(
    icon: ImageVector,
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {
        Card(
            onClick = onClick,
            shape = RoundedCornerShape(20.dp), // Soft rounded corners
            colors = CardDefaults.cardColors(
                containerColor = AlaraColors.ImgBg
            ),
            modifier = Modifier
                .fillMaxWidth() // Fill the weighted space
                .aspectRatio(1f) // Make it squarish
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    AlaraText(
                        text = text,
                        fontSize = 12.sp,
                        maxLines = 1,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

@Composable
fun AlaraModelSelection(
    modifier: Modifier = Modifier,
    heading: String,
    subHeading: String,
    isSelected: Boolean = false,
    onClick: (String) -> Unit = {},
    cardColor: Color = Color.Unspecified
) {
    Card(
        onClick = { onClick(heading) },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Column(modifier = modifier) {
                AlaraText(text = heading)
                Spacer(modifier = Modifier.height(3.dp))
                AlaraText(
                    text = subHeading,
                    fontSize = 14.sp,
                    color = AlaraColors.TextSecondary,
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            if (isSelected) {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun AlaraFileSelection(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    text: String,
    onClick: (String) -> Unit = {},
    cardColor: Color = Color.Unspecified
) {
    Card(
        onClick = { onClick(text) },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(imageVector = icon, null)
            Spacer(modifier = Modifier.width(10.dp))
            AlaraText(text = text)
        }
    }
}

@Composable
fun AlaraInitMessage(modifier: Modifier = Modifier, text: String) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlaraText(text = text, fontSize = 22.sp, fontFamily = FontFamily.Serif)
    }
}

@Composable
fun AlaraUserMessage(message: String) {
    BoxWithConstraints(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.CenterEnd
    ) {
        val maxWidth = maxWidth * 0.75f

        Box(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .background(
                    color = MaterialTheme.colorScheme.inverseOnSurface,
                    shape = RoundedCornerShape(
                        topStart = 13.dp,
                        topEnd = 13.dp,
                        bottomStart = 13.dp
                    )
                )
                .padding(vertical = 10.dp, horizontal = 18.dp)
        ) {
            AlaraText(
                text = message,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f),
                softWrap = true
            )
        }
    }
}

@Composable
fun AlaraBotMessage(
    modifier: Modifier = Modifier,
    msg: AlaraChatUiModels? = null,
    isLoading: Boolean = false,
    isStreaming: Boolean = false,
    isError: Boolean = false,
    markdownState: MutableMap<String, MarkdownState> = mutableMapOf()

) {
    Column(modifier = modifier) {
        // Header with icon and label
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier.padding(end = 10.dp)) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(40.dp),
                        color = MaterialTheme.colorScheme.primary
                    )
                } else {
                    Icon(
                        painterResource(R.mipmap.ic_launcher_foreground),
                        contentDescription = null,
                        Modifier.size(40.dp)
                    )
                }
            }


            AlaraText(
                text = when {
                    isLoading -> "Thinking..."
                    isError -> "Error"
                    else -> stringResource(R.string.app_name)
                }
            )
        }

        val messageContent = msg?.message ?: ""
        if (isStreaming) {
            AlaraText(text = msg?.message ?: "")
        } else {
            val cachedState = if (msg != null) markdownState[msg.id] else null

            if (cachedState != null) {
                AlaraMarkdownText(content = cachedState)
            } else {
                val newState = rememberMarkdownState(content = messageContent)

                if (msg != null) {
                    SideEffect {
                        markdownState[msg.id] = newState
                    }
                }

                AlaraMarkdownText(content = newState)
            }
        }

        // Action buttons (only show for completed messages)
        if (!isStreaming && !isError) {
            val iconModifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(50)
                )
                .size(50.dp)
                .padding(5.dp)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 0.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlaraIconButton(
                    onClick = { /* Copy to clipboard */ },
                    modifier = iconModifier,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Rounded.CopyAll,
                    contentDescription = "Copy",
                    painter = null
                )

                AlaraIconButton(
                    onClick = { /* Thumbs up */ },
                    modifier = iconModifier,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Outlined.ThumbUpAlt,
                    contentDescription = "Like",
                    painter = null
                )

                AlaraIconButton(
                    onClick = { /* Thumbs down */ },
                    modifier = iconModifier,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Outlined.ThumbDownAlt,
                    contentDescription = "Dislike",
                    painter = null
                )
            }
        }

        // Disclaimer
        if (!isStreaming && !isError) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AlaraText(
                    text = "AlaraV2 can make mistakes, so double-check it",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showSystemUi = true)
fun AlaraChatScreenPreview() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val mockSizeClass = if (screenWidth < 600.dp) {
        WindowWidthSizeClass.Compact
    } else {
        WindowWidthSizeClass.Expanded
    }

    AlaraChatComposable(windowWidthSizeClass = mockSizeClass)
}