package com.ai.alarav2.ui.view.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Square
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.MicNone
import androidx.compose.material.icons.rounded.PrivateConnectivity
import androidx.compose.material3.Card
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ai.alarav2.R
import com.ai.alarav2.ui.theme.AlaraDarkGray
import com.ai.alarav2.ui.theme.AlaraWhite
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
    modifier: Modifier = Modifier, value: String,
    onValueChange: (String) -> Unit,
    onSend: () -> Unit,
    onStop: () -> Unit = {},
    isLoading: Boolean = false,
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
                        color = AlaraWhite,
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
                    modifier = imgModifier,
                    iconTint = AlaraDarkGray
                )
                Spacer(modifier = Modifier.width(10.dp))


                AlaraIconButton(
                    onClick = {
                        onClick(AlaraClickType.CONNECTIONS)
                    },
                    imageVector = Icons.Rounded.PrivateConnectivity,
                    contentDescription = null,
                    painter = null,
                    modifier = imgModifier,
                    iconTint = AlaraDarkGray


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
                        iconTint = AlaraDarkGray
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                if (isLoading) {
                    AlaraIconButton(
                        onClick = {
                            onStop()
                        },
                        imageVector = null,
                        contentDescription = null,
                        painter = painterResource(R.drawable.ic_stop),
                        modifier = imgModifier,
                        iconModifier = Modifier.padding(6.dp),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.Black,
                            disabledContainerColor = AlaraDarkGray.copy(alpha = 0.5f),
                        ),
                        iconTint = Color.White


                    )

                } else {
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
                            containerColor = Color.Black,
                            disabledContainerColor = AlaraDarkGray.copy(alpha = 0.5f),
                        ),
                        iconTint = Color.White


                    )

                }


            }
        }
    }
}
