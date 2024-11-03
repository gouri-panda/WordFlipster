package com.javix.wordflipster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class ChallengeViewModel(application: Application) : AndroidViewModel(application) {
    private val challengeDao = WordFlipsterDatabase.getDatabase(application).challengeDao()
    private val repository = ChallengeRepository(challengeDao)

    private val _challenges = MutableLiveData<List<Challenge>>()
    val challenges: LiveData<List<Challenge>> = _challenges

    fun addChallenge(challenge: Challenge) {
        viewModelScope.launch {
            repository.addChallenge(
                ChallengeEntity(wordsSolved = challenge.wordsSolved, totalWords = challenge.totalWords, timeTaken = challenge.timeTaken, date = challenge.date ?: Date())
            )
            loadChallenges()
        }
    }

    fun loadChallenges() {
        viewModelScope.launch {
            val challengeEntities = repository.getChallenges()
            _challenges.value = challengeEntities.map {
                Challenge(it.wordsSolved, it.totalWords, it.timeTaken, date = it.date)
            }
        }
    }
}
