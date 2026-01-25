package com.ai.alarav2.data.remote

import com.ai.alarav2.data.models.req.AlaraChatRequest
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Streaming

interface AlaraChatApi {

    @Streaming
    @POST("chat")

    suspend fun sendMessage(
        @Body request: AlaraChatRequest,
        @Header("Accept") accept: String = "text/event-stream",
        @Header("Cache-Control") cache: String = "no-cache",
        @Header("Connection") connection: String = "keep-alive",
        @Header("Accept-Encoding") acceptEncoding: String = "identity"
    ): Response<ResponseBody>

}