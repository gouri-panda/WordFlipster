package com.javix.wordflipster.ui.theme

sealed class WordChainLevel(val name: String) {
    data object Easy:  WordChainLevel("Easy")
    data object Medium: WordChainLevel("Medium")
    data object Hard: WordChainLevel("Hard")
}