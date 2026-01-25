package com.ai.alarav2.data.models.req

data class AlaraChatRequest(
    val agentId: String,
    val message: String,
    val sessionId: String,
    val stream: Boolean
)