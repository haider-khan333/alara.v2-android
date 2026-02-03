package com.ai.alarav2.vm.chathistory

import com.ai.alarav2.data.models.ui.AlaraChatUiModels

sealed interface AlaraChatHistoryState {
    object Idle : AlaraChatHistoryState
    object Loading : AlaraChatHistoryState
    data class Success(val response: Map<String, List<AlaraChatUiModels>>) : AlaraChatHistoryState
    data class Error(val message: String) : AlaraChatHistoryState
}


sealed interface AlaraChatHistoryUiState {
    object Idle : AlaraChatHistoryUiState
    object Loading : AlaraChatHistoryUiState
    data class Success(val response: List<AlaraChatUiModels>) : AlaraChatHistoryUiState
    data class Error(val message: String) : AlaraChatHistoryUiState
}

