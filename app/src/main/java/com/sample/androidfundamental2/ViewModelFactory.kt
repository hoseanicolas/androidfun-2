package com.sample.androidfundamental2

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sample.androidfundamental2.data.local.preferences.SettingsPreferences
import com.sample.androidfundamental2.data.repository.EventRepository
import com.sample.androidfundamental2.di.Injection
import com.sample.androidfundamental2.ui.detail.DetailViewModel
import com.sample.androidfundamental2.ui.favorite.FavoriteViewModel
import com.sample.androidfundamental2.ui.finished.FinishedViewModel
import com.sample.androidfundamental2.ui.home.HomeViewModel
import com.sample.androidfundamental2.ui.search.SearchViewModel
import com.sample.androidfundamental2.ui.settings.SettingsViewModel
import com.sample.androidfundamental2.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(
    private val eventRepository: EventRepository,
    private val settingsPreferences: SettingsPreferences
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(SettingsViewModel::class.java) -> {
                SettingsViewModel(settingsPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                instance ?: ViewModelFactory(
                    Injection.provideRepository(context),
                    Injection.provideSettingsPreferences(context)
                ).also { instance = it }
            }
    }
}
