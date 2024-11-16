package com.javix.wordflipster

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString

@Composable
fun gameTypes() = listOf(
    GameType(stringResource(id = R.string.word_flip), R.drawable.ic_launcher_background, "Flip the word letters."),
    GameType(stringResource(id = R.string.word_Chain), R.drawable.food, "Find next words in a grid.", isPopular = true),
    GameType("Word Finder", R.drawable.food, "Identify a specific word among jumbled letters."),
    GameType("Anagram Solver", R.drawable.food, "Rearrange letters to form words.", isPopular = true),
    GameType("Speed Flip", R.drawable.food, "Flip words within a time limit."),
    GameType("Category Challenge", R.drawable.food, "Solve words from specific categories."),
    GameType("Daily Puzzle", R.drawable.food, "A new puzzle each day.")
)


data class GameType(
    val name: String,
    @DrawableRes val iconResId: Int,
    val description: String,
    val isPopular: Boolean = false
)
