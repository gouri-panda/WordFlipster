package com.javix.wordflipster

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Share
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import com.javix.wordflipster.Navigation.Screens
import nl.dionsegijn.konfetti.compose.KonfettiView
import nl.dionsegijn.konfetti.core.Party
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
            averageTime = "${convertReadableTimeToString(calculateAverageTime(challenge.timeTaken * 1000, challenge.wordsSolved))} Sec/word",
            correctWords = challenge.wordsSolved,
            totalWords = challenge.totalWords,
            onPlayAgainClick = {
                when(challenge.gameType) {
                    wordFlipGame -> navController.navigate(Screens.WordFlipHomeScreen.createRoute(challenge.gameType.name))
                    wordShuffleGame -> navController.navigate(Screens.WordShuffleMainScreen.createRoute(challenge.gameType.name))
                    wordCrypticGame ->navController.navigate(Screens.WordCrypticMainScreen.createRoute(challenge.gameType.name))
                }
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
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF2196F3), Color(0xFFBBDEFB))
                )
            )
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top Confetti Animation
            ConfettiAnimation()

            // Challenge Stats
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = 16.dp,
                shape = RoundedCornerShape(24.dp),
                backgroundColor = Color.White
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "🎉 Challenge  Completed! 🎉",
                        style = MaterialTheme.typography.h4.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF01579B)
                        )
                    )
                    Divider(color = Color(0xFFB3E5FC), thickness = 1.dp, modifier = Modifier.padding(vertical = 8.dp))
                    Text(
                        text = "⏱ Time Taken: $time",
                        style = MaterialTheme.typography.h6.copy(
                            color = Color(0xFF424242),
                            fontWeight = FontWeight.Medium
                        )
                    )
                    Text(
                        text = "⏳ Average Time: $averageTime",
                        style = MaterialTheme.typography.body1.copy(
                            color = Color(0xFF616161),
                            fontStyle = FontStyle.Italic
                        )
                    )
                    Text(
                        text = "✅ Correct Words: $correctWords/ $totalWords",
                        style = MaterialTheme.typography.h6.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0277BD)
                        )
                    )
                }
            }

            // Play Again Button with Gradient
            Button(
                onClick = onPlayAgainClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFF5722), Color(0xFFFFC107))
                        ),
                        shape = RoundedCornerShape(50)
                    ),
                shape = RoundedCornerShape(50)
            ) {
                Text(
                    text = "Play Again",
                    color = Color.White,
                    style = MaterialTheme.typography.button.copy(fontSize = 18.sp)
                )
            }

            // Bottom Navigation Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                IconButton(onClick = onHomeClick) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Home,
                            contentDescription = "Home",
                            tint = Color.Blue,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Home",
                            style = MaterialTheme.typography.caption,
                            color = Color.Blue
                        )
                    }
                }
                IconButton(onClick = onDashboardClick) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            painter = painterResource(id = R.drawable.progress_graph), // Replace with your drawable name
                            contentDescription = "Dashboard",
                            tint = Color.Blue,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Dashboard",
                            style = MaterialTheme.typography.caption,
                            color = Color.Blue
                        )
                    }
                }
                IconButton(onClick = onShareClick) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Share",
                            tint = Color.Blue,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "Share",
                            style = MaterialTheme.typography.caption,
                            color = Color.Blue
                        )
                    }
                }
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
