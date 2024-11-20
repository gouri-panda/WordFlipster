package com.javix.wordflipster

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.javix.wordflipster.data.threeWordChains
import com.javix.wordflipster.ui.theme.WordChainLevel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Locale


class WordChainMainScreenViewModel(application: Application, level: WordChainLevel, onChallengeCompleteListener: (Challenge?) -> Unit) :
    AndroidViewModel(application) {
    val dataStoreManager = DataStoreManager(application)

    val _remainingTime = MutableStateFlow(60) // Initialize with default 60 seconds
    val remainingTime: StateFlow<Int> = _remainingTime

    val _wordsList = MutableStateFlow(getWordsKeyListFrom(level))

    init {

        viewModelScope.launch {
            // Collect minuteCountFlow and update _totalTime
            _remainingTime.value = dataStoreManager.minuteCountFlow.first() * 60
        }
    }

    fun getWordsKeyListFrom(level: WordChainLevel):  List<String> {
        return when (level) {
            WordChainLevel.Easy -> threeWordChains.keys.shuffled()
            WordChainLevel.Medium -> threeWordChains.keys.shuffled()
            WordChainLevel.Hard -> threeWordChains.keys.shuffled()
        }
    }
    fun getCharList(): List<List<String>> {
        return _wordsList.value.map { word ->
            word.map {
                it.toString().uppercase(
                    Locale.getDefault()
                )
            }
        }
    }
}

class WordChainMainScreenViewModelFactory(
    private val context: Context,
    private val level: WordChainLevel,
    private val onChallengeCompleteListener: (Challenge?) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(WordChainMainScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return WordChainMainScreenViewModel(
                context as Application,
                level = level,
                onChallengeCompleteListener = onChallengeCompleteListener
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}


