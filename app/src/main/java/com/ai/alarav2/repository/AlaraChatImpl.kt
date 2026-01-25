package com.ai.alarav2.repository

import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.data.remote.AlaraChatApi
import okhttp3.ResponseBody
import javax.inject.Inject

class AlaraChatImpl @Inject constructor(
    private val api: AlaraChatApi
) : AlaraChatRepo {
    override suspend fun sendMessage(request: AlaraChatRequest): ResponseBody {
        return api.sendMessage(request)
    }

}