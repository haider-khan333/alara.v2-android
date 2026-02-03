package com.ai.alarav2.di

import com.ai.alarav2.repository.chat.AlaraChatImpl
import com.ai.alarav2.repository.chat.AlaraChatRepo
import com.ai.alarav2.repository.chathistory.AlaraChatHistoryImpl
import com.ai.alarav2.repository.chathistory.AlaraChatHistoryRepo
import com.ai.alarav2.repository.getagent.AlaraGetAgentImpl
import com.ai.alarav2.repository.getagent.AlaraGetAgentRepo
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

    @Binds
    @Singleton
    abstract fun bindAlaraGetAgentRepo(
        impl: AlaraGetAgentImpl
    ): AlaraGetAgentRepo

    @Binds
    @Singleton
    abstract fun bindAlaraChatHistoryRepo(
        impl: AlaraChatHistoryImpl
    ): AlaraChatHistoryRepo


}