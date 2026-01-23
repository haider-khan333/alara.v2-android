package com.ai.alarav2.ui.view.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlaraHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
    actions: @Composable () -> Unit
) {
    TopAppBar(
        title = { content() },
        modifier = modifier,
        actions = { actions() }
    )
}