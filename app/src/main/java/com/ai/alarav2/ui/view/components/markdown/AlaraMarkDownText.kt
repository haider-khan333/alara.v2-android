package com.ai.alarav2.ui.view.components.markdown


import AlaraCodeFence
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import com.mikepenz.markdown.compose.components.markdownComponents
import com.mikepenz.markdown.m3.Markdown
import com.mikepenz.markdown.m3.markdownColor
import com.mikepenz.markdown.m3.markdownTypography

@Composable
fun AlaraMarkdownText(
    modifier: Modifier = Modifier,
    markdown: String
) {


    val textColor = MaterialTheme.colorScheme.onBackground

    Markdown(
        content = markdown,
        modifier = modifier,

        colors = markdownColor(
            text = textColor,
            dividerColor = Color.Gray.copy(alpha = 0.5f),
            codeBackground = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
        ),

        components = markdownComponents(
            codeFence = {


                AlaraCodeFence(
                    content = markdown,
                    node = it.node,
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

