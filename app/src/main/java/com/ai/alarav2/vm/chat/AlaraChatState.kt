package com.ai.alarav2.vm.chat

sealed class AlaraChatUiState {
    object Idle : AlaraChatUiState()
    object Loading : AlaraChatUiState()
    data class Success(val message: String) : AlaraChatUiState()
    data class Error(val message: String) : AlaraChatUiState()

}