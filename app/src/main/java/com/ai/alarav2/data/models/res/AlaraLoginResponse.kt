package com.ai.alarav2.data.models.res

import com.ai.alarav2.data.models.ui.AlaraLoginUiModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class AlaraLoginResponse(
    val message: String,
    val data: Data
) {
    @Serializable
    data class Data(
        val accessToken: String,
        val refreshToken: String,
        val sessionId: String,
        val user: User
    )

    @Serializable
    data class User(
        @SerialName("_id")
        val id: String,
        val username: String,
        val email: String,
        val firstName: String,
        val lastName: String,
        val permission: Permission,
        val teams: List<Team> = emptyList(),
        val isEmailVerified: Boolean,
        val isActive: Boolean,
        val isArchived: Boolean,
        val archivedAt: String? = null,
        val mustChangePassword: Boolean,
        val organization: String,
        val createdAt: String,
        val updatedAt: String,
        @SerialName("__v")
        val v: Int
    )

    @Serializable
    data class Permission(
        @SerialName("_id")
        val id: String,
        val name: String,
        val modulePermissions: List<ModulePermission>,
        val level: String,
        val description: String
    )

    @Serializable
    data class ModulePermission(
        val moduleId: ModuleId,
        val actions: List<String>
    )

    @Serializable
    data class ModuleId(
        @SerialName("_id")
        val id: String,
        val label: String,
        val value: String,
        val description: String
    )

    @Serializable
    data class Team(
        @SerialName("_id")
        val id: String? = null,
        val name: String? = null
    )

    fun toUiModel(): AlaraLoginUiModel {
        return AlaraLoginUiModel(
            email = data.user.email,
            firstName = data.user.firstName,
            lastName = data.user.lastName,
            phoneNumber = "",
            role = ""
        )

    }
}


