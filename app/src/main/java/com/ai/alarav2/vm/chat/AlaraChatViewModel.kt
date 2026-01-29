package com.ai.alarav2.vm.chat

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.data.models.ui.AlaraAgentUiModel
import com.ai.alarav2.data.models.ui.AlaraChatUiModels
import com.ai.alarav2.data.models.ui.AlaraModelsUiModel
import com.ai.alarav2.data.models.ui.AlaraUploadUiModel
import com.ai.alarav2.di.AlaraTokenManager
import com.ai.alarav2.repository.chat.AlaraChatRepo
import com.ai.alarav2.repository.getagent.AlaraGetAgentRepo
import com.ai.alarav2.ui.view.chat.components.AlaraClickType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AlaraChatViewModel @Inject constructor(
    private val chatRepo: AlaraChatRepo,
    private val getAgentRepo: AlaraGetAgentRepo
) : ViewModel() {

    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet

    private val _messages = MutableStateFlow<List<AlaraChatUiModels>>(emptyList())
    val messages: StateFlow<List<AlaraChatUiModels>> = _messages

    private val _clickType = MutableStateFlow<AlaraClickType?>(null)
    val clickType: StateFlow<AlaraClickType?> = _clickType

    private val _chatState = MutableStateFlow<AlaraChatUiState>(AlaraChatUiState.Idle)
    val chatState: StateFlow<AlaraChatUiState> = _chatState

    private val _agentState = MutableStateFlow<AlaraGetAgentState>(AlaraGetAgentState.Idle)
    val agentState: StateFlow<AlaraGetAgentState> = _agentState


    private var streamingJob: Job? = null


    private val _selectedModel = MutableStateFlow("")
    val selectedModel: StateFlow<String> = _selectedModel

    private val _models = MutableStateFlow<List<AlaraAgentUiModel>>(emptyList())
    val models: StateFlow<List<AlaraAgentUiModel>> = _models


    init {
        getAgents()
    }

    private val _uploadOptions = MutableStateFlow(
        listOf(
            AlaraUploadUiModel(text = "Camera", icon = Icons.Rounded.CameraEnhance),
            AlaraUploadUiModel(text = "Gallery", icon = Icons.Rounded.AddPhotoAlternate),
            AlaraUploadUiModel(text = "Files", icon = Icons.Rounded.AttachFile),
        )
    )
    val uploadOptions: StateFlow<List<AlaraUploadUiModel>> = _uploadOptions

    fun showSheet() {
        _showSheet.value = true
    }

    fun hideSheet() {
        _showSheet.value = false
    }

    fun setClickType(type: AlaraClickType) {
        _clickType.value = type
    }

    fun updateSelection(index: Int) {
        val updated = _models.value.mapIndexed { i, model ->
            model.copy(isSelected = i == index)
        }
        _models.value = updated
        _selectedModel.value = updated.getOrNull(index)?.agentName.orEmpty()
    }


    fun setModel(model: String) {
        _selectedModel.value = model
    }

    fun stopStreaming() {
        streamingJob?.cancel()
        streamingJob = null
        _chatState.value = AlaraChatUiState.Success("Stopped")
    }

    fun sendMessage(message: String) {
        streamingJob?.cancel()

        streamingJob = viewModelScope.launch {
            try {
                _messages.update { it + AlaraChatUiModels(message = message, isUser = true) }
                _chatState.value = AlaraChatUiState.Loading

                val req = AlaraChatRequest(
                    agentId = "6970f8ed7f1e9a37b6507b90",
                    message = message,
                    sessionId = "6970f74b37273f8c908d8dbf-1769451831615",
                    stream = true
                )

                when (val result = chatRepo.sendMessage(req)) {
                    is AlaraChatResult.Failure -> {
                        handleError(result.error)
                    }

                    is AlaraChatResult.Stream -> {
                        handleStreamingResponse(result)
                    }
                }
            } catch (e: Exception) {
                Log.e("AlaraChatVM", "Error sending message", e)
                _chatState.value = AlaraChatUiState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private suspend fun handleStreamingResponse(result: AlaraChatResult.Stream) {
        var isFirstChunk = true
        var assembled = ""
        var lastToken = ""

        result.lines.collect { raw ->
            val line = raw.trim()
            if (line.isEmpty() || !line.startsWith("data:")) return@collect

            val jsonString = line.removePrefix("data:").trim()
            if (jsonString == "[DONE]") {
                _chatState.value = AlaraChatUiState.Success("Completed")
                return@collect
            }

            try {
                val root = JSONObject(jsonString)
                val data = root.getJSONObject("data")
                val token = data.optString("message", "")
                val type = data.optString("type", "")

                if (type.equals("end", ignoreCase = true)) {
                    _chatState.value = AlaraChatUiState.Success("Completed")
                    return@collect
                }

                // Skip duplicate tokens
                if (token == lastToken) return@collect
                lastToken = token

                // Handle incremental vs full message
                val newAssembled = when {
                    token.startsWith(assembled) -> token
                    else -> assembled + token
                }

                // Skip if no change
                if (newAssembled == assembled) return@collect
                assembled = newAssembled

                if (isFirstChunk) {
                    _messages.update {
                        it + AlaraChatUiModels(message = assembled, isUser = false)
                    }
                    _chatState.value = AlaraChatUiState.Streaming
                    isFirstChunk = false
                } else {
                    _messages.update { current ->
                        val list = current.toMutableList()
                        val lastIndex = list.lastIndex
                        if (lastIndex >= 0 && !list[lastIndex].isUser) {
                            list[lastIndex] = list[lastIndex].copy(message = assembled)
                        }
                        list
                    }
                }
            } catch (e: Exception) {
                Log.e("AlaraChatVM", "Stream parse error", e)
                _chatState.value = AlaraChatUiState.Error("Stream parse error")
            }
        }

        if (_chatState.value is AlaraChatUiState.Streaming) {
            _chatState.value = AlaraChatUiState.Success("Completed")
        }
    }


    private fun handleError(error: AlaraChatError) {
        val errorMessage = when (error.code) {
            // Auth Errors
            401 -> "Your session has expired. Please sign in again to continue."
            403 -> "You don't have permission to access this. Please check your account status."

            // Server Errors (Categorized)
            500 -> "Our servers are having a moment. Please try again shortly."
            503 -> "Alara is currently at capacity. Please check back in a few minutes."
            504 -> "The server took too long to respond. Please try again."
            in 500..509 -> "We're experiencing technical difficulties. We're working on it!"

            // Client / Network Errors (Common codes for no internet/timeout)
            -1, -2 -> "No internet connection. Please check your network settings."
            408 -> "Request timed out. Please check your connection and try again."
            429 -> "You've sent too many messages too quickly. Please wait a moment."

            // Fallback using your existing mapper or a generic friendly message
            else -> error.toUiMessage().ifBlank { "Something went wrong. Please try again." }
        }

        _chatState.value = AlaraChatUiState.Error(errorMessage)
        streamingJob?.cancel()
    }

    fun getAgents() {
        _agentState.value = AlaraGetAgentState.Loading
        viewModelScope.launch {
            _agentState.value = getAgentRepo.getAgents()

            if (_agentState.value is AlaraGetAgentState.Success) {
                val list = (_agentState.value as AlaraGetAgentState.Success).response
                Log.d("TAG", "getAgents: List=$list")

                if (list.isNotEmpty()) {
                    val selectedIndex = list.indexOfFirst { it.isSelected }.takeIf { it >= 0 } ?: 0
                    val updated =
                        list.mapIndexed { i, m -> m.copy(isSelected = i == selectedIndex) }

                    _models.value = updated
                    _selectedModel.value = updated[selectedIndex].agentName
                } else {
                    Log.d("TAG", "getAgents: empty list")
                    _models.value = emptyList()
                    _selectedModel.value = ""
                }
            }else if(_agentState.value is AlaraGetAgentState.Error){
                Log.d("TAG", "getAgents: Error = ${_agentState.value}")

            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        streamingJob?.cancel()
    }
}
