package com.ai.alarav2.ui.view.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlaraHeader(
    containerColor: Color = Color.Transparent,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    actions: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    isCentered: Boolean = false
) {
    if (isCentered) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                scrolledContainerColor = containerColor
            ),
            title = { content() },
            modifier = modifier,
            actions = { actions() },
            navigationIcon = { navigationIcon() }
        )
    } else {
        TopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = containerColor,
                scrolledContainerColor = containerColor
            ),
            title = { content() },
            modifier = modifier,
            actions = { actions() }
        )
    }

}