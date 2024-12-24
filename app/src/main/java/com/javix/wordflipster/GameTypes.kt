package com.javix.wordflipster

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString

@Composable
fun gameTypes() = listOf(
   wordigmaScreen, wordFlipGame, wordShuffleGame, // Todo:: Add wordCrypticGame
)


data class GameType(
    val name: String,
    @DrawableRes val iconResId: Int,
    val description: String,
    val isPopular: Boolean = false
)

val wordFlipGame = GameType("Word Flip", R.drawable.word_flip, "Flip the word letters.")
val wordigmaScreen = GameType("Word Code", R.drawable.word_flip, "Guess the letters", isPopular = true)
val wordShuffleGame = GameType("Word Shuffle", R.drawable.word_flip, "Flip the word letters.")
val wordCrypticGame = GameType("Word Cryptic", R.drawable.word_flip, "Used for cryptic later", isPopular = true)
val wordChainGame = GameType(R.string.word_Chain.toString(), R.drawable.colors, "Find next words in a grid.", isPopular = true)
val wordGameX =  GameType(R.string.word_Chain.toString(), R.drawable.word_flip, "Identify a specific word among jumbled letters.")


