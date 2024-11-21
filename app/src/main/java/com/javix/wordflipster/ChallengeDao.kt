package com.javix.wordflipster

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface ChallengeDao {
    @Insert
    suspend fun insertChallenge(challenge: ChallengeEntity)

    @Query("SELECT * FROM challenges ORDER BY date DESC")
    suspend fun getAllChallenges(): List<ChallengeEntity>
}
