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
    data object WordChainMainScreen: Screens("wordChainMainScreen?level={level}"){
        fun createRoute(level: String) = "wordChainMainScreen?level=$level"
    }
    data object WordShuffleMainScreen: Screens("wordShuffleMainScreen?category={category}"){
        fun createRoute(category: String) = "wordShuffleMainScreen?category=$category"
    }
    data object WordShuffleOnboarding: Screens("wordShuffleOnboarding")

    data object WordCrypticMainScreen: Screens("wordCrypticMainScreen?category={category}") {
        fun createRoute(category: String) = "wordCrypticMainScreen?category=$category"
    }
    data object WordCrypticOnboarding: Screens("wordCrypticOnboarding")

}