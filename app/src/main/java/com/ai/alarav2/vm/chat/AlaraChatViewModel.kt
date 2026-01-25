package com.ai.alarav2.vm.chat

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AddPhotoAlternate
import androidx.compose.material.icons.rounded.AttachFile
import androidx.compose.material.icons.rounded.CameraEnhance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.data.models.ui.AlaraChatUiModels
import com.ai.alarav2.data.models.ui.AlaraModelsUiModel
import com.ai.alarav2.data.models.ui.AlaraUploadUiModel
import com.ai.alarav2.repository.AlaraChatRepo
import com.ai.alarav2.ui.view.chat.components.AlaraClickType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.json.JSONObject
import javax.inject.Inject

@HiltViewModel
class AlaraChatViewModel @Inject constructor(
    private val chatRepo: AlaraChatRepo
) : ViewModel() {
    private val _showSheet = MutableStateFlow(false)
    val showSheet: StateFlow<Boolean> = _showSheet

    private val _messages = MutableStateFlow<List<AlaraChatUiModels>>(mutableListOf())
    val messages: StateFlow<List<AlaraChatUiModels>> = _messages

    private val _clickType = MutableStateFlow<AlaraClickType?>(null)
    val clickType: StateFlow<AlaraClickType?> = _clickType

    private val _chatState = MutableStateFlow<AlaraChatUiState>(AlaraChatUiState.Idle)
    val chatState: StateFlow<AlaraChatUiState> = _chatState


    private val _models = MutableStateFlow<List<AlaraModelsUiModel>>(
        mutableListOf(
            AlaraModelsUiModel(
                heading = "AutoGPT",
                subHeading = "Autonomous agent that breaks goals into sub-tasks using GPT-4.",
                isSelected = true
            ),
            AlaraModelsUiModel(
                heading = "BabyAGI",
                subHeading = "Task management agent that prioritizes and executes tasks in a loop.",
                isSelected = false
            ),
            AlaraModelsUiModel(
                heading = "AgentGPT",
                subHeading = "Browser-based platform to deploy autonomous agents via web.",
                isSelected = false
            ),
            AlaraModelsUiModel(
                heading = "Camel",
                subHeading = "Role-playing framework where agents converse to solve tasks.",
                isSelected = false
            ),
            AlaraModelsUiModel(
                heading = "SuperAGI",
                subHeading = "Open-source framework for building and managing useful agents.",
                isSelected = false
            )
        )
    )
    private val _selectedModel: MutableStateFlow<String> =
        MutableStateFlow(_models.value.first().heading)


    val selectedModel: StateFlow<String> = _selectedModel

    val models: StateFlow<List<AlaraModelsUiModel>> = _models

    private val _uploadOptions: MutableStateFlow<List<AlaraUploadUiModel>> = MutableStateFlow(
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

    fun addMessage(message: AlaraChatUiModels) {
        _messages.value += message

    }

    fun setClickType(type: AlaraClickType) {
        _clickType.value = type
    }

    fun updateSelection(index: Int) {
        _models.update { currentList ->
            currentList.mapIndexed { i, model ->
                if (i == index) {
                    model.copy(isSelected = !model.isSelected)
                } else {
                    model.copy(isSelected = false)
                }
            }

        }
    }

    fun setModel(model: String) {
        _selectedModel.value = model
    }


    fun sendMessage(message: String) {
        viewModelScope.launch {
            _messages.update { it + AlaraChatUiModels(message = message, isUser = true) }
            _chatState.value = AlaraChatUiState.Loading

            val req = AlaraChatRequest(
                agentId = "6970f8ed7f1e9a37b6507b90",
                message = message,
                sessionId = "6970f74b37273f8c908d8dbf-1769292512795",
                stream = true
            )

            when (val result = chatRepo.sendMessage(req)) {
                is AlaraChatResult.Failure -> {
                    when (result.error.code) {
                        401 -> {
                            // unauthorized
                            // api call for refresh token
                            _chatState.value = AlaraChatUiState.Error("Unauthorized")

                        }

                        in 500..509 -> {
                            _chatState.value = AlaraChatUiState.Error("Server Error")
                        }

                        else -> {
                            _chatState.value = AlaraChatUiState.Error(result.error.toUiMessage())
                        }
                    }
                    _chatState.value = AlaraChatUiState.Error(result.error.toUiMessage())
                }

                is AlaraChatResult.Stream -> {
                    var isFirstChunk = true

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

                            if (isFirstChunk) {
                                _messages.update {
                                    it + AlaraChatUiModels(
                                        message = token,
                                        isUser = false
                                    )
                                }
                                _chatState.value = AlaraChatUiState.Success("Streaming...")
                                isFirstChunk = false
                            } else {
                                if (type.equals("end", ignoreCase = true)) {
                                    _chatState.value = AlaraChatUiState.Success("Completed")
                                    return@collect
                                }
                                _messages.update { current ->
                                    val list = current.toMutableList()
                                    val last = list.lastIndex
                                    list[last] =
                                        list[last].copy(message = list[last].message + token)
                                    list
                                }
                            }
                        } catch (e: Exception) {
                            _chatState.value = AlaraChatUiState.Error("Stream parse error")
                        }
                    }
                }
            }
        }
    }


}