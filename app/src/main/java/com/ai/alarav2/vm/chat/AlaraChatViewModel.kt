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

    private val _markdownTest = """// 1. Define the test variable using triple quotes
val testMarkdownMessage = ""${'"'}
Since your request is quite broad, I have designed a robust, production-ready **Data Fetching Hook**. This is a fundamental building block in modern frontend engineering that allows me to demonstrate the core principles of the defined coding style: type safety, defensive programming, explicit error handling, and comprehensive documentation.

This solution implements a custom `useFetch` hook that handles asynchronous data fetching with automatic aborting (to prevent memory leaks), generic type support for type-safe responses, and structured error handling.

### Implementation Details

**1. Type Safety & Generics:**

The hook uses TypeScript Generics (`T`) to ensure that the data returned matches the expected interface. This eliminates the need for manual type casting later in the component lifecycle.

**2. Error Handling Strategy:**

Instead of relying on raw `Error` objects, we define a specific `ApiError` interface. This allows consumers of the hook to distinguish between network errors, parsing errors, and HTTP status errors (like 404 or 500), enabling better UX decisions (e.g., showing a "Not Found" message vs. "Connection Lost").

**3. Cleanup & Memory Management:**

We utilize `AbortController` within the `useEffect` cleanup function. This is critical for preventing race conditions where a slow network response might overwrite state after a user has already navigated away or changed filters.

**4. Defensive Programming:**

The hook includes guards to ensure the URL is valid before attempting a fetch and explicitly checks `response.ok` to handle HTTP error status codes that `fetch` does not treat as exceptions by default.

### The Code

Here is the implementation of the hook followed by a usage example.

```typescript
/**
 * @file use_fetch.ts
 * @description Custom hook for handling data fetching with type safety, error handling, and abort capabilities.
 * @created_by Frontend Maker
 */

import { useState, useEffect, useCallback, useRef } from "react";

/**
 * Custom error interface to provide detailed information about API failures.
 * Extends the standard Error class to include status codes and context.
 */
export interface ApiError extends Error {
  code?: string;
  status?: number;
  details?: unknown;
}

/**
 * Represents the state of the API request.
 */
export interface FetchState<T> {
  data: T | null;
  isLoading: boolean;
  error: ApiError | null;
}

/**
 * Configuration options for the fetch request.
 */
export interface UseFetchOptions extends RequestInit {
  /**
   * If true, the fetch will not execute automatically.
   * Useful for manual triggering (e.g., form submissions).
   */
  isManual?: boolean;
}

/**
 * A robust custom hook for fetching data.
 *
 * @template T - The expected type of the response data.
 * @param url - The endpoint URL to fetch from.
 * @param options - Optional configuration for the request (headers, method, etc.).
 * @returns An object containing the data, loading state, error, and a refetch function.
 *
 * @example
 * const { data, isLoading, error, refetch } = useFetch<User>('/api/users/1');
 */
export const useFetch = <T>(
  url: string,
  options: UseFetchOptions = {}
): FetchState<T> & { refetch: () => void } => {
  const { isManual = false, ...fetchOptions } = options;
  const [state, setState] = useState<FetchState<T>>({
    data: null,
    isLoading: !isManual,
    error: null,
  });

  // Use a ref to store the abort controller to avoid recreating it on every render
  const abortControllerRef = useRef<AbortController | null>(null);

  /**
   * Executes the fetch operation.
   * Wrapped in useCallback to maintain referential stability if passed around.
   */
  const fetchData = useCallback(async () => {
    // Prevent fetching if URL is missing
    if (!url) {
      return;
    }

    // Abort any existing request to avoid race conditions
    if (abortControllerRef.current) {
      abortControllerRef.current.abort();
    }

    // Create a new AbortController for this request
    abortControllerRef.current = new AbortController();
    const { signal } = abortControllerRef.current;

    setState((prev) => ({ ...prev, isLoading: true, error: null }));

    try {
      const response = await fetch(url, {
        ...fetchOptions,
        signal,
      });

      // Handle HTTP errors (e.g., 404, 500) explicitly
      if (!response.ok) {
        const error: ApiError = new Error(
          `HTTP Error: ${'$'}{response.statusText}`
        ) as ApiError;
        error.status = response.status;
        error.code = "HTTP_ERROR";
        throw error;
      }

      // Attempt to parse JSON
      const jsonData = (await response.json()) as T;

      setState({
        data: jsonData,
        isLoading: false,
        error: null,
      });
    } catch (err) {
      // Ignore errors caused by aborting (component unmount or rapid refetch)
      if (err instanceof Error && err.name === "AbortError") {
        return;
      }

      const apiError: ApiError =
        err instanceof Error
          ? (err as ApiError)
          : new Error("An unknown error occurred");

      setState({
        data: null,
        isLoading: false,
        error: apiError,
      });
    }
  }, [url, JSON.stringify(fetchOptions)]);

  useEffect(() => {
    if (!isManual) {
      fetchData();
    }

    // Cleanup function to abort request on unmount
    return () => {
      if (abortControllerRef.current) {
        abortControllerRef.current.abort();
      }
    };
  }, [fetchData, isManual]);

  return {
    ...state,
    refetch: fetchData,
  };
};"""
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
        viewModelScope.launch(Dispatchers.IO) {
            // 1. Add User Message ONLY
            val userMsg = AlaraChatUiModels(message = message, isUser = true)
            _messages.update { it + userMsg }

//            _messages.update {
//                it + AlaraChatUiModels(message =_markdownTest, isUser = false)
//            }

            // REMOVED: val botPlaceHolder = ... (Don't add empty bubble here)

            // 2. Show "Thinking" state
            _chatState.value = AlaraChatUiState.Loading

            try {
                val body = AlaraChatRequest(
                    agentId = "6970f8ed7f1e9a37b6507b90",
                    message = message,
                    sessionId = "6970f74b37273f8c908d8dbf-1769292512795",
                    stream = true
                )
                val response = chatRepo.sendMessage(body)
                val input = response.byteStream().bufferedReader()

                var line: String? = null
                var isFirstChunk = true // Track if we have started receiving text

                while (input.readLine().also { line = it } != null) {
                    val rawLine = line?.trim() ?: continue
                    if (rawLine.isEmpty() || !rawLine.startsWith("data:")) continue

                    val jsonString = rawLine.removePrefix("data:").trim()
                    if (jsonString == "[DONE]") break

                    try {
                        val rootObject = JSONObject(jsonString)
                        val dataObject = rootObject.getJSONObject("data")
                        val token = dataObject.getString("message")
                        val type = dataObject.getString("type")


                        // 3. Handle First Chunk vs Subsequent Chunks
                        if (isFirstChunk) {
                            // FIRST CHUNK:
                            // A. Create the message now (so no empty bubble appeared before)
                            val newBotMsg = AlaraChatUiModels(message = token, isUser = false)
                            _messages.update { it + newBotMsg }

                            // B. Hide "Thinking" indicator since we are now "Typing"
                            _chatState.value = AlaraChatUiState.Success("Streaming...")

                            isFirstChunk = false
                        } else {
                            // SUBSEQUENT CHUNKS:
                            // Append to the existing message
                            if (type == "End".lowercase()) {
                                _chatState.value = AlaraChatUiState.Success("Completed")
                                break
                            }
                            _messages.update { currentList ->
                                val updatedList = currentList.toMutableList()
                                val lastIndex = updatedList.lastIndex
                                val currentBotMessage = updatedList[lastIndex]

                                val newFullText = currentBotMessage.message + token
                                updatedList[lastIndex] =
                                    currentBotMessage.copy(message = newFullText)
                                updatedList
                            }
                        }
                    } catch (e: Exception) {
                        Log.e("StreamError", "Error parsing chunk", e)
                    }
                }
                _chatState.value = AlaraChatUiState.Success("Completed")
            } catch (e: Exception) {
                _chatState.value = AlaraChatUiState.Error(e.message.toString())
            }

//            _chatState.value = AlaraChatUiState.Success("Completed")
        }
    }

}