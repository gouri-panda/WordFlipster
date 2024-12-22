package com.javix.wordflipster

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QuotesLevelsDao {
    @Insert
    suspend fun insertChallenge(challenge: QuoteEntity)

    @Query("SELECT * FROM quote_levels ORDER BY date DESC")
    suspend fun getAllChallenges(): List<QuoteEntity>
}