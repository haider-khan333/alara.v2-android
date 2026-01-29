package com.ai.alarav2.di

import com.ai.alarav2.data.remote.AlaraLoginApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class PolyXNetworkModule {

    private val ployXUrl = "https://alara-agents-prod.fintra.ai/poly-x/poly-auth/v1/auth/"


    @Provides
    @Singleton
    @PolyXNetwork
    fun providePolyXHttpClient():
            OkHttpClient = OkHttpClient.Builder()
        .callTimeout(120, TimeUnit.SECONDS)
        .readTimeout(120, TimeUnit.SECONDS)
        .writeTimeout(120, TimeUnit.SECONDS)
        .build()

    @Provides
    @Singleton
    @PolyXNetwork
    fun providePolyXRetrofitClient(@PolyXNetwork okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(ployXUrl).client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun providePolyXLoginApi(@PolyXNetwork retrofit: Retrofit): AlaraLoginApi =
        retrofit.create(AlaraLoginApi::class.java)

}