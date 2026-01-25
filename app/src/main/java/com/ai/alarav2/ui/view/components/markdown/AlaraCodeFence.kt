import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisallowComposableCalls
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ai.alarav2.ui.view.components.AlaraText
import com.mikepenz.markdown.compose.LocalMarkdownColors
import com.mikepenz.markdown.compose.LocalMarkdownDimens
import com.mikepenz.markdown.compose.LocalMarkdownPadding
import com.mikepenz.markdown.compose.LocalMarkdownTypography
import com.mikepenz.markdown.compose.elements.MarkdownCodeBackground
import com.mikepenz.markdown.compose.elements.MarkdownCodeFence
import com.mikepenz.markdown.compose.elements.material.MarkdownBasicText
import dev.snipme.highlights.Highlights
import dev.snipme.highlights.model.BoldHighlight
import dev.snipme.highlights.model.ColorHighlight
import dev.snipme.highlights.model.SyntaxLanguage
import kotlinx.coroutines.delay
import org.intellij.markdown.ast.ASTNode

@Composable
fun AlaraCodeFence(
    content: String,
    node: ASTNode,
    modifier: Modifier = Modifier,
    language: String? = null,
) {
    val clipboard = LocalClipboardManager.current
    var copied by remember { mutableStateOf(false) }

    val codeText = remember(content) { content.trimEnd() }



    LaunchedEffect(copied) {
        if (copied) {
            delay(1200)
            copied = false
        }
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        tonalElevation = 0.dp,
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AlaraText(
                    text = language ?: "Code",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                TextButton(
                    onClick = {
                        clipboard.setText(AnnotatedString(codeText))
                        copied = true
                    },
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 2.dp),
                    modifier = Modifier.height(30.dp)
                ) {
                    Icon(
                        imageVector = if (copied) Icons.Filled.Check else Icons.Filled.ContentCopy,
                        contentDescription = if (copied) "Copied" else "Copy",
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(6.dp))
                    AlaraText(text = if (copied) "Copied" else "Copy", fontSize = 12.sp)
                }
            }
            MarkdownHighlightedCodeFence(content = content, node = node)
        }
    }
}


@Composable
fun MarkdownHighlightedCodeFence(
    content: String,
    node: ASTNode,
    highlights: Highlights.Builder = Highlights.Builder()
) {
    MarkdownCodeFence(content, node) { code, language ->
        MarkdownHighlightedCode(code, language, highlights)
    }
}

@Composable
fun MarkdownHighlightedCode(
    code: String,
    language: String?,
    highlights: Highlights.Builder = Highlights.Builder(),
    style: TextStyle = LocalMarkdownTypography.current.code,
) {
    val backgroundCodeColor = LocalMarkdownColors.current.codeBackground
    LocalMarkdownDimens.current.codeBackgroundCornerSize
    val codeBlockPadding = LocalMarkdownPadding.current.codeBlock
    val syntaxLanguage = remember(language) { language?.let { SyntaxLanguage.getByName(it) } }

    val codeHighlights by remembering(code) {
        derivedStateOf {
            highlights
                .code(code)
                .let { if (syntaxLanguage != null) it.language(syntaxLanguage) else it }
                .build()
        }
    }

    MarkdownCodeBackground(
        color = backgroundCodeColor,
        shape = RoundedCornerShape(0.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 10.dp)
    ) {
        MarkdownBasicText(
            buildAnnotatedString {
                text(codeHighlights.getCode())
                codeHighlights.getHighlights()
                    .filterIsInstance<ColorHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(color = Color(it.rgb).copy(alpha = 1f)),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
                codeHighlights.getHighlights()
                    .filterIsInstance<BoldHighlight>()
                    .forEach {
                        addStyle(
                            SpanStyle(fontWeight = FontWeight.Bold),
                            start = it.location.start,
                            end = it.location.end,
                        )
                    }
            },
            color = LocalMarkdownColors.current.codeText,
            modifier = Modifier
                .padding(codeBlockPadding),
            style = style,
            softWrap = true,
        )
    }
}

@Composable
internal inline fun <T, K> remembering(
    key1: K,
    crossinline calculation: @DisallowComposableCalls (K) -> T,
): T = remember(key1) { calculation(key1) }

internal fun AnnotatedString.Builder.text(text: String, style: SpanStyle = SpanStyle()) =
    withStyle(style = style) {
        append(text)
    }


