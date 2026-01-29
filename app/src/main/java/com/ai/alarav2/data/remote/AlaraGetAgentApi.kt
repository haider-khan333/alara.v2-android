package com.ai.alarav2.data.remote

import com.ai.alarav2.data.models.res.AlaraAgentResponse
import com.ai.alarav2.data.models.res.AlaraChatHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlaraGetAgentApi {
    @GET("agents")
    suspend fun getAgents(
    ): Response<AlaraAgentResponse>
}