package com.ai.alarav2.vm.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ai.alarav2.data.models.req.AlaraLoginRequest
import com.ai.alarav2.data.remote.AlaraLoginApi
import com.ai.alarav2.repository.login.AlaraLoginRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AlaraLoginViewModel @Inject constructor(private val loginRepo: AlaraLoginRepo) : ViewModel() {

    private val _email = MutableStateFlow("")
    val email: StateFlow<String> = _email

    private val _password = MutableStateFlow("")
    val password: StateFlow<String> = _password

    private val _isContinueEnabled = MutableStateFlow(false)
    val isContinueEnabled: StateFlow<Boolean> = _isContinueEnabled

    private val _loginState = MutableStateFlow<AlaraLoginUiState>(AlaraLoginUiState.Idle)
    val loginState: StateFlow<AlaraLoginUiState> = _loginState

    private val _typewriterTaglines = MutableStateFlow(
        listOf(
            "Hello, Creator",
            "Welcome Back",
            "Spark an Idea",
            "Design the UI",
            "Generate Code",
            "Refine Logic",
            "Deploy Build",
            "Scale Global",
            "Excel Today",
            "Let's Build AI"
        )
    )
    val typewriterTaglines: StateFlow<List<String>> = _typewriterTaglines


    fun login(
        userName: String,
        password: String
    ) {
        _loginState.value = AlaraLoginUiState.Loading
        val loginRequest = AlaraLoginRequest(
            email = userName,
            password = password,
            organizationCode = "org-52b5204a-7312-44e8-9a4a-a87dff23aeb0"
        )
        viewModelScope.launch {
            val response = loginRepo.login(request = loginRequest)
            // save the models in
            _loginState.value = response
        }


    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
        validateInput()
    }

    fun updatePassword(newPassword: String) {
        _password.value = newPassword
        validateInput()
    }

    // Helper function to keep logic in one place
    private fun validateInput() {
        val emailValid = _email.value.isNotBlank() // Add regex if needed
        val passwordValid = _password.value.length >= 6 // Example rule

        _isContinueEnabled.value = emailValid && passwordValid
    }

    fun resetLoginState() {
        _loginState.value = AlaraLoginUiState.Idle
    }


}