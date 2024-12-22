package com.javix.wordflipster

import android.app.Application
import android.content.Context
import androidx.compose.runtime.remember
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class WordigmaViewModel (application: Application, val dataStoreManager: DataStoreManager) : AndroidViewModel(application) {

    val challengeDao = WordFlipsterDatabase.getDatabase(application).quotesDao()
    private val _level = MutableStateFlow(1)
    val level: StateFlow<Int> = _level

    fun getCurrentLevel() {
        viewModelScope.launch {
            dataStoreManager.quoteLevel.collect { level ->
                _level.value = level
            }
        }
    }

    fun createChallengeEntity(level: Level) {
        viewModelScope.launch {
            challengeDao.insertChallenge(QuoteEntity(
                quote = level.details.quote,
                author = level.details.title,
                birth = level.details.born,
                death = level.details.death,
                date = Date(),
                gameType = wordigmaScreen
            ))
        }
    }
    fun saveLevel(level: Int) {
        viewModelScope.launch {
            dataStoreManager.saveQuoteLevel(level)
        }
    }
    fun updateLevel(level: Int) {
        _level.value = level
    }
}
class WordgimaViewModelFactory(private val context: Context,
                               private val dataStoreManager: DataStoreManager) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(WordigmaViewModel::class.java)) {
                return WordigmaViewModel(context.applicationContext as Application,
                    dataStoreManager = dataStoreManager) as T
            }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
                               }