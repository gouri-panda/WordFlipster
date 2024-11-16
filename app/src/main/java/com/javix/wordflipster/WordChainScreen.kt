package com.javix.wordflipster

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.with
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalAnimationApi::class)
@Preview(showBackground = true)
@Composable
fun EnhancedWordChainGameScreen() {
    val viewModel : WordChainViewModel = viewModel()
    val currentWord: String by remember { viewModel.currentWord }
    val movesLeft by remember { viewModel.movesLeft }
    val timeLeft by remember { viewModel.timeLeft }
    val score by remember { viewModel.score }
    val hint by remember { viewModel.hint }
    val targetWord = listOf("cat")
//    val correctLetters by remember { viewModel.correctLetters }

    // Define dynamic colors based on correct/incorrect input
    val backgroundColor = Color(0xFFFAFAFA)  // Light gray background
    val buttonColor = Color(0xFF009688)  // Teal for primary actions
    val incorrectColor = Color(0xFFD32F2F)  // Red for incorrect inputs
    val correctColor = Color(0xFF388E3C)  // Green for correct inputs

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Game Header and Timer
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp)
        ) {
            Text(
                text = "Word Chain Game",
                style = MaterialTheme.typography.h3.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF00796B)
            )
            Text(
                text = "Score: $score",
                style = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Bold),
                color = Color(0xFF00796B)
            )
        }

        // Timer Progress Bar
        LinearProgressIndicator(
            progress = timeLeft / 100f,  // Assuming timeLeft is between 0-100
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            color = buttonColor
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Current and Target Word Display
        Row(modifier = Modifier.padding(bottom = 16.dp)) {
            Text(
                text = "Start Word: $currentWord",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = "Target Word: $targetWord",
                style = MaterialTheme.typography.h5.copy(fontWeight = FontWeight.Bold),
                color = Color.Black
            )
        }

        // Input fields with animated color change for correct/incorrect letters
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            for (i in currentWord.indices) {
                var letter by remember { mutableStateOf(currentWord[i].toString()) }
                val isCorrect = letter == targetWord[i].toString()
                val letterColor = if (isCorrect) correctColor else incorrectColor

                // Animated transition for letter input
                AnimatedContent(
                    targetState = letter,
                    transitionSpec = {
                        fadeIn() with fadeOut() // Smooth transition
                    }
                ) { animatedLetter ->
                    Box(modifier = Modifier.padding(4.dp)) {
                        TextField(
                            value = animatedLetter,
                            onValueChange = {
//                                if (it.length == 1) {
//                                    letter = it
//                                    viewModel.changeLetter(it[0])
//                                }
                            },
                            label = { Text("Letter ${i + 1}") },
                            singleLine = true,
                            modifier = Modifier
                                .width(60.dp)
                                .background(Color.White, shape = RoundedCornerShape(8.dp))
                                .padding(8.dp),
                            textStyle = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold),
                            colors = TextFieldDefaults.textFieldColors(
                                backgroundColor = Color.White,
                                focusedIndicatorColor = letterColor,
                                unfocusedIndicatorColor = Color.Gray
                            )
                        )
                    }
                }
            }
        }

        // Hint button (floating action button)
        Spacer(modifier = Modifier.height(16.dp))
        FloatingActionButton(
            onClick = {  },
            backgroundColor = buttonColor,
            modifier = Modifier
                .padding(16.dp)
                .align(Alignment.End)
        ) {
            Icon(Icons.Default.Home, contentDescription = "Hint")
        }

        // Display hint as a pop-up or text
        if (hint.isNotEmpty()) {
            Text(
                text = "Hint: $hint",
                style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold),
                color = Color.Black,
                modifier = Modifier.padding(top = 16.dp)
            )
        }

        // Game Completion Button
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = { viewModel.checkWordValidity("home", {}) },
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = buttonColor)
        ) {
            Text(text = "Check Completion", color = Color.White)
        }
    }
}
