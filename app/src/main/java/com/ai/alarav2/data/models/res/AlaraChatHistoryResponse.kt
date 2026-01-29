package com.ai.alarav2.data.models.res

import com.ai.alarav2.data.models.ui.AlaraChatUiModels
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AlaraChatHistoryResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("data")
    val data: Map<String, List<ChatMessage>>
) {
    @Serializable
    data class ChatMessage(
        @SerialName("id")
        val id: String,

        @SerialName("flow_id")
        val flowId: String,

        @SerialName("timestamp")
        val timestamp: String,

        @SerialName("sender")
        val sender: String,

        @SerialName("sender_name")
        val senderName: String,

        @SerialName("session_id")
        val sessionId: String,

        @SerialName("text")
        val text: String,

        @SerialName("files")
        val files: String = "[]",

        @SerialName("edit")
        val edit: Boolean = false,

        @SerialName("duration")
        val duration: Long? = null,

        @SerialName("properties")
        val properties: MessageProperties,

        @SerialName("category")
        val category: String,

        @SerialName("content_blocks")
        val contentBlocks: List<ContentBlock> = emptyList()
    )

    @Serializable
    data class MessageProperties(
        @SerialName("text_color")
        val textColor: String = "",

        @SerialName("background_color")
        val backgroundColor: String = "",

        @SerialName("edited")
        val edited: Boolean = false,

        @SerialName("source")
        val source: MessageSource = MessageSource(),

        @SerialName("icon")
        val icon: String = "",

        @SerialName("allow_markdown")
        val allowMarkdown: Boolean = false,

        @SerialName("positive_feedback")
        val positiveFeedback: Boolean? = null,

        @SerialName("state")
        val state: String = "",

        @SerialName("targets")
        val targets: List<String> = emptyList()
    )

    @Serializable
    data class MessageSource(
        @SerialName("id")
        val id: String? = null,

        @SerialName("display_name")
        val displayName: String? = null,

        @SerialName("source")
        val source: String? = null
    )

    @Serializable
    data class ContentBlock(
        @SerialName("title")
        val title: String? = null,

        @SerialName("contents")
        val contents: List<ContentItem> = emptyList(),

        @SerialName("allow_markdown")
        val allowMarkdown: Boolean? = null,

        @SerialName("media_url")
        val mediaUrl: String? = null
    )

    @Serializable
    data class ContentItem(
        @SerialName("type")
        val type: String,

        @SerialName("duration")
        val duration: Long? = null,

        @SerialName("header")
        val header: Map<String, String> = emptyMap(),

        @SerialName("component")
        val component: String? = null,

        @SerialName("field")
        val field: String? = null,

        @SerialName("reason")
        val reason: String? = null,

        @SerialName("solution")
        val solution: String? = null,

        @SerialName("traceback")
        val traceback: String? = null
    )

    fun toUiModel(msg: ChatMessage): AlaraChatUiModels {
        return AlaraChatUiModels(
            message = msg.text,
            isUser = msg.sender.equals("user", ignoreCase = true),
        )

    }
}




