package com.sample.androidfundamental2.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sample.androidfundamental2.data.model.Event
import com.sample.androidfundamental2.data.repository.EventRepository
import kotlinx.coroutines.launch

class SearchViewModel(private val repository: EventRepository) : ViewModel() {
    
    private val _events = MutableLiveData<List<Event>>()
    val events: LiveData<List<Event>> = _events
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage
    
    fun searchEvents(keyword: String) {
        if (keyword.isEmpty()) {
            _events.value = emptyList()
            return
        }
        
        _isLoading.value = true
        _errorMessage.value = null
        
        viewModelScope.launch {
            try {
                val result = repository.searchEvents(keyword)
                if (result.isSuccess) {
                    _events.value = result.getOrNull() ?: emptyList()
                } else {
                    _errorMessage.value = result.exceptionOrNull()?.message ?: "Search failed"
                }
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "An error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun clearSearch() {
        _events.value = emptyList()
        _errorMessage.value = null
    }
}
