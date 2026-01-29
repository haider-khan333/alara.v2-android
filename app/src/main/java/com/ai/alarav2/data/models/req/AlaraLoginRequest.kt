package com.ai.alarav2.data.models.req

data class AlaraLoginRequest(
    val email: String,
    val password: String,
    val organizationCode: String
)