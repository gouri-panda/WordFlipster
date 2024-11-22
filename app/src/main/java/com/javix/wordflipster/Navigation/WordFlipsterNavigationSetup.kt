package com.javix.wordflipster.Navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.javix.wordflipster.Challenge
import com.javix.wordflipster.ChallengeCompleteScreenWrapper
import com.javix.wordflipster.WordFlipMainScreen
import com.javix.wordflipster.PreviewCategoryGridScreen
import com.javix.wordflipster.SettingsScreen
import com.javix.wordflipster.WordFlipOnboarding
import com.javix.wordflipster.TestDashboardScreen
import com.javix.wordflipster.WelcomeScreenComposeWrapper
import com.javix.wordflipster.WordChainMainScreen
import com.javix.wordflipster.WordChainOnboardingWrapper
import com.javix.wordflipster.WordShuffleMainScreen
import com.javix.wordflipster.WordShuffleOnboarding

@Composable
fun WordFlipsterNavigationSetup(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.WelcomeScreen.route) {
        composable(Screens.WordFlipOnboarding.route) { WordFlipOnboarding(navHostController) }
        composable(Screens.WordFlipHomeScreen.route, arguments = listOf(navArgument("category") { type = NavType.StringType }), ) {
            val category = it.arguments?.getString("category") ?: ""
            WordFlipMainScreen(navHostController, category)
        }
        composable(Screens.Dashboard.route) { TestDashboardScreen() }
        composable(Screens.Settings.route) { SettingsScreen() }
        composable(route = "overviewChallenge?challenge={challenge}", arguments = listOf(navArgument("challenge") { type = NavType.StringType })){ backStackEntry ->
            val challengeString = backStackEntry.arguments?.getString("challenge")
            val challenge = Gson().fromJson(challengeString, Challenge::class.java)

            ChallengeCompleteScreenWrapper(navHostController, challenge)
        }
        composable(Screens.PuzzleCategory.route){
            PreviewCategoryGridScreen(navHostController)
        }
        composable(Screens.WelcomeScreen.route)  { WelcomeScreenComposeWrapper(navHostController)}

        composable(Screens.WordChainOnboarding.route) {
            WordChainOnboardingWrapper(navHostController)
        }
        composable(Screens.WordChainMainScreen.route, arguments = listOf(navArgument("level") {type = NavType.StringType})){
            val level = it.arguments?.getString("level") ?: ""
            WordChainMainScreen(navController = navHostController, level = level)
        }
        composable(Screens.WordShuffleOnboarding.route) {
            WordShuffleOnboarding(navHostController)
        }
        composable(Screens.WordShuffleMainScreen.route, arguments = listOf(navArgument("category") { type = NavType.StringType }), ) {
            val category = it.arguments?.getString("category") ?: ""
            WordShuffleMainScreen(navHostController, category)
        }
    }
}