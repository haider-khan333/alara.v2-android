package com.ai.alarav2.repository.getagent

import com.ai.alarav2.vm.chat.AlaraGetAgentState

interface AlaraGetAgentRepo {
    suspend fun getAgents(): AlaraGetAgentState
}