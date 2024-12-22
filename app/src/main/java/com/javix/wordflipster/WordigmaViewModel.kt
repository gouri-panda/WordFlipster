package com.javix.wordflipster

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import java.util.Date

class WordigmaViewModel (application: Application) : AndroidViewModel(application) {
    val challengeDao = WordFlipsterDatabase.getDatabase(application).quotesDao()
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
}