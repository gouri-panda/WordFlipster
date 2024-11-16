package com.javix.wordflipster.Navigation


sealed class Screens(val route: String) {
    data object WelcomeScreen : Screens("welcome")
    data object WordFlipHomeScreen: Screens("wordFlipHomeScreen?category={category}") {
        fun createRoute(category: String) = "wordFlipHomeScreen?category=$category"
    }
    data object WordFlipOnboarding: Screens("wordFlipOnboarding")
    data object Dashboard: Screens("dashboard")
    data object Settings: Screens("settings")
    data object WordChainOnboarding : Screens("wordChainOnboarding")
    data object PuzzleCategory: Screens("puzzleCategory")
}