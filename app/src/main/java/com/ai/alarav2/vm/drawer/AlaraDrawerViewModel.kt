package com.ai.alarav2.vm.drawer

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.alarav2.di.AlaraTokenManager
import com.ai.alarav2.repository.chathistory.AlaraChatHistoryRepo
import com.ai.alarav2.routes.AlaraRoutes
import com.ai.alarav2.vm.chathistory.AlaraChatHistoryState
import com.ai.alarav2.vm.chathistory.AlaraChatHistoryUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlaraDrawerViewModel @Inject constructor(
    private val tokenManager: AlaraTokenManager,
    private val repository: AlaraChatHistoryRepo
) : ViewModel() {
    private val _drawerState = MutableStateFlow(false)
    val drawerState = _drawerState.asStateFlow()

    private val _startRoute = MutableStateFlow(AlaraRoutes.Login)
    val startRoute = _startRoute.asStateFlow()


    private val _showSplashScreen = MutableStateFlow(false)
    val showSplashScreen = _showSplashScreen.asStateFlow()

    private val _state = MutableStateFlow<AlaraChatHistoryUiState>(AlaraChatHistoryUiState.Idle)
    val state: StateFlow<AlaraChatHistoryUiState> = _state

    init {
        Log.d("TAG", "getting chat history ")
        getChatHistory(agentId = "6970f8ed7f1e9a37b6507b90", type = "prebuilt")
    }

    fun openDrawer() {
        _drawerState.value = true
    }

    fun closeDrawer() {
        _drawerState.value = false

    }

    fun showSplashScreen() {
        _showSplashScreen.value = true
    }

    fun hideSplashScreen() {
        _showSplashScreen.value = false
    }

    fun initDestination(): AlaraRoutes {
        return if (tokenManager.getAccessTokenSync() != null) {
            AlaraRoutes.Chat
        } else {
            AlaraRoutes.Login
        }
    }

    fun getChatHistory(agentId: String, type: String) {
        viewModelScope.launch {
            _state.value = AlaraChatHistoryUiState.Loading
            when (val result = repository.getChatHistory(agentId, type)) {
                is AlaraChatHistoryState.Error -> {
                    _state.value = AlaraChatHistoryUiState.Error(result.message)

                }

                is AlaraChatHistoryState.Success -> {
                    val res = result.response
                    // we will get first message from each map and add it ot a list
                    val list = res.mapValues { entry ->
                        entry.value.firstOrNull()
                    }.values.filterNotNull()

                    Log.d("TAG", "getChatHistory: list = $list")

                    _state.value = AlaraChatHistoryUiState.Success(list)
                }

                is AlaraChatHistoryState.Loading -> {
                    _state.value = AlaraChatHistoryUiState.Loading
                }


                else -> {
                    _state.value = AlaraChatHistoryUiState.Idle
                }

            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            tokenManager.clearTokens()
            _startRoute.value = AlaraRoutes.Login
            _drawerState.value = false
        }
    }


}