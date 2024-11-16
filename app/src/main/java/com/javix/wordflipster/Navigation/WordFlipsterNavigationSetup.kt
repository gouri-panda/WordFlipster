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
import com.javix.wordflipster.GameTypeList
import com.javix.wordflipster.WordFlipHomeScreen
import com.javix.wordflipster.PreviewCategoryGridScreen
import com.javix.wordflipster.SettingsScreen
import com.javix.wordflipster.WordFlipOnboarding
import com.javix.wordflipster.TestDashboardScreen
import com.javix.wordflipster.WelcomeScreen
import com.javix.wordflipster.WelcomeScreenComposeWrapper
import com.javix.wordflipster.WordChainOnboarding
import com.javix.wordflipster.WordChainOnboardingWrapper

@Composable
fun WordFlipsterNavigationSetup(navHostController: NavHostController) {
    NavHost(navController = navHostController, startDestination = Screens.WelcomeScreen.route) {
        composable(Screens.WordFlipOnboarding.route) { WordFlipOnboarding(navHostController) }
        composable(Screens.WordFlipHomeScreen.route, arguments = listOf(navArgument("category") { type = NavType.StringType }), ) {
            val category = it.arguments?.getString("category") ?: ""

            WordFlipHomeScreen(navHostController, category)
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
            WordChainOnboarding()
        }
    }
}