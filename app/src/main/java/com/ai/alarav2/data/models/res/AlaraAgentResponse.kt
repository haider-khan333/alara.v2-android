package com.ai.alarav2.data.models.res

import com.ai.alarav2.data.models.ui.AlaraAgentUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class AlaraAgentResponse(
    @SerialName("success")
    val success: Boolean,

    @SerialName("data")
    val data: List<ConversationDto> = emptyList(),
) {
    @Serializable
    data class ConversationDto(
        @SerialName("_id")
        val _id: String,

        @SerialName("tenantId")
        val tenantId: String,

        @SerialName("name")
        val name: String,

        @SerialName("description")
        val description: String,

        @SerialName("type")
        val type: String? = null,

        @SerialName("createdAt")
        val createdAt: String? = null,

        @SerialName("updatedAt")
        val updatedAt: String? = null

    ) {
        fun toUiModel(): AlaraAgentUiModel {
            return AlaraAgentUiModel(
                agentId = _id,
                agentName = name,
                agentDescription = description,
                agentType = type ?: "Unknown"
            )
        }
    }


}

