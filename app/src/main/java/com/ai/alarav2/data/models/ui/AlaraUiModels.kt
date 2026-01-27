package com.ai.alarav2.data.models.ui

import androidx.compose.ui.graphics.vector.ImageVector
import com.mikepenz.markdown.model.MarkdownState
import org.intellij.markdown.ast.ASTNode
import java.util.UUID

data class AlaraChatUiModels(
    val id: String = UUID.randomUUID().toString(),
    val message: String,
    val isUser: Boolean,
    val markdownState: MarkdownState? = null)
data class AlaraModelsUiModel(
    val heading: String,
    val subHeading: String,
    var isSelected: Boolean
)

data class AlaraUploadUiModel(
    val text: String,
    val icon: ImageVector
)

