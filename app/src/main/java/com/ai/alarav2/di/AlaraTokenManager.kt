package com.ai.alarav2.di

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore by preferencesDataStore(name = "auth_prefs")

@Singleton
class AlaraTokenManager @Inject constructor(
    @param:ApplicationContext private val context: Context
) {
    companion object {
        private val ACCESS_TOKEN_KEY = stringPreferencesKey("access_token")
        private val REFRESH_TOKEN_KEY = stringPreferencesKey("refresh_token")
    }

    suspend fun saveTokens(accessToken: String, refreshToken: String) {
        context.dataStore.edit { prefs ->
            prefs[ACCESS_TOKEN_KEY] = accessToken
            prefs[REFRESH_TOKEN_KEY] = refreshToken
        }
    }

    fun getAccessTokenSync(): String? {
        return runBlocking {
            context.dataStore.data.first()[ACCESS_TOKEN_KEY]
        }
    }

    fun getRefreshTokenSync(): String? {
        return runBlocking {
            context.dataStore.data.first()[REFRESH_TOKEN_KEY]
        }
    }

    suspend fun clearTokens() {
        context.dataStore.edit { it.clear() }
    }
}