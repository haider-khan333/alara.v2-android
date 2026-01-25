package com.ai.alarav2.repository

import com.ai.alarav2.data.models.req.AlaraChatRequest
import okhttp3.ResponseBody

interface AlaraChatRepo {
    suspend fun sendMessage(request: AlaraChatRequest): ResponseBody


}