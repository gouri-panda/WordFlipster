package com.javix.wordflipster

import android.content.Context
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
    }

    val letterCountFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[LETTER_COUNT_KEY] ?: 2 // Default to 2
        }

    val minuteCountFlow: Flow<Int> = dataStore.data
        .map { preferences ->
            preferences[MINUTE_COUNT_KEY] ?: 1 // Default to 1
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
}