package com.ai.alarav2.repository

import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.vm.chat.AlaraChatResult
import okhttp3.ResponseBody

interface AlaraChatRepo {
    suspend fun sendMessage(request: AlaraChatRequest): AlaraChatResult


}