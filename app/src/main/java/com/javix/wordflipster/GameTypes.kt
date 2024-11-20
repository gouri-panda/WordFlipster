package com.javix.wordflipster

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat.getString

@Composable
fun gameTypes() = listOf(
    GameType(stringResource(id = R.string.word_flip), R.drawable.word_chain, "Flip the word letters."),
    GameType(stringResource(id = R.string.word_Chain), R.drawable.word_chain, "Find next words in a grid.", isPopular = true),
    GameType("Word Cryptic", R.drawable.word_chain, "Identify a specific word among jumbled letters."),
)


data class GameType(
    val name: String,
    @DrawableRes val iconResId: Int,
    val description: String,
    val isPopular: Boolean = false
)
