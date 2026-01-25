package com.ai.alarav2.di

import com.ai.alarav2.repository.AlaraChatImpl
import com.ai.alarav2.repository.AlaraChatRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.http.Streaming
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlaraRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlaraChatRepo(
        impl: AlaraChatImpl
    ): AlaraChatRepo


}