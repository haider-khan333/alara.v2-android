package com.ai.alarav2.repository.chathistory

import android.util.Log
import com.ai.alarav2.data.remote.AlaraChatHistoryApi
import com.ai.alarav2.data.remote.AlaraGetAgentApi
import com.ai.alarav2.vm.chat.AlaraGetAgentState
import com.ai.alarav2.vm.chathistory.AlaraChatHistoryState
import com.mikepenz.markdown.compose.elements.createImageInlineTextContent
import javax.inject.Inject

class AlaraChatHistoryImpl @Inject constructor(
    private val api: AlaraChatHistoryApi,
) : AlaraChatHistoryRepo {

    override suspend fun getChatHistory(agentId: String, type: String):
            AlaraChatHistoryState {
        return try {
            val resp = api.getConversations(
                agentId = agentId,
                type = type,
                cursor = "q"
            )
            if (resp.isSuccessful) {
                val body = resp.body()

                if (body != null) {
                    Log.d("TAG", "getHistory: body = ${body.data}")
                    val map = body.data.mapValues { entry ->
                        entry.value.map { it.toUiModel() }
                    }

                    AlaraChatHistoryState.Success(map)
                } else {
                    AlaraChatHistoryState.Error("Empty body response")
                }
            } else {
                val errBody = resp.errorBody()?.string()
                Log.d("TAG", "getChatHistory: error = $errBody")
                AlaraChatHistoryState.Error(errBody ?: "Api Error")

            }

        } catch (ex: Exception) {
            Log.d("TAG", "getChatHistory: exception = ${ex.toString()}")
            AlaraChatHistoryState.Error(ex.message ?: "Network Exception")
        }
    }

}