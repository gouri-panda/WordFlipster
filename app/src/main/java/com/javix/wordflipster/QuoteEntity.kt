package com.javix.wordflipster

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "quote_levels")
data class QuoteEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val quote:String,
    val author: String,
    val birth: String,
    val death: String,
    val others: String? = null,
    val timeTaken: Long? = null, // time in milliseconds
    val date: Date,
    val gameType: GameType?
)
