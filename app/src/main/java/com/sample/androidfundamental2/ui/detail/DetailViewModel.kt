package com.sample.androidfundamental2.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.androidfundamental2.data.model.Event
import com.sample.androidfundamental2.data.repository.EventRepository
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: EventRepository) : ViewModel() {

    private val _event = MutableLiveData<Event?>()
    val event: LiveData<Event?> = _event

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun loadEventDetail(eventId: Int) {
        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val result = repository.getEventDetail(eventId)
                if (result.isSuccess) {
                    _event.value = result.getOrNull()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Failed to load event detail"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun isEventFavorite(eventId: Int): LiveData<Boolean> {
        return repository.isEventFavorite(eventId)
    }

    fun toggleFavorite(event: Event, isFavorite: Boolean) {
        viewModelScope.launch {
            try {
                if (isFavorite) {
                    repository.deleteFavoriteEventById(event.id)
                } else {
                    repository.insertFavoriteEvent(event)
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Failed to update favorite"
            }
        }
    }
}
