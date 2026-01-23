package com.ai.alarav2.ui.view.dashboard

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddComment
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.alarav2.R
import com.ai.alarav2.ui.theme.AlaraChipGrayBorder
import com.ai.alarav2.ui.theme.AlaraRating
import com.ai.alarav2.ui.view.chat.AlaraBotMessage
import com.ai.alarav2.ui.view.chat.AlaraUserMessage
import com.ai.alarav2.ui.view.chat.components.AlaraIconButton
import com.ai.alarav2.ui.view.chat.customOverscroll
import com.ai.alarav2.ui.view.components.AlaraHeader
import com.ai.alarav2.ui.view.components.AlaraText
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


@Composable
fun AlaraDashboardComposable(
    windowWidthSizeClass: WindowWidthSizeClass, onClick: () -> Unit
) {
    val icBtnModifier = Modifier.size(45.dp)
    val icModifier = Modifier.padding(10.dp)

    val filters = listOf("All", "Favourites", "Scheduled")
    var selectedFilterIndex by remember { mutableIntStateOf(0) }

    Scaffold(containerColor = MaterialTheme.colorScheme.background, topBar = {

        Surface(color = MaterialTheme.colorScheme.background, tonalElevation = 0.dp) {
            Column {
                AlaraHeader(containerColor = Color.Transparent, isCentered = true, content = {
                    AlaraText(text = stringResource(R.string.app_name))
                }, navigationIcon = {
                    AlaraIconButton(
                        onClick = { /*TODO*/ },
                        iconModifier = icModifier,
                        modifier = icBtnModifier,
                        imageVector = null,
                        contentDescription = "Profile",
                        iconTint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(R.drawable.ic_profile)
                    )
                }, modifier = Modifier, actions = {
                    AlaraIconButton(
                        onClick = { /*TODO*/ },
                        imageVector = null,
                        modifier = icBtnModifier,
                        iconModifier = icModifier,
                        contentDescription = "Profile",
                        iconTint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(R.drawable.ic_notification)
                    )
                    AlaraIconButton(
                        onClick = { /*TODO*/ },
                        imageVector = null,
                        modifier = icBtnModifier,
                        iconModifier = icModifier,
                        contentDescription = "Profile",
                        iconTint = MaterialTheme.colorScheme.onBackground,
                        painter = painterResource(R.drawable.ic_search)
                    )
                })
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    filters.forEachIndexed { index, filterName ->
                        AlaraChips(
                            text = filterName,
                            isSelected = selectedFilterIndex == index,
                            onSelected = {
                                selectedFilterIndex = index
                            })
                    }
                }
            }
        }

    }, content = { contentPadding ->
        val listState = rememberLazyListState()
        var animatedOverscrollAmount by remember { mutableFloatStateOf(0f) }



        Box(
            modifier = Modifier
                .fillMaxSize()
                .customOverscroll(
                    listState, onNewOverscrollAmount = { animatedOverscrollAmount = it })
                .offset { IntOffset(0, animatedOverscrollAmount.roundToInt()) }) {

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(contentPadding)
            ) {
                item {
                    AlaraMessage()
                    AlaraMessage()
                }
            }

        }
    }, bottomBar = {

    }, floatingActionButton = {
        FloatingActionButton(
            onClick = {
                onClick()
            },
            containerColor = MaterialTheme.colorScheme.onSurface,
            shape = RoundedCornerShape(50)
        ) {
            Icon(
                imageVector = Icons.Default.AddComment,
                contentDescription = "Action",
                Modifier.size(25.dp),
                tint = MaterialTheme.colorScheme.surface

            )
        }

    })
}


@Composable
fun AlaraMessage(modifier: Modifier = Modifier) {

    val imgBgColor = if (isSystemInDarkTheme()) {
        AlaraChipGrayBorder.copy(alpha = 0.1f)
    } else {
        AlaraChipGrayBorder
    }


    val subTextBgColor = if (isSystemInDarkTheme()) {
        AlaraChipGrayBorder
    } else {
        Color.Black.copy(alpha = 0.5f)
    }
    Card(
        onClick = {

        }, colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ), modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp), shape = RoundedCornerShape(0)
    ) {
        // Image
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
        ) {
            Box(
                modifier = Modifier
                    .background(
                        color = imgBgColor, shape = RoundedCornerShape(50)
                    )
                    .size(40.dp)
            ) {
                Icon(
                    painterResource(R.drawable.ic_chat),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(10.dp)

                )
            }

            Spacer(modifier = Modifier.width(10.dp))
            Column() {
                val today = LocalDateTime.now()
                val day = today.format(DateTimeFormatter.ofPattern("EEE"))


                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    AlaraText(
                        text = "Good bye message Good bye message Good bye message Good bye message",
                        maxLines = 1,
                        modifier = Modifier.fillMaxWidth(0.6f),
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        imageVector = Icons.Rounded.Star,
                        contentDescription = null,
                        tint = AlaraRating
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    AlaraText(text = day.toString(), color = subTextBgColor)

                }
                Spacer(modifier = Modifier.height(5.dp))
                AlaraText(
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    text = "Hellow there how can i help you today? I am here to your needs. let me know how can i help you today",
                    color = subTextBgColor,
                    fontSize = 14.sp

                )
            }
        }

    }

}

@Composable
fun AlaraChips(
    text: String, isSelected: Boolean = false, onSelected: () -> Unit = {}
) {


    val chipBorderColor = if (isSystemInDarkTheme()) {
        AlaraChipGrayBorder.copy(alpha = 0.4f)
    } else {
        AlaraChipGrayBorder
    }

    val chipBgColor = if (isSelected && isSystemInDarkTheme()) {
        Color.White
    } else if (isSelected && !isSystemInDarkTheme()) {
        Color.Black
    } else if (!isSelected && isSystemInDarkTheme()) {
        Color.Black
    } else {
        Color.White
    }

    val chipTextColor = if (isSelected && isSystemInDarkTheme()) {
        Color.Black
    } else if (isSelected && !isSystemInDarkTheme()) {
        Color.White
    } else if (!isSelected && isSystemInDarkTheme()) {
        Color.White
    } else {
        Color.Black
    }


    FilterChip(
        modifier = Modifier.padding(vertical = 0.dp, horizontal = 5.dp),
        shape = RoundedCornerShape(50),
        onClick = { onSelected() },
        label = {
            AlaraText(text = text, color = chipTextColor)
        },
        selected = isSelected,
        border = BorderStroke(1.dp, chipBorderColor),
        colors = FilterChipDefaults.filterChipColors(
            containerColor = chipBgColor, selectedContainerColor = chipBgColor
        )
    )
}

@Composable
@Preview(showSystemUi = true)
fun AlaraDashboardPreview() {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    // Simple logic to mock the size class for Preview purposes
    val mockSizeClass = if (screenWidth < 600.dp) {
        WindowWidthSizeClass.Compact
    } else {
        WindowWidthSizeClass.Expanded
    }
    AlaraDashboardComposable(
        windowWidthSizeClass = mockSizeClass, onClick = {

        })
}