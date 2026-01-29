package com.ai.alarav2.repository.chat

import android.util.Log
import com.ai.alarav2.data.models.req.AlaraChatRequest
import com.ai.alarav2.data.remote.AlaraChatApi
import com.ai.alarav2.vm.chat.AlaraChatError
import com.ai.alarav2.vm.chat.AlaraChatResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.ResponseBody
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject

class AlaraChatImpl @Inject constructor(
    private val api: AlaraChatApi
) : AlaraChatRepo {
    override suspend fun sendMessage(request: AlaraChatRequest): AlaraChatResult {
        return try {
            val resp: Response<ResponseBody> = api.sendMessage(request)

            if (!resp.isSuccessful) {
                val errBody = resp.errorBody()?.string()
                return AlaraChatResult.Failure(AlaraChatError.Http(resp.code(), errBody))
            }

            val body = resp.body()
                ?: return AlaraChatResult.Failure(
                    AlaraChatError.Http(
                        resp.code(),
                        "Empty body"
                    )
                )

            AlaraChatResult.Stream(lines = sseLines(body))
        } catch (e: IOException) {
            AlaraChatResult.Failure(AlaraChatError.Network(message = e.message))
        } catch (e: Exception) {
            AlaraChatResult.Failure(AlaraChatError.Unknown(message = e.message))
        }
    }

    private fun sseLines(body: ResponseBody): Flow<String> = flow {
        Log.d("TAG", "sseLines: streaming started")
        body.use { responseBody ->
            val reader = responseBody.charStream().buffered()

            while (true) {
                val line = reader.readLine() ?: break
                Log.d("SSE", "line=${line.take(120)}")

                emit(line)
            }
        }
    }.flowOn(Dispatchers.IO)

}