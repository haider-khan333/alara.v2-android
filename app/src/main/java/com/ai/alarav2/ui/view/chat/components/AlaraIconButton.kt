package com.ai.alarav2.ui.view.chat.components

import android.R
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

@Composable
fun AlaraIconButton(
    modifier: Modifier = Modifier,
    iconModifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconTint: Color = MaterialTheme.colorScheme.surfaceVariant,
    onClick: () -> Unit,
    imageVector: ImageVector?,
    contentDescription: String?,
    painter: Painter?,
    colors: IconButtonColors? = null,
) {
    val brandIconColor = if (isSystemInDarkTheme()) {
        Color(0xFFFFB74D) // Lighter orange for dark backgrounds
    } else {
        Color(0xFFE65100) // Darker orange for light backgrounds
    }

    IconButton(
        enabled = enabled,
        onClick = onClick,
        modifier = modifier,
        colors = colors ?: IconButtonDefaults.iconButtonColors()
    ) {
        if (imageVector != null) {
            Icon(
                modifier = iconModifier,
                imageVector = imageVector,
                contentDescription = contentDescription,
                tint = iconTint
            )
        }
        if (painter != null) {
            Icon(
                painter = painter,
                modifier = iconModifier,
                contentDescription = contentDescription,
                tint = iconTint
            )
        }
    }
}