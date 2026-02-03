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


        @SerialName("category")
        val category: String,

        ) {
        fun toUiModel(): AlaraChatUiModels {
            return AlaraChatUiModels(
                sessionId = sessionId,
                message = text,
                isUser = sender.equals("user", ignoreCase = true),
            )

        }
    }


}




