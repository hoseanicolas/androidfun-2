package com.sample.androidfundamental2.ui.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.sample.androidfundamental2.data.local.preferences.SettingsPreferences
import kotlinx.coroutines.launch

class SettingsViewModel(private val preferences: SettingsPreferences) : ViewModel() {

    fun getThemeSettings(): LiveData<Boolean> {
        return preferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            preferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getNotificationSettings(): LiveData<Boolean> {
        return preferences.getNotificationSetting().asLiveData()
    }

    fun saveNotificationSetting(isNotificationActive: Boolean) {
        viewModelScope.launch {
            preferences.saveNotificationSetting(isNotificationActive)
        }
    }
}
