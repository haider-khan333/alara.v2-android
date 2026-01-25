package com.ai.alarav2.vm.chat

sealed class AlaraChatUiState {
    object Idle : AlaraChatUiState()
    object Loading : AlaraChatUiState()
    data class Success(val message: String) : AlaraChatUiState()
    data class Error(val message: String) : AlaraChatUiState()

}

sealed class AlaraChatError {
    abstract val code: Int?

    data class Http(override val code: Int, val body: String? = null) : AlaraChatError()
    data class Network(override val code: Int? = null, val message: String? = null) :
        AlaraChatError()

    data class Parse(override val code: Int? = null, val message: String? = null) : AlaraChatError()
    data class Unknown(override val code: Int? = null, val message: String? = null) :
        AlaraChatError()
}

sealed class AlaraChatResult {
    data class Stream(val lines: kotlinx.coroutines.flow.Flow<String>) : AlaraChatResult()
    data class Failure(val error: AlaraChatError) : AlaraChatResult()
}

fun AlaraChatError.toUiMessage(): String = when (this) {
    is AlaraChatError.Http -> when (code) {
        401 -> "Unauthorized. Please sign in again."
        403 -> "Forbidden. You don’t have access."
        429 -> "Too many requests. Try again in a moment."
        in 500..599 -> "Server error ($code). Please try again."
        else -> "Request failed ($code)."
    }

    is AlaraChatError.Network -> "Network error. Check internet and try again."
    is AlaraChatError.Parse -> "Response format error."
    is AlaraChatError.Unknown -> message ?: "Something went wrong."

}

