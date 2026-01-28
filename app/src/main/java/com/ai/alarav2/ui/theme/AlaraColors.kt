package com.ai.alarav2.ui.theme

import android.text.style.BackgroundColorSpan
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import com.ai.alarav2.R

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

    val Background: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            AlaraBackgroundDark
        else
            AlaraBackgroundLight

    val MainContent: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            AlaraContentDark
        else
            AlaraContentLight

    val Chip: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            AlaraChipDark
        else
            AlaraChipLight


    val Input: Color
        @Composable
        get() = if (isSystemInDarkTheme())
            AlaraInputDark
        else
            AlaraInputLight

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

    val AlaraAppIcon: Painter
        @Composable
        get() = painterResource(R.drawable.logo_app_black)


}