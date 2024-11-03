package com.javix.wordflipster

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class SettingViewModel(application: Application): AndroidViewModel(application) {
    private val dataStoreManager = DataStoreManager(application)
    private val _musicEnabled = MutableStateFlow(true)
    val musicEnabled: StateFlow<Boolean> = _musicEnabled
    private val _vibrationEnabled = MutableStateFlow(true)
    val vibrationEnabled: StateFlow<Boolean> = _vibrationEnabled

    init {
        viewModelScope.launch {
            _musicEnabled.value = dataStoreManager.musicEnabledFlow.first()
            _vibrationEnabled.value = dataStoreManager.vibrationEnabledFlow.first()
        }
        collectVibrationEnabled()
        collectMusicEnabled()
    }

    fun collectVibrationEnabled(){
        viewModelScope.launch {
            dataStoreManager.vibrationEnabledFlow.collect { isEnabled ->
                _vibrationEnabled.value = isEnabled
            }
        }
    }
    fun collectMusicEnabled() {
        viewModelScope.launch {
            dataStoreManager.musicEnabledFlow.collect { isEnabled ->
                _musicEnabled.value = isEnabled
            }
        }
    }


    fun saveMusicPreference(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveMusicPreference(isEnabled)
        }
    }
    fun saveVibrationPreference(isEnabled: Boolean) {
        viewModelScope.launch {
            dataStoreManager.saveVibrationPreference(isEnabled)
        }
    }
}