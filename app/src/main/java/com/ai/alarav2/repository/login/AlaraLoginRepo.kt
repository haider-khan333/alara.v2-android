package com.ai.alarav2.repository.login

import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.data.models.req.AlaraLoginRequest
import com.ai.alarav2.data.models.res.AlaraLoginResponse
import com.ai.alarav2.data.models.ui.AlaraLoginUiModel
import com.ai.alarav2.vm.chat.AlaraChatResult
import com.ai.alarav2.vm.login.AlaraLoginUiState

interface AlaraLoginRepo {
    suspend fun login(request: AlaraLoginRequest): AlaraLoginUiState
}