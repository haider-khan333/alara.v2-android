package com.ai.alarav2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

object AlaraColors {
    val TextSecondary: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            AlaraChipGrayBorder
        else
            Color.Black.copy(alpha = 0.5f)


    val TextPrimary: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            Color.LightGray
        else
            Color.DarkGray

    val ImgBg: Color
        @Composable
        get() = if (isSystemInDarkTheme()) {
            AlaraChipGrayBorder.copy(alpha = 0.1f)
        } else {
            AlaraChipGrayBorder
        }

    val SheetColors: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            Color.DarkGray
        else
            MaterialTheme.colorScheme.background
}