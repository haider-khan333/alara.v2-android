package com.ai.alarav2.ui.view.chat

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ThumbDownAlt
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ai.alarav2.R
import com.ai.alarav2.data.models.ui.AlaraChatUiModels
import com.ai.alarav2.ui.theme.AlaraColors
import com.ai.alarav2.ui.view.chat.components.AlaraClickType
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.chat.components.AlaraTextBar
import com.ai.alarav2.ui.view.components.AlaraAnimatedBottomSheet
import com.ai.alarav2.ui.view.components.AlaraHeader
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.ui.view.components.markdown.AlaraMarkdownText
import com.ai.alarav2.vm.chat.AlaraChatUiState
import com.ai.alarav2.vm.chat.AlaraChatViewModel
import com.ai.alarav2.vm.chat.AlaraGetAgentState
import com.ai.alarav2.vm.drawer.AlaraDrawerVm
import com.mikepenz.markdown.model.MarkdownState
import com.mikepenz.markdown.model.rememberMarkdownState
import customOverscroll
import kotlin.math.roundToInt


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlaraChatComposable(
    windowWidthSizeClass: WindowWidthSizeClass,
    drawerVm: AlaraDrawerVm
) {
    var chatMessage by remember { mutableStateOf("") }
    val listState = rememberLazyListState()
    val chatViewModel: AlaraChatViewModel = hiltViewModel()
    val messages = chatViewModel.messages.collectAsState().value
    val sheetVisible = chatViewModel.showSheet.collectAsState().value
    val selectedModel = chatViewModel.selectedModel.collectAsState().value
    val chatState = chatViewModel.chatState.collectAsState().value
    val isStreaming = chatState is AlaraChatUiState.Streaming
    val lastBotId = messages.lastOrNull { !it.isUser }?.id
    val agentState = chatViewModel.agentState.collectAsState().value
    val markdownCache = remember { mutableStateMapOf<String, MarkdownState>() }

    LaunchedEffect(messages.size) {
        listState.animateScrollToItem(messages.size)
    }

    LaunchedEffect(agentState) {
        when (agentState) {
            is AlaraGetAgentState.Loading -> {
                // show loading in agent
            }

            is AlaraGetAgentState.Success -> {

            }

            is AlaraGetAgentState.Error -> {

            }

            else -> {
                //Ignored
            }

        }
    }

    Scaffold(
        containerColor = AlaraColors.Background,
        topBar = {
            AlaraHeader(
                containerColor = AlaraColors.Background,
                content = {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = AlaraColors.Chip,
                        ),
                        onClick = {
                            drawerVm.openDrawer()
                        }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center,
                            modifier = Modifier.padding(
                                top = 7.dp,
                                end = 15.dp,
                                start = 15.dp,
                                bottom = 7.dp
                            )

                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Menu,
                                contentDescription = null,
                                modifier = Modifier.size(25.dp)
                            )

                            Spacer(modifier = Modifier.width(10.dp))
                            AlaraText(
                                text = stringResource(R.string.app_name),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        }
                    }
                },
                actions = {
                    AlaraAgentChip(
                        selectedModel = selectedModel,
                        isLoading = agentState is AlaraGetAgentState.Loading,
                        onClick = {
                            chatViewModel.setClickType(AlaraClickType.MODELS)
                            chatViewModel.showSheet()
                        }
                    )
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
                        .padding(start = 5.dp, bottom = 5.dp, top = 10.dp, end = 5.dp)
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
                            AlaraUserMessage(
                                message = msg.message
                            )
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

                    if (chatState is AlaraChatUiState.Error) {
                        item {
                            AlaraBotMessage(
                                isError = true,
                                errorMessage = chatState.message

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
                            uploadOptions.take(3)
                                .forEach { option -> // Take first 3 to fit evenly
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
                            heading = models[index].agentName,
                            subHeading = "",
                            isSelected = models[index].isSelected,
                            onClick = { _ ->
                                chatViewModel.updateSelection(index)
                                // Optional: Close sheet on model selection
                                chatViewModel.hideSheet()
                            }
                        )
                    }
                }
            }

        }
    )
}


@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun AlaraAgentChip(
    selectedModel: String,
    isLoading: Boolean,
    onClick: () -> Unit = {}
) {
    Card(
        enabled = !isLoading,
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(
            containerColor = AlaraColors.Chip,
        ),
        onClick = onClick
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(
                top = 10.dp,
                end = 15.dp,
                start = 15.dp,
                bottom = 10.dp
            )
        ) {
            if (!isLoading) {
                AlaraText(
                    text = selectedModel,
                    fontSize = 16.sp,
                )
            } else {
                LoadingIndicator(modifier = Modifier.size(20.dp))
            }
        }
    }
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
    errorMessage: String = "",
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
                        painterResource(R.drawable.logo_app_black),
                        contentDescription = null,
                        Modifier
                            .size(35.dp)
                            .padding(start = 10.dp, end = 10.dp, bottom = 3.dp),
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }


            AlaraText(
                text = stringResource(R.string.app_name),
            )
        }

        if (isError) {
            AlaraText(
                text = errorMessage,
                maxLines = 5,
                softWrap = true,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
        }
        val messageContent = msg?.message ?: ""
        if (isStreaming) {
            AlaraText(
                text = msg?.message ?: "",
                softWrap = true,
                modifier = Modifier.padding(horizontal = 10.dp)
            )
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
        if (!isStreaming && !isError && !isLoading) {
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
        if (!isStreaming && !isError && !isLoading) {
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

//    AlaraChatComposable(
//        windowWidthSizeClass = mockSizeClass,
//    )
}