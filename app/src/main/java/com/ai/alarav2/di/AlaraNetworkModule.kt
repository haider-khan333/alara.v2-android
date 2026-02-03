package com.ai.alarav2.di

import com.ai.alarav2.data.remote.AlaraChatApi
import com.ai.alarav2.data.remote.AlaraChatHistoryApi
import com.ai.alarav2.data.remote.AlaraGetAgentApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AlaraNetworkModule {

    private val baseUrl = "https://alara-agents-staging.fintra.ai/app/api/v1/"

    @Provides
    @Singleton
    @AlaraNetwork
    fun provideAuthInterceptor(tokenManager: AlaraTokenManager): Interceptor =
        Interceptor { chain ->
            val originalRequest = chain.request()

            val token = tokenManager.getAccessTokenSync()

            val newRequest = if (token != null) {
                originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer $token")
                    .build()
            } else {
                originalRequest
            }

            chain.proceed(newRequest)
        }


    @Provides
    @Singleton
    @AlaraNetwork
    fun provideHttpClient(@AlaraNetwork authInterceptor: Interceptor):
            OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .callTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @AlaraNetwork
    fun provideRetrofitClient(@AlaraNetwork okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideChatApi(@AlaraNetwork retrofit: Retrofit): AlaraChatApi =
        retrofit.create(AlaraChatApi::class.java)

    @Provides
    @Singleton
    fun provideChatHistoryApi(@AlaraNetwork retrofit: Retrofit): AlaraChatHistoryApi =
        retrofit.create(AlaraChatHistoryApi::class.java)
    @Provides
    @Singleton
    fun provideAlaraAgentApi(@AlaraNetwork retrofit: Retrofit): AlaraGetAgentApi =
        retrofit.create(AlaraGetAgentApi::class.java)



}