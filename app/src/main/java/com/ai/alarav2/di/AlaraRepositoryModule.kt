package com.ai.alarav2.di

import com.ai.alarav2.repository.chat.AlaraChatImpl
import com.ai.alarav2.repository.chat.AlaraChatRepo
import com.ai.alarav2.repository.login.AlaraLoginImpl
import com.ai.alarav2.repository.login.AlaraLoginRepo
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AlaraRepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAlaraChatRepo(
        impl: AlaraChatImpl
    ): AlaraChatRepo

    @Binds
    @Singleton
    abstract fun bindAlaraLoginRepo(
        impl: AlaraLoginImpl
    ): AlaraLoginRepo


}