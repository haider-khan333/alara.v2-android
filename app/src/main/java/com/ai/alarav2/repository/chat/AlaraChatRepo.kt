package com.ai.alarav2.repository.chat

import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.vm.chat.AlaraChatResult

interface AlaraChatRepo {
    suspend fun sendMessage(request: AlaraChatRequest): AlaraChatResult


}