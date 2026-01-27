package com.ai.alarav2.ui.view.components.markdown


import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
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
    state: MarkdownState
) {


    val textColor = MaterialTheme.colorScheme.onBackground

    Markdown(
        markdownState = state,
        modifier = modifier,

        colors = markdownColor(
            text = textColor,
            dividerColor = Color.Gray.copy(alpha = 0.5f),
            codeBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),

        components = markdownComponents(
            codeFence = { fence ->
                MarkdownHighlightedCodeFence(
                    content = fence.content,
                    node = fence.node,


                )
            },
            codeBlock = {
                MarkdownHighlightedCodeFence(
                    content = it.content,
                    node = it.node,
                    style = it.typography.code,

                )
            }
        ),

        typography = markdownTypography(
            text = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 16.sp,
                color = textColor
            ),
            h1 = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 22.sp,
                color = textColor
            ),
            code = TextStyle(
                fontFamily = FontFamily.Monospace,
                fontSize = 14.sp,
                color = textColor
            )
        )
    )
}

