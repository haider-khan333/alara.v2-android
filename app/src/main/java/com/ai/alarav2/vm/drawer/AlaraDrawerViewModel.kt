package com.ai.alarav2.vm.drawer

import androidx.lifecycle.ViewModel
import com.ai.alarav2.di.AlaraTokenManager
import com.ai.alarav2.routes.AlaraRoutes
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AlaraDrawerVm @Inject constructor(private val tokenManager: AlaraTokenManager) : ViewModel() {
    private val _drawerState = MutableStateFlow(false)
    val drawerState = _drawerState.asStateFlow()

    private val _startRoute = MutableStateFlow(AlaraRoutes.Login)
    val startRoute = _startRoute.asStateFlow()


    private val _showSplashScreen = MutableStateFlow(false)
    val showSplashScreen = _showSplashScreen.asStateFlow()


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

    suspend fun logout() {
        tokenManager.clearTokens()
        _startRoute.value = AlaraRoutes.Login
        _drawerState.value = false
        _showSplashScreen.value = true
    }


}