package com.ai.alarav2.data.remote

import com.ai.alarav2.data.models.req.AlaraLoginRequest
import com.ai.alarav2.data.models.res.AlaraLoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AlaraLoginApi {
    @POST("login")
    suspend fun login(
        @Body request: AlaraLoginRequest,
    ): Response<AlaraLoginResponse>

}