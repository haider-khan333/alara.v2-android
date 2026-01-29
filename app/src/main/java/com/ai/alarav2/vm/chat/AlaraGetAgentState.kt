package com.ai.alarav2.vm.chat

import com.ai.alarav2.data.models.ui.AlaraAgentUiModel

sealed interface AlaraGetAgentState {
    object Idle : AlaraGetAgentState
    object Loading : AlaraGetAgentState
    data class Success(val response: List<AlaraAgentUiModel>) : AlaraGetAgentState
    data class Error(val message: String) : AlaraGetAgentState
}


