package com.ai.alarav2.data.models.ui

import androidx.compose.ui.graphics.vector.ImageVector

data class AlaraChatUiModels(
    val message: String,
    val isUser: Boolean,
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

