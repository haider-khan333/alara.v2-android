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
    "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJfaWQiOiI2OTcwZjc0YjM3MjczZjhjOTA4ZDhkYmYiLCJvcmdhbml6YXRpb24iOiI2OTQ4ZmZiMzNkNWI3ZGNhOTI1NDUyMzMiLCJwZXJtaXNzaW9uIjp7Il9pZCI6IjY5NDkxNjc2M2Q1YjdkY2E5MjU0NmFhYiIsIm5hbWUiOiJTdXBlci1BZG1pbiIsIm1vZHVsZVBlcm1pc3Npb25zIjpbeyJtb2R1bGVJZCI6eyJfaWQiOiI2OTcwNzE3NjM3MjczZjhjOTA4YzIxODQiLCJsYWJlbCI6IkFsYXJhVjIgLSBDb3BpbG90IiwidmFsdWUiOiJhbGFyYS12Mi1jb3BpbG90IiwiZGVzY3JpcHRpb24iOiIifSwiYWN0aW9ucyI6WyJjcmVhdGUiLCJyZWFkIiwidXBkYXRlIiwiZGVsZXRlIl19LHsibW9kdWxlSWQiOnsiX2lkIjoiNjk3MDcxNzYzNzI3M2Y4YzkwOGMyMTg1IiwibGFiZWwiOiJBbGFyYVYyIC0gQ3VzdG9tIEFnZW50cyIsInZhbHVlIjoiYWxhcmEtdjItY3VzdG9tLWFnZW50cyIsImRlc2NyaXB0aW9uIjoiIn0sImFjdGlvbnMiOlsicmVhZCIsImNyZWF0ZSIsInVwZGF0ZSIsImRlbGV0ZSJdfSx7Im1vZHVsZUlkIjp7Il9pZCI6IjY5NzA3MTc2MzcyNzNmOGM5MDhjMjE4NiIsImxhYmVsIjoiQWxhcmFWMiAtIENYICYgU2FsZXMiLCJ2YWx1ZSI6ImFsYXJhLXYyLWN4LXNhbGVzIiwiZGVzY3JpcHRpb24iOiIifSwiYWN0aW9ucyI6WyJyZWFkIiwiY3JlYXRlIiwidXBkYXRlIiwiZGVsZXRlIl19LHsibW9kdWxlSWQiOnsiX2lkIjoiNjk3MDcxNzYzNzI3M2Y4YzkwOGMyMTg3IiwibGFiZWwiOiJBbGFyYVYyIC0gSGl2ZSIsInZhbHVlIjoiYWxhcmEtdjItaGl2ZSIsImRlc2NyaXB0aW9uIjoiIn0sImFjdGlvbnMiOlsidXBkYXRlIiwicmVhZCIsImNyZWF0ZSIsImRlbGV0ZSJdfSx7Im1vZHVsZUlkIjp7Il9pZCI6IjY5NzA3MTc2MzcyNzNmOGM5MDhjMjE4OCIsImxhYmVsIjoiQWxhcmFWMiAtSm91cm5leXMiLCJ2YWx1ZSI6ImFsYXJhLXYyLWpvdXJuZXlzIiwiZGVzY3JpcHRpb24iOiIifSwiYWN0aW9ucyI6WyJkZWxldGUiLCJ1cGRhdGUiLCJyZWFkIiwiY3JlYXRlIl19LHsibW9kdWxlSWQiOnsiX2lkIjoiNjk3MDcxNzYzNzI3M2Y4YzkwOGMyMTg5IiwibGFiZWwiOiJBbGFyYVYyIC0gUmVhc29uaW5nIEFnZW50cyIsInZhbHVlIjoiYWxhcmEtdjItcmVhc29uaW5nLWFnZW50cyIsImRlc2NyaXB0aW9uIjoiIn0sImFjdGlvbnMiOlsiY3JlYXRlIiwicmVhZCIsInVwZGF0ZSIsImRlbGV0ZSJdfSx7Im1vZHVsZUlkIjp7Il9pZCI6IjY5NzA3MTc2MzcyNzNmOGM5MDhjMjE4YSIsImxhYmVsIjoiQWxhcmFWMiAtIFNvZnR3YXJlIEVuZ2luZWVyaW5nIiwidmFsdWUiOiJhbGFyYS12Mi1zb2Z0d2FyZS1lbmciLCJkZXNjcmlwdGlvbiI6IiJ9LCJhY3Rpb25zIjpbImNyZWF0ZSIsInJlYWQiLCJ1cGRhdGUiLCJkZWxldGUiXX0seyJtb2R1bGVJZCI6eyJfaWQiOiI2OTcwNzE3NjM3MjczZjhjOTA4YzIxOGIiLCJsYWJlbCI6IkFsYXJhVjIgLSBUaGUgV2F0Y2h0b3dlciIsInZhbHVlIjoiYWxhcmEtdjItd2F0Y2h0b3dlciIsImRlc2NyaXB0aW9uIjoiIn0sImFjdGlvbnMiOlsiZGVsZXRlIiwidXBkYXRlIiwicmVhZCIsImNyZWF0ZSJdfSx7Im1vZHVsZUlkIjp7Il9pZCI6IjY5NzA3MTc2MzcyNzNmOGM5MDhjMjE4YyIsImxhYmVsIjoiQ29udHJvbHMiLCJ2YWx1ZSI6ImFsYXJhLXYyLXdhdGNodG93ZXItY29udHJvbHMiLCJkZXNjcmlwdGlvbiI6IiJ9LCJhY3Rpb25zIjpbImNyZWF0ZSIsInJlYWQiLCJ1cGRhdGUiLCJkZWxldGUiXX0seyJtb2R1bGVJZCI6eyJfaWQiOiI2OTcwNzE3NjM3MjczZjhjOTA4YzIxOGQiLCJsYWJlbCI6Ikluc2lnaHRzIiwidmFsdWUiOiJhbGFyYS12Mi13YXRjaHRvd2VyLWluc2lnaHRzIiwiZGVzY3JpcHRpb24iOiIifSwiYWN0aW9ucyI6WyJkZWxldGUiLCJ1cGRhdGUiLCJyZWFkIiwiY3JlYXRlIl19LHsibW9kdWxlSWQiOnsiX2lkIjoiNjk3MDcxNzYzNzI3M2Y4YzkwOGMyMThlIiwibGFiZWwiOiJQcm9kdWN0aXZpdHkiLCJ2YWx1ZSI6ImFsYXJhLXYyLXdhdGNodG93ZXItcHJvZHVjdGl2aXR5IiwiZGVzY3JpcHRpb24iOiIifSwiYWN0aW9ucyI6WyJjcmVhdGUiLCJyZWFkIiwidXBkYXRlIiwiZGVsZXRlIl19LHsibW9kdWxlSWQiOnsiX2lkIjoiNjk3MDcxNzYzNzI3M2Y4YzkwOGMyMThmIiwibGFiZWwiOiJBZ2VudCBTd2FybSIsInZhbHVlIjoiYWxhcmEtdjItd2F0Y2h0b3dlci1zd2FybSIsImRlc2NyaXB0aW9uIjoiIn0sImFjdGlvbnMiOlsiZGVsZXRlIiwidXBkYXRlIiwicmVhZCIsImNyZWF0ZSJdfV0sImxldmVsIjoiZnVsbC1hY2Nlc3MiLCJkZXNjcmlwdGlvbiI6IkNhbiBkbyBhbnl0aGluZyJ9LCJmaXJzdE5hbWUiOiJIYWlkZXIiLCJ0ZWFtcyI6W10sImxhc3ROYW1lIjoiS2hhbiIsImVtYWlsIjoiaGFpZGVyLmtoYW5AaXNzbS5haSIsImlhdCI6MTc2OTU0NTQ1NSwiZXhwIjoxNzY5NTQ5MDU1fQ.shfPcJknErcatiYqfNaOUJvdDTxNNTEL6Z7oc_Gj4LM"
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