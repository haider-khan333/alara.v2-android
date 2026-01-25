package com.ai.alarav2.ui.view.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.outlined.ThumbDownAlt
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.KeyboardArrowDown
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.ai.alarav2.ui.theme.AlaraColors
import com.ai.alarav2.ui.view.chat.components.AlaraClickType
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.chat.components.AlaraTextBar
import com.ai.alarav2.ui.view.components.AlaraAnimatedBottomSheet
import com.ai.alarav2.ui.view.components.AlaraHeader
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.ui.view.components.StreamingFadeText
import com.ai.alarav2.ui.view.components.clickableWithOpaqueText
import com.ai.alarav2.ui.view.components.markdown.AlaraMarkdownText
import com.ai.alarav2.vm.chat.AlaraChatUiState
import com.ai.alarav2.vm.chat.AlaraChatViewModel
import customOverscroll
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
    val isScrolling by remember { derivedStateOf { listState.isScrollInProgress } }

    val isAtBottom by remember {
        derivedStateOf { !listState.canScrollForward }
    }
    val canAutoScroll by remember {
        derivedStateOf { isAtBottom && !listState.isScrollInProgress }
    }
    val lastMsgText = messages.lastOrNull()?.message.orEmpty()

    LaunchedEffect(messages.size, lastMsgText) {
        if (messages.isEmpty()) return@LaunchedEffect
        if (!canAutoScroll) return@LaunchedEffect

        // no need for awaitFrame usually
        listState.scrollToItem(messages.size - 1)
    }



    Scaffold(topBar = {
        AlaraHeader(containerColor = MaterialTheme.colorScheme.background, content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                IconButton(onClick = {

                }, Modifier.size(30.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = null
                    )
                }
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
                            .padding(
                                horizontal = 4.dp, vertical = 2.dp
                            )) {
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
        }, actions = {
            val size = Modifier.size(30.dp)
            IconButton(onClick = {}, modifier = size) {
                Icon(
                    painter = painterResource(R.drawable.ic_alara_add_profile),
                    contentDescription = null,
                    Modifier.padding(5.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(
                onClick = {

                },
                modifier = size,
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_alara_share),
                    contentDescription = null,
                    Modifier.padding(5.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = {}, modifier = size) {
                Icon(
                    painter = painterResource(R.drawable.ic_alara_settings),
                    contentDescription = null,
                    Modifier.padding(5.dp)
                )
            }

        })
    }, bottomBar = {
        AlaraTextBar(
            value = chatMessage,
            onValueChange = { chatMessage = it }, onClick = { type ->
                chatViewModel.setClickType(type)
                chatViewModel.showSheet()
            }, onSend = {
                chatViewModel.sendMessage(message = chatMessage)
                chatMessage = ""
            }, onStop = {
                // on stop the api call and show a message to user that the message has been stopped
            },
            isLoading = chatState is AlaraChatUiState.Loading
        )
    }, content = { contentPadding ->

        var animatedOverscrollAmount by remember { mutableFloatStateOf(0f) }

        Box(
            modifier = Modifier

                .customOverscroll(
                    listState, onNewOverscrollAmount = { animatedOverscrollAmount = it })
                .offset { IntOffset(0, animatedOverscrollAmount.roundToInt()) }
        )
        {

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
                    key = { it.id }
                ) { msg ->
                    if (msg.isUser) AlaraUserMessage(
                        message =
                            msg.message
                    )
                    else AlaraBotMessage(message = msg.message)

                    // Add a little space after every message
//                    Spacer(modifier = Modifier.height(16.dp))
                }
                if (chatState is AlaraChatUiState.Loading) {
                    item {
                        AlaraBotMessage(isLoading = true)
                    }

                }

                if (chatState is AlaraChatUiState.Error) {
                    item {
                        AlaraBotMessage(message = chatState.message, isLoading = false)
                    }

                }

            }
        }

        // show bottom sheet
        AlaraAnimatedBottomSheet(
            containerColor = AlaraColors.SheetColors,
            isVisible = sheetVisible, onDismissRequest = {
                chatViewModel.hideSheet()
            }) {
            val type = chatViewModel.clickType.collectAsState().value
            when (type) {
                AlaraClickType.MODELS -> {
                    // show list of Models
                    val models = chatViewModel.models.collectAsState().value
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        item {
                            AlaraText(
                                text = "Alara Models",
                                fontSize = 20.sp,
                                modifier = Modifier.padding(vertical = 10.dp, horizontal = 10.dp)
                            )

                            Divider(
                                modifier = Modifier.padding(horizontal = 10.dp),
                                color = MaterialTheme.colorScheme.onBackground
                            )

                        }
                        items(models.size) {
                            AlaraModelSelection(
                                cardColor = AlaraColors.SheetColors,

                                heading = models[it].heading,
                                subHeading = models[it].subHeading,
                                isSelected = models[it].isSelected,
                                onClick = { selectedModel ->
                                    chatViewModel.hideSheet()
                                    chatViewModel.updateSelection(it)
                                    chatViewModel.setModel(selectedModel)

                                }
                            )
                        }

                    }


                }

                AlaraClickType.ADD -> {
                    val uploadOptions = chatViewModel.uploadOptions.value
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(uploadOptions.size) {
                            AlaraFileSelection(
                                cardColor = AlaraColors.SheetColors,
                                icon = uploadOptions[it].icon,
                                text = uploadOptions[it].text,
                                onClick = { option ->


                                }
                            )
                        }
                    }
                }

                else -> {}
            }

        }
    })
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
        onClick = {
            onClick(heading)
        },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,

            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
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
        onClick = {
            onClick(text)
        },
        shape = RoundedCornerShape(0.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor,

            ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 0.dp
        )
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
        modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AlaraText(text = text, fontSize = 22.sp, fontFamily = FontFamily.Serif)
    }

}


@Composable
fun AlaraUserMessage(message: String) {
    BoxWithConstraints(

        modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd
    ) {
        val maxWidth = maxWidth * 0.75f

        Box(
            modifier = Modifier
                .widthIn(max = maxWidth)
                .background(
                    color = MaterialTheme.colorScheme.inverseOnSurface, shape = RoundedCornerShape(
                        topStart = 13.dp, topEnd = 13.dp, bottomStart = 13.dp
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
    message: String? = "",
    isLoading: Boolean = false,
    isScrolling: Boolean = false
) {
    Column(modifier = modifier) {
        // icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Box(modifier = Modifier.padding(end = 10.dp)) {

                if (isLoading) {
                    CircularProgressIndicator(
                        color = Color.DarkGray
                    )
                }
                Icon(
                    painterResource(R.mipmap.ic_launcher_foreground),
                    contentDescription = null,
                    Modifier.size(40.dp)
                )
            }

            if (!isLoading) {
                AlaraText(text = stringResource(R.string.app_name))

            } else {
                AlaraText(text = "Thinking...")
            }
        }

        Column(modifier = modifier.padding(start = 10.dp)) {
            if(isScrolling){
                AlaraText(text = message!!)
            }else{
                AlaraMarkdownText(markdown = message!!)
            }

//            AlaraText(text=message!!)
        }


        if (!isLoading) {
            val iconModifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(50),

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
                    onClick = {},
                    modifier = iconModifier,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Rounded.CopyAll,
                    contentDescription = null,
                    painter = null
                )

                AlaraIconButton(
                    onClick = {},
                    modifier = iconModifier,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Outlined.ThumbUpAlt,
                    contentDescription = null,
                    painter = null
                )


                AlaraIconButton(
                    onClick = {},
                    modifier = iconModifier,
                    iconTint = MaterialTheme.colorScheme.onBackground,
                    imageVector = Icons.Outlined.ThumbDownAlt,
                    contentDescription = null,
                    painter = null
                )
            }

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AlaraText(text = "AlaraV2 can make mistakes, so double-check it", fontSize = 12.sp)

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

    // Simple logic to mock the size class for Preview purposes
    val mockSizeClass = if (screenWidth < 600.dp) {
        WindowWidthSizeClass.Compact
    } else {
        WindowWidthSizeClass.Expanded
    }
    Column(Modifier.padding(top = 10.dp)) {
        AlaraBotMessage(
            message = "Hellow tere how are you", isLoading = true
        )
    }
//    AlaraChatComposable(windowWidthSizeClass = mockSizeClass)
//    AlaraModelSelection(
//        modifier = Modifier.padding(top = 20.dp),
//        heading = "AutoGPT",
//        subHeading = "Autonomous agent that breaks goals into sub-tasks using GPT-4.",
//        isSelected = true
//    )
}