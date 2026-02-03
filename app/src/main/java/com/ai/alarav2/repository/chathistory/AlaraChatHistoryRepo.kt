package com.ai.alarav2.repository.chathistory

import com.ai.alarav2.vm.chathistory.AlaraChatHistoryState

interface AlaraChatHistoryRepo {
    suspend fun getChatHistory(agentId: String, type: String): AlaraChatHistoryState
}