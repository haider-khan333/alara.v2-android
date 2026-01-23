package com.ai.alarav2.ui.view.chat

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.CubicBezierEasing
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.outlined.ThumbDownAlt
import androidx.compose.material.icons.outlined.ThumbUpAlt
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.MicNone
import androidx.compose.material.icons.rounded.PrivateConnectivity
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.Velocity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.alarav2.R
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.components.AlaraHeader
import com.ai.alarav2.ui.view.components.AlaraText
import com.ai.alarav2.ui.view.components.clickableWithOpaqueText
import kotlinx.coroutines.launch
import kotlin.math.log
import kotlin.math.roundToInt
import kotlin.math.sign

@Composable
fun AlaraChatComposable(windowWidthSizeClass: WindowWidthSizeClass) {
    var chatMessage by remember { mutableStateOf("") }
    Scaffold(topBar = {
        AlaraHeader(content = {
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
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.background)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickableWithOpaqueText {
                            }
                            .padding(
                                horizontal = 4.dp,
                                vertical = 2.dp
                            )
                    ) {
                        AlaraText(
                            text = "Front End Maker",
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
            onValueChange = { chatMessage = it },
            onClick = {},
            onSend = {})
    }, content = { contentPadding ->
        val listState = rememberLazyListState()
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
                modifier = Modifier
                    .fillMaxSize()

                    .padding(contentPadding)
                    .padding(5.dp)
            ) {
                item {
                    AlaraUserMessage(message = "Hello there how are you. i am fine a n how are you?")
                    AlaraBotMessage(message = "I ma great and i am alara bot and i am here assist you with any type of your needds")
                }
            }
        }
    })
}

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
                return Offset.Zero
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


enum class AlaraClickType {
    ADD,
    CONNECTIONS,
    MIC
}

@Composable
fun AlaraTextBar(
    modifier: Modifier = Modifier, value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onClick: (AlaraClickType) -> Unit
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .imePadding()
            .padding(bottom = 10.dp, start = 10.dp, end = 10.dp),
        shape = RoundedCornerShape(24.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            TextField(
                placeholder = {
                    AlaraText(
                        text = "Ask Alara any thing",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                },
                maxLines = 5,
                value = value,
                onValueChange = {
                    onValueChange(it)
                },
                modifier = modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    cursorColor = MaterialTheme.colorScheme.onBackground,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
            ) {

                val size = 35.dp
                val imgModifier = Modifier
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(50),
                    )
                    .size(size)
                AlaraIconButton(
                    onClick = {
                        onClick(AlaraClickType.ADD)
                    },
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    painter = null,
                    modifier = imgModifier
                )
                Spacer(modifier = Modifier.width(10.dp))


                AlaraIconButton(
                    onClick = {
                        onClick(AlaraClickType.CONNECTIONS)
                    },
                    imageVector = Icons.Rounded.PrivateConnectivity,
                    contentDescription = null,
                    painter = null,
                    modifier = imgModifier
                )

                Spacer(modifier = Modifier.weight(1f))

                if (value.isEmpty()) {

                    AlaraIconButton(
                        onClick = {
                            onClick(AlaraClickType.MIC)
                        },
                        imageVector = Icons.Rounded.MicNone,
                        contentDescription = null,
                        painter = null,
                        modifier = imgModifier,
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                AlaraIconButton(
                    enabled = !value.isEmpty(),
                    onClick = {
                        onSend()
                    },
                    imageVector = Icons.Rounded.ArrowUpward,
                    contentDescription = null,
                    painter = null,
                    modifier = imgModifier,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = MaterialTheme.colorScheme.onBackground,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceDim

                    )
                )
            }
        }
    }
}


@Composable
fun AlaraUserMessage(message: String) {
    BoxWithConstraints(

        modifier = Modifier
            .fillMaxWidth(),
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
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.8f)
            )
        }

    }

}

@Composable
fun AlaraBotMessage(
    modifier: Modifier = Modifier,
    message: String
) {
    Column(modifier = modifier) {
        // icon
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painterResource(R.mipmap.ic_launcher_foreground),
                contentDescription = null,
                Modifier.size(40.dp)
            )
            AlaraText(text = stringResource(R.string.app_name))
        }

        Column(modifier = modifier.padding(start = 10.dp)) {
            AlaraText(text = message)
        }

        val iconModifier = Modifier
            .background(
                color =
                    MaterialTheme.colorScheme.background,
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
    AlaraChatComposable(windowWidthSizeClass = mockSizeClass)
}