package com.sample.androidfundamental2.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.androidfundamental2.data.model.Event
import com.sample.androidfundamental2.data.repository.EventRepository
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: EventRepository) : ViewModel() {
    
    private val _upcomingEvents = MutableLiveData<List<Event>>()
    val upcomingEvents: LiveData<List<Event>> = _upcomingEvents
    
    private val _finishedEvents = MutableLiveData<List<Event>>()
    val finishedEvents: LiveData<List<Event>> = _finishedEvents
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    fun loadHomeData() {
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            try {
                val upcomingResult = repository.getUpcomingEvents(limit = 5)
                val finishedResult = repository.getFinishedEvents(limit = 5)
                
                if (upcomingResult.isSuccess && finishedResult.isSuccess) {
                    _upcomingEvents.value = upcomingResult.getOrNull() ?: emptyList()
                    _finishedEvents.value = finishedResult.getOrNull() ?: emptyList()
                } else {
                    val error = upcomingResult.exceptionOrNull() ?: finishedResult.exceptionOrNull()
                    _errorMessage.value = error?.message ?: "Failed to load events"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
