package com.ai.alarav2.data.remote

import com.ai.alarav2.data.models.res.AlaraChatHistoryResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AlaraChatHistoryApi {
    @GET("chat/conversations")
    suspend fun getConversations(
        @Query("agentId") agentId: String,
        @Query("type") type: String,
        @Query("cursor") cursor: String
    ): Response<AlaraChatHistoryResponse>
}