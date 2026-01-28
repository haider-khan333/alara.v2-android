package com.ai.alarav2.vm.drawer

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class AlaraDrawerVm @Inject constructor() : ViewModel() {
    private val _drawerState = MutableStateFlow(false)
    val drawerState = _drawerState.asStateFlow()

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



}