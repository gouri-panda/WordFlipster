package com.javix.wordflipster


import java.util.Date


data class Challenge(
    val wordsSolved: Int,
    val totalWords: Int,
    val timeTaken: Long, // time in milliseconds
    val date: Date?,
    val gameType: GameType
)