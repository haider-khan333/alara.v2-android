package com.ai.alarav2.ui.view.components.markdown


import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.compose.elements.MarkdownHighlightedCodeFence
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography
import com.mikepenz.markdown.model.MarkdownState

@Composable
fun AlaraMarkdownText(
    modifier: Modifier = Modifier,
    content: MarkdownState,
) {
    val textColor = MaterialTheme.colorScheme.onBackground
    val codeBg = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)

    // These call internal library memoization
    val alaraTypography = markdownTypography(
        text = TextStyle(fontFamily = FontFamily.Serif, fontSize = 16.sp, color = textColor),
        h1 = TextStyle(fontFamily = FontFamily.Serif, fontSize = 22.sp, color = textColor),
        code = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 14.sp, color = textColor)
    )

    val alaraColors = markdownColor(
        text = textColor,
        dividerColor = Color.Gray.copy(alpha = 0.5f),
        codeBackground = codeBg
    )



    Markdown(
        markdownState = content,
        modifier = modifier,
//        loading = { MarkdownLoadingPlaceholder() },
        colors = alaraColors,
        components = markdownComponents(
            codeFence = {
                MarkdownHighlightedCodeFence(
                    content = it.content,
                    node = it.node,
                    style = alaraTypography.code,
                    showHeader = true
                )
            },
            codeBlock = {
                MarkdownHighlightedCodeFence(
                    content = it.content,
                    node = it.node,
                    style = alaraTypography.code,
                    showHeader = true
                )
            }
        ),
        typography = alaraTypography
    )
}

@Composable
fun MarkdownLoadingPlaceholder() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
    ) {
        // Line 1: Long
        Box(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
        Spacer(modifier = Modifier.height(10.dp))
        // Line 2: Medium
        Box(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .height(14.dp)
                .clip(RoundedCornerShape(4.dp))
                .shimmer()
                .background(Color.LightGray.copy(alpha = 0.2f))
        )
    }
}

@Composable
fun Modifier.shimmer(): Modifier {
    val transition = rememberInfiniteTransition(label = "shimmer")
    val translateAnim by transition.animateFloat(
        initialValue = -1000f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "shimmerTranslation"
    )

    return this.drawWithCache {
        val brush = Brush.linearGradient(
            colors = listOf(
                Color.LightGray.copy(alpha = 0.3f),
                Color.White.copy(alpha = 0.5f),
                Color.LightGray.copy(alpha = 0.3f),
            ),
            start = Offset(translateAnim, 0f),
            end = Offset(translateAnim + size.width, size.height)
        )
        onDrawWithContent {
            drawRect(brush = brush)
        }
    }
}

