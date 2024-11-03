package com.javix.wordflipster

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "settings")

class DataStoreManager(context: Context) {
    private val dataStore = context.dataStore

    companion object {
        val LETTER_COUNT_KEY = intPreferencesKey("letter_count")
        val MINUTE_COUNT_KEY = intPreferencesKey("minute_count")
        val TOTAL_WORDS_KEY = intPreferencesKey("total_words")
        val TOTAL_CORRECT_WORDS_KEY = intPreferencesKey("total_correct_words")
        val MUSIC_ENABLED = booleanPreferencesKey("music_enabled")
        val VIBRATION_ENABLED = booleanPreferencesKey("vibration_enabled")
    }

    val letterCountFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            Log.d("DataStoreManager", "Emitted letter count: $preferences") // Add this log

            preferences[LETTER_COUNT_KEY] ?: 2 // Default to 2
        }

    val minuteCountFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            Log.d("DataStoreManager", "Emitted minute count: $preferences")
            preferences[MINUTE_COUNT_KEY] ?: 1 // Default to 1
        }

    val totalWords: Flow<Int> = dataStore.data.map { preference ->
        preference[TOTAL_WORDS_KEY] ?: 0
    }
    val totalCorrectWords: Flow<Int> = dataStore.data.map { preference ->
        preference[TOTAL_CORRECT_WORDS_KEY] ?: 1
    }

    val musicEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[MUSIC_ENABLED] ?: true // Default value is true
        }

    val vibrationEnabledFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[VIBRATION_ENABLED] ?: true // Default value is true
        }


    suspend fun saveLetterCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[LETTER_COUNT_KEY] = count
        }
    }

    suspend fun saveMinuteCount(count: Int) {
        dataStore.edit { preferences ->
            preferences[MINUTE_COUNT_KEY] = count
        }
    }
    suspend fun saveTotalWords(count: Int) {
        dataStore.edit { preferences ->
            preferences[TOTAL_WORDS_KEY] = count
        }

    }
    suspend fun saveTotalCorrectWords(count: Int) {
        dataStore.edit { preferences ->
            preferences[TOTAL_CORRECT_WORDS_KEY] = count
        }
    }
    suspend fun saveMusicPreference(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[MUSIC_ENABLED] = isEnabled
        }
    }

    suspend fun saveVibrationPreference(isEnabled: Boolean) {
        dataStore.edit { preferences ->
            preferences[VIBRATION_ENABLED] = isEnabled
        }
    }
}