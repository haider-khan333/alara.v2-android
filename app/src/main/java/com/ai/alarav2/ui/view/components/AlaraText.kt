package com.ai.alarav2.ui.view.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp


@Composable
fun AlaraText(
    clickable: Boolean = false,
    modifier: Modifier = Modifier,
    text: String,
    color: Color = MaterialTheme.colorScheme.onBackground,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
    fontWeight: FontWeight = FontWeight.Normal,
    maxLines: Int = Int.MAX_VALUE,
    overflow: TextOverflow = TextOverflow.Clip,
    lineHeight: TextUnit = TextUnit.Unspecified,
    letterSpacing: TextUnit = TextUnit.Unspecified,
    fontSize: TextUnit = TextUnit.Unspecified,
    textAlign: TextAlign = TextAlign.Start,
    fontFamily: FontFamily = FontFamily.Default

) {

    Text(
        modifier = if (clickable) modifier.clickableWithOpaqueText {} else modifier,
        text = text,
        color = color,
        style = style,
        fontWeight = fontWeight,
        maxLines = maxLines,
        overflow = overflow,
        lineHeight = lineHeight,
        letterSpacing = letterSpacing,
        fontSize = fontSize,
        textAlign = textAlign,
        fontFamily = fontFamily
    )
}

fun Modifier.clickableWithOpaqueText(
    onClick: () -> Unit
): Modifier {
    return this.composed {
        this.clickable(
            enabled = true,
            onClickLabel = null,
            role = null,
            onClick = onClick,
            interactionSource = remember { MutableInteractionSource() },
            indication = customRipple(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                radius = 1000000.dp,
                bounded = true,
            )
        )
    }
}