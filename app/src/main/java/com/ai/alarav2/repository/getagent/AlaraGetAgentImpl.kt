package com.ai.alarav2.repository.getagent

import android.util.Log
import com.ai.alarav2.data.models.req.AlaraLoginRequest
import com.ai.alarav2.data.models.ui.AlaraAgentUiModel
import com.ai.alarav2.data.remote.AlaraGetAgentApi
import com.ai.alarav2.data.remote.AlaraLoginApi
import com.ai.alarav2.di.AlaraTokenManager
import com.ai.alarav2.vm.chat.AlaraGetAgentState
import com.ai.alarav2.vm.login.AlaraLoginUiState
import javax.inject.Inject

class AlaraGetAgentImpl @Inject constructor(
    private val api: AlaraGetAgentApi,
) : AlaraGetAgentRepo {

    override suspend fun getAgents(): AlaraGetAgentState {
        return try {
            val resp = api.getAgents()
            if (resp.isSuccessful) {
                val body = resp.body()

                if (body != null) {
                    Log.d("TAG", "getAgents: body = ${body.data}")
                    val uiList = body.data.map { it.toUiModel() }
                    AlaraGetAgentState.Success(uiList)
                } else {
                    AlaraGetAgentState.Error("Empty body response")
                }
            } else {
                val errBody = resp.errorBody()?.string()
                AlaraGetAgentState.Error(errBody ?: "Api Error")
            }

        } catch (ex: Exception) {
            AlaraGetAgentState.Error(ex.message ?: "Network Exception")
        }
    }

}