package com.sample.androidfundamental2.di

import android.content.Context
import com.sample.androidfundamental2.data.local.database.AppDatabase
import com.sample.androidfundamental2.data.local.preferences.SettingsPreferences
import com.sample.androidfundamental2.data.local.preferences.dataStore
import com.sample.androidfundamental2.data.remote.ApiConfig
import com.sample.androidfundamental2.data.repository.EventRepository

object Injection {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = AppDatabase.getInstance(context)
        val favoriteEventDao = database.favoriteEventDao()
        return EventRepository.getInstance(apiService, favoriteEventDao)
    }

    fun provideSettingsPreferences(context: Context): SettingsPreferences {
        return SettingsPreferences.getInstance(context.dataStore)
    }
}
