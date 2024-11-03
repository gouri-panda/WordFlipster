package com.javix.wordflipster

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import java.sql.Date

@Dao
interface ChallengeDao {
    @Insert
    suspend fun insertChallenge(challenge: ChallengeEntity)

    @Query("SELECT * FROM challenges")
    suspend fun getAllChallenges(): List<ChallengeEntity>
}
