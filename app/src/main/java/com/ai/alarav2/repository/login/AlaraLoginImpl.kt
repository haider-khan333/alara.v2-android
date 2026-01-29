package com.ai.alarav2.repository.login

import com.ai.alarav2.data.models.req.AlaraLoginRequest
import com.ai.alarav2.data.models.res.AlaraLoginResponse
import com.ai.alarav2.data.remote.AlaraLoginApi
import com.ai.alarav2.di.AlaraTokenManager
import com.ai.alarav2.vm.login.AlaraLoginUiState
import retrofit2.Response
import javax.inject.Inject

class AlaraLoginImpl @Inject constructor(
    private val api: AlaraLoginApi,
    private val tokenManager: AlaraTokenManager
) : AlaraLoginRepo {

    override suspend fun login(request: AlaraLoginRequest): AlaraLoginUiState {
        return try {
            val resp = api.login(request = request)
            if (resp.isSuccessful) {
                val body = resp.body()
                if (body != null) {
                    tokenManager.saveTokens(
                        accessToken = body.data.accessToken,
                        refreshToken = body.data.refreshToken
                    )

                    AlaraLoginUiState.Success(body.toUiModel())
                } else {
                    AlaraLoginUiState.Error("Empty body response")
                }
            } else {
                val errBody = resp.errorBody()?.string()
                AlaraLoginUiState.Error(errBody ?: "Api Error")
            }

        } catch (ex: Exception) {
            AlaraLoginUiState.Error(ex.message ?: "Network Exception")
        }
    }

}