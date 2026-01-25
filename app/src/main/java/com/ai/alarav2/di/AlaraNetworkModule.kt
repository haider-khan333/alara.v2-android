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

    @Provides
    @Singleton
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val req = chain.request().newBuilder().addHeader(
            "Authorization",
            "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2OTcwZjc0YjM3MjczZjhjOTA4ZDhkYmYiLCJvcmdhbml6YXRpb24iOiI2OTQ4ZmZiMzNkNWI3ZGNhOTI1NDUyMzMiLCJwZXJtaXNzaW9uIjoiNjk0OTE2NzYzZDViN2RjYTkyNTQ2YWFiIiwiZmlyc3ROYW1lIjoiSGFpZGVyIiwidGVhbXMiOltdLCJsYXN0TmFtZSI6IktoYW4iLCJlbWFpbCI6ImhhaWRlci5raGFuQGlzc20uYWkiLCJpYXQiOjE3NjkzNTQ2MjQsImV4cCI6MTc2OTM1ODIyNH0.ujeL3rq2q_k_cijgdnVUY7IVL9amf2BgcDrPG9zSLEg")
            .build()

        chain.proceed(req)
    }


    @Provides
    @Singleton
    fun provideHttpClient(authInterceptor: Interceptor):
            OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
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