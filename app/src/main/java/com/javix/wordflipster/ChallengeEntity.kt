package com.javix.wordflipster

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "challenges")
data class ChallengeEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val wordsSolved: Int,
    val totalWords: Int,
    val timeTaken: Long, // time in milliseconds
    val date: Date,
    val gameType: GameType?
)
