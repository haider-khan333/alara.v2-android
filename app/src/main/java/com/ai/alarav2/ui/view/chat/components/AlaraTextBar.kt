package com.ai.alarav2.ui.view.chat.components

import android.graphics.Paint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.alarav2.ui.view.components.AlaraText

enum class AlaraClickType {
    ADD,
    CONNECTIONS,
    MIC,
    MODELS,
    SETTINGS,
    SHARE
}


@Composable
fun AlaraTextBar(
    modifier: Modifier = Modifier,
    value: String = "hellow world",
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onStop: () -> Unit = {},
    isLoading: Boolean = false,
    onClick: (AlaraClickType) -> Unit
) {
    val inputBackgroundColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
    val contentColor = MaterialTheme.colorScheme.onSurface

    val isTyping = value.isNotEmpty()
    val sendButtonColor = if (isTyping) contentColor else Color.Gray.copy(alpha = 0.2f)
    val sendIconColor = if (isTyping) MaterialTheme.colorScheme.inverseOnSurface else Color.White


    Surface(
        modifier = modifier
            .fillMaxWidth()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .imePadding()
            .padding(vertical = 10.dp),
        color = Color.Transparent
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp)
                .fillMaxWidth()
                .height(intrinsicSize = IntrinsicSize.Min),
            // 2. This keeps icons at the bottom when text grows to multiple lines
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {

            // Add Button
            Box(Modifier.fillMaxHeight()) {
                AlaraIconButton(
                    onClick = { onClick(AlaraClickType.ADD) },
                    imageVector = Icons.Rounded.Add,
                    contentDescription = "Add",
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(
                            color = MaterialTheme.colorScheme.surfaceVariant,
                            shape = CircleShape
                        )
                        .size(45.dp), // Space between icon and its circle background
                    iconTint = contentColor,
                    painter = null,
                )
            }
            Spacer(modifier = Modifier.width(4.dp))

            // Input Capsule
            Row(
                modifier = Modifier
                    .background(
                        color = inputBackgroundColor,
                        shape = RoundedCornerShape(22.dp)
                    )
                    .padding(vertical = 6.dp)
                    .padding(start = 10.dp, end = 5.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(end = 8.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        AlaraText(
                            text = "Ask Alara any thing...",
                            color = contentColor.copy(alpha = 0.5f),
                            fontSize = 16.sp
                        )
                    }

                    BasicTextField(
//                        value = "value\najalklnlnd\nihdd\naisudhad\naisudhasd",
                        value = value,
                        onValueChange = onValueChange,
                        maxLines = 5,
                        textStyle = TextStyle(
                            fontSize = 16.sp,
                            color = contentColor,
                            fontFamily = MaterialTheme.typography.bodyLarge.fontFamily
                        ),
                        cursorBrush = SolidColor(contentColor),
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                // Send Button
                Box(Modifier.fillMaxHeight()) {
                    AlaraIconButton(
                        enabled = isTyping,
                        onClick = { if (isTyping) onSend() },
                        imageVector = Icons.Rounded.ArrowUpward,
                        contentDescription = "Send",
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .background(
                                color = sendButtonColor,
                                shape = CircleShape
                            )
                            .size(35.dp),
                        // 3. Prevents icon from touching capsule edges
                        iconTint = sendIconColor,
                        painter = null
                    )
                }
            }
        }
    }
}
//@Composable
//fun AlaraTextBar(
//    modifier: Modifier = Modifier, value: String,
//    onValueChange: (String) -> Unit,
//    onSend: () -> Unit,
//    onStop: () -> Unit = {},
//    isLoading: Boolean = false,
//    onClick: (AlaraClickType) -> Unit
//) {
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        horizontalArrangement = Arrangement.Center
//    ) {
//
//
//        Card(
//            modifier = modifier
//                .fillMaxWidth()
//                .windowInsetsPadding(WindowInsets.navigationBars)
//                .imePadding()
//                .padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
//            shape = RoundedCornerShape(0.dp),
//            colors = CardDefaults.cardColors(
//                containerColor = Color.Transparent
//            )
//        ) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                AlaraIconButton(
//                    onClick = {
//
//                    },
//                    imageVector = Icons.Rounded.Add,
//                    contentDescription = null,
//                    painter = null,
//                    modifier = Modifier
//                        .background(
//                            color = Color.Red,
//                            shape = RoundedCornerShape(50),
//                        ).size(40.dp),
//                    iconTint = AlaraDarkGray
//                )
//                Spacer(modifier = Modifier.width(5.dp))
//                TextField(
//                    placeholder = {
//                        AlaraText(
//                            text = "Ask Alara any thing",
//                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
//                        )
//                    },
//                    maxLines = 1,
//                    minLines = 1,
//                    value = value,
//                    onValueChange = {
//                        onValueChange(it)
//                    },
//                    modifier = modifier
//                        .fillMaxWidth()
//                        .background(
//                            color = Color.Red,
//                            shape = RoundedCornerShape(24.dp)
//                        ).height(48.dp).padding(0.dp),
//                    colors = TextFieldDefaults.colors(
//                        focusedContainerColor = Color.Transparent,
//                        unfocusedContainerColor = Color.Transparent,
//                        disabledContainerColor = Color.Transparent,
//                        focusedIndicatorColor = Color.Transparent,
//                        unfocusedIndicatorColor = Color.Transparent,
//                        cursorColor = MaterialTheme.colorScheme.onBackground,
//                        focusedTextColor = MaterialTheme.colorScheme.onBackground,
//                        unfocusedTextColor = MaterialTheme.colorScheme.onBackground
//                    ),
//                )
//            }
////            Row(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .padding(start = 10.dp, end = 10.dp, bottom = 10.dp)
////            ) {
////
////
////                val size = 35.dp
////                val imgModifier = Modifier
////                    .background(
////                        color = AlaraWhite,
////                        shape = RoundedCornerShape(50),
////                    )
////                    .size(size)
////                AlaraIconButton(
////                    onClick = {
////                        onClick(AlaraClickType.ADD)
////                    },
////                    imageVector = Icons.Rounded.Add,
////                    contentDescription = null,
////                    painter = null,
////                    modifier = imgModifier,
////                    iconTint = AlaraDarkGray
////                )
////                Spacer(modifier = Modifier.width(10.dp))
////
////
////                AlaraIconButton(
////                    onClick = {
////                        onClick(AlaraClickType.CONNECTIONS)
////                    },
////                    imageVector = Icons.Rounded.PrivateConnectivity,
////                    contentDescription = null,
////                    painter = null,
////                    modifier = imgModifier,
////                    iconTint = AlaraDarkGray
////
////
////                )
////
////                Spacer(modifier = Modifier.weight(1f))
////
////                if (value.isEmpty()) {
////
////                    AlaraIconButton(
////                        onClick = {
////                            onClick(AlaraClickType.MIC)
////                        },
////                        imageVector = Icons.Rounded.MicNone,
////                        contentDescription = null,
////                        painter = null,
////                        modifier = imgModifier,
////                        iconTint = AlaraDarkGray
////                    )
////                }
////
////                Spacer(modifier = Modifier.width(10.dp))
////
////                if (isLoading) {
////                    AlaraIconButton(
////                        onClick = {
////                            onStop()
////                        },
////                        imageVector = null,
////                        contentDescription = null,
////                        painter = painterResource(R.drawable.ic_stop),
////                        modifier = imgModifier,
////                        iconModifier = Modifier.padding(6.dp),
////                        colors = IconButtonDefaults.iconButtonColors(
////                            containerColor = Color.Black,
////                            disabledContainerColor = AlaraDarkGray.copy(alpha = 0.5f),
////                        ),
////                        iconTint = Color.White
////
////
////                    )
////
////                } else {
////                    AlaraIconButton(
////                        enabled = !value.isEmpty(),
////                        onClick = {
////                            onSend()
////                        },
////                        imageVector = Icons.Rounded.ArrowUpward,
////                        contentDescription = null,
////                        painter = null,
////                        modifier = imgModifier,
////                        colors = IconButtonDefaults.iconButtonColors(
////                            containerColor = Color.Black,
////                            disabledContainerColor = AlaraDarkGray.copy(alpha = 0.5f),
////                        ),
////                        iconTint = Color.White
////
////
////                    )
////
////                }
////
////
////            }
//
//        }
//    }
//}

@Composable
@Preview(showSystemUi = true)

fun PreviewComp() {
    Box(modifier = Modifier.fillMaxSize()) {
        AlaraTextBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            value = "",
            onValueChange = {

            },
            onSend = {}
        ) { }
    }
}
