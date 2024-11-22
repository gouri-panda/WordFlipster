package com.javix.wordflipster

import android.content.Context
import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.javix.wordflipster.ui.theme.WordChainLevel
import java.io.BufferedReader
import java.io.InputStreamReader


@Composable
fun WordChainMainScreen (navController: NavHostController, level: String) {
   var currentWordIndex = rememberSaveable { mutableStateOf(0) }
    val viewModel: WordChainMainScreenViewModel = viewModel(factory = WordChainMainScreenViewModelFactory(
        context = LocalContext.current.applicationContext,
        level = WordChainLevel.Easy,
        {}
    ))
    val charLists = viewModel.getCharList()
    val context = LocalContext.current

    Box(modifier = Modifier
        .padding(8.dp)
        .padding(WindowInsets.ime.asPaddingValues())) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxSize()
        ) {
            TopBar(navController) {}
            TimerAndCorrectObjectsWithTimerWrapper(viewModel)

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
                    val currentWord = charLists[currentWordIndex.value]
                    WordGridWithWoodenTiles(currentWord)
                    ArrowButton()
//                    InputWordWrapperView(
//                        currentWord,
//                        currentWordIndex = currentWordIndex.value,
//                        isVibrationEnabled = true,
//                    ) { count, _->
//                       val isValidWord = isValidWordFromFileBinarySearch(context,"bato")
//                        Log.d("WordFlipster", isValidWord.toString())
//
//                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { currentWordIndex.value += 1 },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        ) {
                            Text("Next", color = Color.White)
                        }
                        Button(onClick = { /*TODO*/ },
                            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        ) {
                            Text("Finish", color = Color.White)

                        }
                    }

                }
            }
        }
    }
}
@Composable
private fun TimerAndCorrectObjectsWithTimerWrapper(viewModel: WordChainMainScreenViewModel) {
    TimerAndCorrectObjectsWithTimer(59, 4,  5) //todo
}

fun isValidWordFromFileBinarySearch(context: Context, word: String): Boolean {
    return try {
        val inputStream = context.assets.open("dictionary.txt")
        val reader = BufferedReader(InputStreamReader(inputStream))

        // Read the entire file into a list
        val lines = reader.readLines()

        // Perform binary search if the file is sorted
        val index = lines.binarySearch { it.trim().compareTo(word, ignoreCase = true) }

        // Return true if word is found
        index >= 0
    } catch (e: Exception) {
        // Handle errors such as file not found
        false
    }
}
