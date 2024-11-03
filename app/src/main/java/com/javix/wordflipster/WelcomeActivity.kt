package com.javix.wordflipster

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.toRoute
import com.google.gson.Gson
import com.javix.wordflipster.ui.theme.WordFlipsterTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class WelcomeActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WordFlipsterTheme {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 32.dp),
                ) { innerPadding ->
                    val navController = rememberNavController()
                    NavHost(navController = navController, startDestination = "home") {
                        composable("home") { StartingScreen(navController) }
                        composable("mainScreen?category={category}", arguments = listOf(navArgument("category") { type = NavType.StringType }), ) {
                            val category = it.arguments?.getString("category") ?: ""

                            HomeScreen(navController, category)
                        }
                        composable("dashboard") { TestDashboardScreen() }
                        composable("settings") { SettingsScreen()}
                        composable(route = "overviewChallenge?challenge={challenge}", arguments = listOf(navArgument("challenge") { type = NavType.StringType })){ backStackEntry ->
                            val challengeString = backStackEntry.arguments?.getString("challenge")
                            val challenge = Gson().fromJson(challengeString, Challenge::class.java)

                            ChallengeCompleteScreenWrapper(navController, challenge)
                        }
                        composable("puzzleCategory"){
                            PreviewCategoryGridScreen(navController)
                        }
                    }
                }
            }
        }
    }

}

@Composable
fun StartingScreen(navController: NavController) {

    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val coroutineScope = rememberCoroutineScope()


    // State variables to hold the letter and minute counts


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp)
    ) {


        TopBar(navController)

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = { navController.navigate("puzzleCategory") },
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                ),  // Rounded corners
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                modifier = Modifier.size(200.dp, 50.dp)
            ) {
                Text("Category", color = Color.White)
            }
            EditLettersButton( dataStoreManager, coroutineScope)
            MinutesButton(dataStoreManager, coroutineScope)
            Button(
                onClick = { navController.navigate("mainScreen?category=") },
                shape = RoundedCornerShape(
                    topStart = 16.dp,
                    topEnd = 16.dp,
                    bottomEnd = 16.dp,
                    bottomStart = 16.dp
                ),  // Rounded corners
                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                modifier = Modifier.size(200.dp, 50.dp)
            ) {
                Text("Play", color = Color.White)
            }
        }
    }
}


@Composable
fun EditLettersButton(dataStoreManager: DataStoreManager, coroutineScope: CoroutineScope) {
    var letterCount by remember { mutableStateOf(2) }

    LaunchedEffect(Unit) {
        dataStoreManager.letterCountFlow.collect { count ->
            letterCount = count
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                if (letterCount >= 2) {
                    letterCount--
                    coroutineScope.launch { dataStoreManager.saveLetterCount(letterCount) }
                }
            },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("-", color = Color.Black) }
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),  // Rounded corners
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Blue background
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text("${letterCount} Letters", color = Color.White)  // White text
        }
        Button(
            onClick = {
                if (letterCount < 5) {
                    letterCount++
                    coroutineScope.launch { dataStoreManager.saveLetterCount(letterCount) }
                }
            },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("+", color = Color.Black) }
    }

}

@Composable
fun MinutesButton(dataStoreManager: DataStoreManager, coroutineScope: CoroutineScope) {
    var minuteCount by remember { mutableStateOf(1) }
    LaunchedEffect(Unit) {
        dataStoreManager.minuteCountFlow.collect { count ->
            minuteCount = count
        }
    }

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {
                if (minuteCount > 1) {
                    minuteCount--
                    coroutineScope.launch { dataStoreManager.saveMinuteCount(minuteCount) }
                }
            },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("-", color = Color.Black) }
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),  // Rounded corners
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Blue background
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text("${minuteCount} Minutes", color = Color.White)  // White text
        }
        Button(
            onClick = {
                if (minuteCount < 5) {
                    minuteCount++
                    coroutineScope.launch { dataStoreManager.saveMinuteCount(minuteCount) }
                }
            },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("+", color = Color.Black) }
    }

}

