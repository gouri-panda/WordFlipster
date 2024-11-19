package com.javix.wordflipster

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.javix.wordflipster.Navigation.Screens
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
import nl.dionsegijn.konfetti.core.Position
import nl.dionsegijn.konfetti.core.emitter.Emitter
import java.util.concurrent.TimeUnit

@Composable
fun ChallengeCompleteScreenWrapper(navController: NavController, challenge: Challenge) {
    BackHandler {
        navController.navigate(Screens.WelcomeScreen.route){
            popUpTo(Screens.WelcomeScreen.route) {
                inclusive = true // Two make sure we don't have double Welcome screen
            }
        }
    }
    Box {
        val party = Party(
            emitter = Emitter(duration = 5, TimeUnit.SECONDS).perSecond(30),
            spread = 360
        )
        KonfettiView(
            modifier = Modifier
                .fillMaxSize()
                .zIndex(1f),
            parties = listOf(party),
        )
        ChallengeCompletionScreen(
            time = "${challenge.timeTaken / 60} Min",
            averageTime = "${convertReadableTimeToString(calculateAverageTime(challenge.timeTaken, challenge.wordsSolved))} Sec / word",
            correctWords = challenge.wordsSolved,
            totalWords = challenge.totalWords,
            onPlayAgainClick = {
                navController.navigate(Screens.WordFlipHomeScreen.createRoute(""))
            }, onHomeClick = {
                navController.navigate(Screens.WelcomeScreen.route)
            }, onDashboardClick = {
                navController.navigate(Screens.Dashboard.route)
            }, onShareClick = {}) // Todo: Add share click when in the play store :)

    }


}


@Composable
fun ChallengeCompletionScreen(
    time: String = "5 Min",
    averageTime: String = "30 sec/word",
    correctWords: Int = 7,
    totalWords: Int = 10,
    onPlayAgainClick: () -> Unit,
    onHomeClick: () -> Unit,
    onDashboardClick: () -> Unit,
    onShareClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Confetti Animation Placeholder
        ConfettiAnimation()

        // Challenge Stats
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(text = "Time $time", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Average time: $averageTime", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Correct Words: $correctWords", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(text = "Total words: $totalWords", fontSize = 18.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(16.dp))

            // Play Again Button
            Button(onClick = onPlayAgainClick, colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)) {
                Text(" Play Again", color = Color.White)
            }
        }

        // Bottom Navigation Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = onHomeClick) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = Color.Blue
                )
            }
            IconButton(onClick = onDashboardClick) {
                Icon(
                    painter = painterResource(id = R.drawable.progress_graph), // Replace with your drawable name
                    contentDescription = "Progress Graph",
                    tint = Color.Blue,
                    modifier = Modifier.size(24.dp) // Set size as needed
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Share",
                    tint = Color.Blue
                )
            }
        }
    }
}

@Composable
fun ConfettiAnimation() {
    // Placeholder for confetti animation. Replace with your animation code.
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "🎉", fontSize = 80.sp) // Sample confetti text, replace with animation
    }
}
