package com.ai.alarav2.vm.login

import com.ai.alarav2.data.models.ui.AlaraLoginUiModel

sealed interface AlaraLoginUiState {
    object Idle : AlaraLoginUiState
    object Loading : AlaraLoginUiState
    data class Success(val response: AlaraLoginUiModel) : AlaraLoginUiState
    data class Error(val message: String) : AlaraLoginUiState
}

