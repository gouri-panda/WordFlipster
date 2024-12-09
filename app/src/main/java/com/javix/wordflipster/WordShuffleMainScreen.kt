package com.javix.wordflipster

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.javix.wordflipster.Navigation.Screens
import com.javix.wordflipster.ui.theme.WordFlipsterTheme


@Composable
fun WordShuffleMainScreen(navController: NavController, category: String) {
    BaseScreen {
        var showDialog by remember { mutableStateOf(false) }
        BackHandler {
            showDialog = true
        }
        val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(
            LocalContext.current.applicationContext, category = category
        ) { challenge ->
            finishChallenge(challenge, navController)
        }
        )

        val charLists = homeViewModel.getCharList()
        homeViewModel.updateCurrentScreen(Screens.WordShuffleMainScreen)
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Exit Game?") },
                text = { Text("Are you sure you want to exit? Your progress will be lost.") },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        navController.popBackStack()
                    }) {
                        Text("Yes")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDialog = false }) {
                        Text("No")
                    }
                }
            )
        }
        if (!homeViewModel.isLoading.value) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .padding(WindowInsets.ime.asPaddingValues())
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .fillMaxSize()
                ) {
                    val currentScreen = remember { mutableStateOf(Screens.WordShuffleMainScreen) }

                    TopBar(
                        navController,
                        screen = currentScreen.value
                    ) {} // Todo show dialog button exit if necessary


                    TimerAndCorrectObjectsWithTimerWrapper(
                        homeViewModel
                    )

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(bottom = 32.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val correctWord =
                                charLists[homeViewModel.currentWordIndex.collectAsState().value]
                            val shuffledWord = correctWord.shuffled()
                            WordGridWithWoodenTiles(shuffledWord)
                            ArrowButton()
                            InputWordWrapperView(
                                shuffledWord = shuffledWord,
                                correctWord = correctWord,
                                currentWordIndex = homeViewModel.currentWordIndex.collectAsState().value,
                                isVibrationEnabled = homeViewModel.isVibrationEnabled()
                            ) { count, isCorrectWord ->
                                homeViewModel.currentWordIndex.value = count
                                homeViewModel.updateTotalWords(homeViewModel.totalWords.value + 1)
                                if (isCorrectWord) {
                                    homeViewModel.updateCorrectWords(homeViewModel.wordsSolved.value + 1)
                                }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Button(
                                    onClick = {
                                        homeViewModel.currentWordIndex.value += 1
                                        homeViewModel.updateTotalWords(homeViewModel.totalWords.value + 1)

                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                ) {
                                    Text("Next", color = Color.White)
                                }
                                Button(
                                    onClick = {
                                        homeViewModel.finishGame { challenge ->
                                            finishChallenge(challenge, navController)
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                                ) {
                                    Text("Finish", color = Color.White)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            CircularLoadingIndicator()
        }
    }
}
private fun finishChallenge(
    challenge: Challenge?,
    navController: NavController
) {
    val jsonChallenge = Gson().toJson(challenge)
    navController.navigate("overviewChallenge?challenge=${jsonChallenge}")
}

@Composable
private fun InputWordWrapperView(
    correctWord: List<String>,
    shuffledWord: List<String>, //Shuffled letters
    currentWordIndex: Int,
    isVibrationEnabled: Boolean,
    onCompleteListener: (Int, Boolean) -> Unit
) {
    WordFlipsterTheme {

        var inputValue by remember { mutableStateOf("") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            InputWordView(
                inputText = inputValue,
                containerSize = 48.dp,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters
                ),
                charColor = Color.Blue,
                correctWord = correctWord,
                isVibrationEnabled = isVibrationEnabled,
                onValueChange = { value ->
                    if (value.length <= shuffledWord.size) {
                        Log.d("Actual Value", value)
                        if (value.length == shuffledWord.size) {
                            val isCorrectWord = value == correctWord.joinToString("")
                            onCompleteListener(currentWordIndex + 1, isCorrectWord)
                            inputValue = ""
                        }
                    }
                },
                onTextChange = {
                    inputValue = it
                },
                onEachLetterChange = { inputText, index ->
                    val inputChar = inputText.getOrNull(index)?.toString()
                    val isCorrectChar = inputChar != null && inputChar == correctWord.get(index)
                    return@InputWordView isCorrectChar
                }
            )
        }
    }
}
