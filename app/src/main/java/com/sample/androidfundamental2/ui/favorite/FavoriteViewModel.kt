package com.sample.androidfundamental2.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.androidfundamental2.data.local.entity.FavoriteEvent
import com.sample.androidfundamental2.data.repository.EventRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {

    val favoriteEvents: LiveData<List<FavoriteEvent>> = repository.getAllFavoriteEvents()

    fun deleteFavoriteEvent(eventId: Int) {
        viewModelScope.launch {
            try {
                repository.deleteFavoriteEventById(eventId)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun restoreFavoriteEvent(favoriteEvent: FavoriteEvent) {
        viewModelScope.launch {
            try {
                repository.restoreFavoriteEvent(favoriteEvent)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    suspend fun getFavoriteEventById(eventId: Int): FavoriteEvent? {
        return repository.getFavoriteEventById(eventId)
    }
}
