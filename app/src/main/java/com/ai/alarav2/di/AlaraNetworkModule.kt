package com.ai.alarav2.di

import com.ai.alarav2.data.remote.AlaraChatApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AlaraNetworkModule {

    private val baseUrl = "https://alara-agents-staging.fintra.ai/app/api/v1/"
    private val token =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2OTcwZjc0YjM3MjczZjhjOTA4ZDhkYmYiLCJvcmdhbml6YXRpb24iOiI2OTQ4ZmZiMzNkNWI3ZGNhOTI1NDUyMzMiLCJwZXJtaXNzaW9uIjoiNjk0OTE2NzYzZDViN2RjYTkyNTQ2YWFiIiwiZmlyc3ROYW1lIjoiSGFpZGVyIiwidGVhbXMiOltdLCJsYXN0TmFtZSI6IktoYW4iLCJlbWFpbCI6ImhhaWRlci5raGFuQGlzc20uYWkiLCJpYXQiOjE3NjkzNzc3NzEsImV4cCI6MTc2OTM4MTM3MX0.WEfuMfKtNPbwWON8Cznc0ncUihoZCXOypibQm_WvhGg"
    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val req = chain.request().newBuilder().addHeader(
            "Authorization",
            "Bearer $token"
        )
            .build()

        chain.proceed(req)
    }


    @Provides
    @Singleton
    fun provideHttpClient(authInterceptor: Interceptor):
            OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .callTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(120, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    fun provideRetrofitClient(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(baseUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideChatApi(retrofit: Retrofit): AlaraChatApi =
        retrofit.create(AlaraChatApi::class.java)


}