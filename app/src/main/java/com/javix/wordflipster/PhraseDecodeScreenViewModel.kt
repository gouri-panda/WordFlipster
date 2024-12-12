package com.javix.wordflipster

import android.app.Application
import android.content.Context
import androidx.compose.ui.text.style.LineBreak.WordBreak.Companion.Phrase
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PhraseDecodeScreenViewModel(application: Application) : AndroidViewModel(application) {
    // Live data or state holding the list of phrases
    private val _phrases = MutableStateFlow<List<Phrase>>(emptyList())
    val phrases: StateFlow<List<Phrase>> = _phrases

    init {
        // Load phrases from the JSON file or database
        loadPhrases(application.applicationContext)
    }

    private fun loadPhrases(context: Context) {
        // Simulate loading data from a JSON file or API
            viewModelScope.launch {
                val json = context.assets.open("clues.json").bufferedReader().use { it.readText() }
                val type = object : TypeToken<List<List<String>>>() {}.type
                val phraseList: List<List<String>> = Gson().fromJson(json, type)

                // Map and select random 15 phrases
                _phrases.value = phraseList
                    .map { Phrase(it[0], it[1]) }
                    .distinct()
                    .shuffled()
                    .take(15)
            }
        }

    // Function to generate a quote based on valid phrases
    fun generateQuote(phrases: List<Phrase>): String {
        val quoteBuilder = StringBuilder("DREAM BIG AND DARE TO FAIL CAT")

        // Collect all the unique letters from the answers
        val answerLetters = phrases.flatMap { it.answer.toUpperCase().toList() }.toSet()

        // Ensure all answer letters are in the quote (we assume the quote already contains some letters)
        answerLetters.forEach { letter ->
            if (quoteBuilder.indexOf(letter) == -1) {
                // Add the missing letter to the quote
                quoteBuilder.append(letter)
            }
        }

        return quoteBuilder.toString()
    }
    // Function to get valid phrases where all answer letters are in the quote
    fun getValidPhrases(phrases: List<Phrase>): List<Phrase> {
        // Select 15 random phrases from the list
        val randomPhrases = phrases.shuffled().take(15)

        // Remove duplicates by converting the list to a set
        val uniquePhrases = randomPhrases.toSet()

        val validPhrases = mutableListOf<Phrase>()
        var validLetters = mutableSetOf<Char>()

        for (phrase in uniquePhrases) {
            val answerLetters = phrase.answer.toUpperCase().toSet()
            if (validLetters.containsAll(answerLetters)) {
                validPhrases.add(phrase)
                validLetters.addAll(answerLetters)
            }
        }

        return validPhrases
    }
}
data class Phrase(
    val clue: String,
    val answer: String
) {
    // Override equals and hashCode to compare both clue and answer
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        other as Phrase
        return clue == other.clue && answer == other.answer
    }

    override fun hashCode(): Int {
        return 31 * clue.hashCode() + answer.hashCode()
    }
}
