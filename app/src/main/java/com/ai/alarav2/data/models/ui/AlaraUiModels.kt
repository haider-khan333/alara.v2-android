package com.ai.alarav2.data.models.ui

import androidx.compose.ui.graphics.vector.ImageVector
import com.mikepenz.markdown.model.MarkdownState
import org.intellij.markdown.ast.ASTNode
import java.util.UUID

data class AlaraChatUiModels(
    val id: String = UUID.randomUUID().toString(),
    val sessionId: String? = null,
    val message: String,
    val isUser: Boolean,
    val markdownState: MarkdownState? = null
)

data class AlaraModelsUiModel(
    val heading: String,
    val subHeading: String,
    var isSelected: Boolean
)

data class AlaraUploadUiModel(
    val text: String,
    val icon: ImageVector
)

data class AlaraLoginUiModel(
    val email: String,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    val role: String
)

data class AlaraAgentUiModel(
    val agentId: String,
    val agentName: String,
    val agentDescription: String,
    val agentType: String,
    val isSelected: Boolean = false
)




