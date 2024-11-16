package com.javix.wordflipster

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


// ViewModel for checking if a word is valid from the dictionary
class WordChainViewModel(private val application: Application) : AndroidViewModel(application) {


    val currentWord = mutableStateOf("bat")
    var movesLeft = mutableStateOf(5)
    var timeLeft = mutableStateOf(30)
    var score = mutableStateOf(0)
    var hint = mutableStateOf("")
    // Function to check if a word exists in the sorted dictionary file
    fun checkWordValidity(word: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isValid = withContext(Dispatchers.IO) {
                isValidWordFromFile(application.applicationContext, word)
            }

            // Pass the result back to the caller (e.g., UI) using the callback
            onResult(isValid)
        }
    }

    // Function to check if a word is valid by searching through the dictionary file
    fun isValidWordFromFile(context: Context, word: String): Boolean {
        return try {
            // Open the dictionary file from assets
            val inputStream = context.assets.open("dictionary.txt")
            val reader = BufferedReader(InputStreamReader(inputStream))

            // Read all lines of the dictionary file into a list
            val lines = reader.readLines()

            // Perform binary search on the list of words
            val index = lines.binarySearch { it.trim().compareTo(word, ignoreCase = true) }

            // Return true if the word was found (index >= 0 means found)
            index >= 0
        } catch (e: Exception) {
            // Handle any errors (e.g., file not found, read error)
            Log.e("WordViewModel", "Error reading dictionary file: ${e.message}")
            false
        }
    }
}
