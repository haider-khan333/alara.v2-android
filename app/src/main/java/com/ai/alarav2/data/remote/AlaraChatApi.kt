package com.ai.alarav2.data.remote

import com.ai.alarav2.data.models.req.AlaraChatRequest
import okhttp3.ResponseBody
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Streaming

interface AlaraChatApi {

    @Streaming
    @POST("chat")
    suspend fun sendMessage(@Body request: AlaraChatRequest): ResponseBody

}