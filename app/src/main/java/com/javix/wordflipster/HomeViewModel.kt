package com.javix.wordflipster

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class HomeViewModel(application: Application, category: String?, onChallengeCompleteListener: (Challenge?) -> Unit) :
    AndroidViewModel(application) {
    val dataStoreManager = DataStoreManager(application)
    private val challengeDao = WordFlipsterDatabase.getDatabase(application).challengeDao()


     val _remainingTime = MutableStateFlow(60) // Initialize with default 60 seconds
    val remainingTime: StateFlow<Int> = _remainingTime

    val currentWordIndex  = MutableStateFlow(0)

    private val _totalTime = MutableStateFlow(60)

    private val _totalWords = MutableStateFlow(0)
    val totalWords: StateFlow<Int> = _totalWords

    private val _wordsSolved = MutableStateFlow(0)
    val wordsSolved: StateFlow<Int> = _wordsSolved

    private val _letterCount = MutableStateFlow(2)
    val letterCount: StateFlow<Int> = _letterCount

    var isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)

    val _wordsList = MutableStateFlow(getWordsListFrom(category))

    private val _vibration = MutableStateFlow(true)
    val vibration: StateFlow<Boolean> = _vibration

    private var _challenge = MutableStateFlow<Challenge?>(null)



    init {
        Log.d("HomeViewModel", "init block called")
        viewModelScope.launch {
            // Collect minuteCountFlow and update _totalTime
            _remainingTime.value = dataStoreManager.minuteCountFlow.first() * 60
            _totalTime.value = dataStoreManager.minuteCountFlow.first() * 60
            _letterCount.value = dataStoreManager.letterCountFlow.first()
            _vibration.value = dataStoreManager.vibrationEnabledFlow.first()
            _wordsList.value = getWordsListFrom(category).also {
                isLoading.value = false
            }

            dataStoreManager.totalWords.collect { total ->
                _totalWords.value = total
            }
            dataStoreManager.totalCorrectWords.collect { solved ->
                Log.d("HomeViewModel", "Collected correct words: $solved")
                _wordsSolved.value = solved
            }

            viewModelScope.launch {
                dataStoreManager.letterCountFlow.collect { count ->
                    Log.d("HomeViewModel", "Collected letter count: $count") // Debug line

                    _letterCount.value = count
                    _wordsList.value = getWordsListFromCount(count)

                }

            }
        }

        // Start the countdown timer
        viewModelScope.launch {
            while (_remainingTime.value > 0) {
                delay(1000L) // 1 second delay
                _remainingTime.value -= 1
            }
            if (_remainingTime.value.equals(0)) {
                finishGame(onChallengeCompleteListener)
            }
        }

        viewModelScope.launch {
            currentWordIndex.collect { currentWordIndex ->
                if (currentWordIndex  == _wordsList.value.size - 1) { // Todo: This is temp fix . we still don't see the last word
                    finishGame(onChallengeCompleteListener)
                }
            }
        }

    }

    private fun finishGame(onChallengeCompleteListener: (Challenge?) -> Unit) {
        saveChallengeEntityToDatabase()
        val challenge = createChallenge()
        _challenge.value = challenge
        onChallengeCompleteListener.invoke(_challenge.value)
    }

    fun updateCorrectWords(correctWords: Int) {
        _wordsSolved.value = correctWords
    }

    fun updateTotalWords(totalWords: Int) {
        _totalWords.value = totalWords
    }
    fun isVibrationEnabled() = _vibration.value

    fun createChallengeEntity(): ChallengeEntity {
        return ChallengeEntity(
            wordsSolved = wordsSolved.value,
            totalWords = totalWords.value,
            timeTaken = remainingTime.value.toLong(),
            date = Date()
        )
    }

    fun saveChallengeEntityToDatabase() {
        viewModelScope.launch {
            challengeDao.insertChallenge(createChallengeEntity())
        }
    }

    fun createChallenge(): Challenge? {
        _challenge = MutableStateFlow(
            Challenge(
                wordsSolved = wordsSolved.value,
                totalWords = totalWords.value,
                timeTaken = _totalTime.value.toLong(),
                date = Date()
            )
        )
        return _challenge.value
    }


    fun getWordsListFromCount(count: Int): List<String> {
        Log.d("HomeViewModel", "Getting words list for letter count: $count") // Debug line
        return when (count) {
            2 -> twoLetterWords
            3 -> threeLetterWords
            4 -> fourLetterWords
            5 -> fiveLetterWords
            else -> twoLetterWords
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

    fun getWordsListFrom(category: String?): List<String> {
        return if (category != null && category != "") {
            categoriesWithWords[category]?.shuffled() ?: twoLetterWords.shuffled()
        } else getWordsListFromCount(letterCount.value).shuffled()
    }

}

class HomeViewModelFactory(
    private val context: Context,
    private val category: String?,
    private val onChallengeCompleteListener: (Challenge?) -> Unit
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(
                context as Application,
                category = category,
                onChallengeCompleteListener = onChallengeCompleteListener
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

