package com.javix.wordflipster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class OverViewModel(application: Application): AndroidViewModel(application) {
    val dataStoreManager = DataStoreManager(application)
    private var _time = MutableStateFlow(0)
    val time: Int = _time.value

    private var _correctWords = MutableStateFlow(0)
    val correctWord: Int = _correctWords.value

    private var _totalWords = MutableStateFlow(0)
    val totalWords: StateFlow<Int> = _totalWords
    init {
        viewModelScope.launch {
            _time.value = dataStoreManager.minuteCountFlow.first() * 60
            _correctWords.value = dataStoreManager.totalCorrectWords.first()
            _totalWords.value = dataStoreManager.totalWords.first()
        }
    }
}