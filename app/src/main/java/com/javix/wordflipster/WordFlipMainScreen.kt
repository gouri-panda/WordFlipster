package com.javix.wordflipster

import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.gson.Gson
import com.javix.wordflipster.ui.theme.WordFlipsterTheme
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter

@Composable
fun WordFlipMainScreen(navController: NavController, category: String) {
    var showDialog by remember { mutableStateOf(false) }
    BackHandler {
        showDialog = true
    }
    val homeViewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(
        LocalContext.current.applicationContext, category = category
    ) { challenge ->
        val jsonChallenge = Gson().toJson(challenge)
        navController.navigate("overviewChallenge?challenge=${jsonChallenge}")
    }
    )

    val charLists = homeViewModel.getCharList()
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
            TopBar(navController) {} // Todo show dialog button exit if necessary


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
                    WordGridWithWoodenTiles(charLists[homeViewModel.currentWordIndex.collectAsState().value])
                    ArrowButton()
                    InputWordWrapperView(
                        charLists[0],
                        currentWordIndex = homeViewModel.currentWordIndex.collectAsState().value,
                        isVibrationEnabled = homeViewModel.isVibrationEnabled()
                    ) { count, isCorrectWord ->
                        homeViewModel.currentWordIndex.value = count
                        homeViewModel.updateTotalWords(homeViewModel.totalWords.value + 1)
                        if (isCorrectWord) {
                            homeViewModel.updateCorrectWords(homeViewModel.wordsSolved.value + 1)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MainScreen() {
    WordFlipsterTheme {
        // Holds the current value of each input field
        val pinValues = remember { mutableStateListOf("", "", "", "") }

        // Create FocusRequester for each text field to request focus dynamically
        val focusRequesters = List(4) { FocusRequester() }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            for (i in 0 until 4) {
                BasicTextField(
                    value = pinValues[i],
                    onValueChange = { newValue ->
                        if (newValue.length <= 1) {
                            pinValues[i] = newValue
                            if (newValue.isNotEmpty() && i < 3) {
                                focusRequesters[i + 1].requestFocus() // Move to the next field
                            }
                        }
                    },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        color = Color.Black,
                    ),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number), // Number keyboard
                    modifier = Modifier
                        .size(50.dp)
                        .border(2.dp, Color.Blue) // Blue border
                        .background(Color.White) // Black background
                        .focusRequester(focusRequesters[i])
                        .padding(8.dp)
                )
            }
        }
    }
}

@Composable
fun InputWordWrapperView(
    word: List<String>,
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
                onTextChange = {
                    inputValue = it
                    Log.d("Actual Value", inputValue)

                },
                containerSize = 48.dp,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters
                ),
                charColor = Color.Blue,
                correctWord = word,
                currentWordIndex = currentWordIndex,
                isVibrationEnabled = isVibrationEnabled,
                onWordCompleteListener = { count, isCorrect ->
                    onCompleteListener(count, isCorrect)
                    inputValue = ""
                }
            )
        }
    }
}


@Composable
fun InputWordView(
    correctWord: List<String>,
    inputText: String,
    modifier: Modifier = Modifier,
    charColor: Color = Color.Black,
    containerColor: Color = charColor,
    selectedContainerColor: Color = charColor,
    charBackground: Color = Color.Transparent,
    charSize: TextUnit = 16.sp,
    containerSize: Dp = charSize.value.dp * 2,
    containerRadius: Dp = 4.dp,
    containerSpacing: Dp = 4.dp,
    count: Int = correctWord.size,
    keyboardOptions: KeyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
    onTextChange: (String) -> Unit,
    currentWordIndex: Int,
    isVibrationEnabled: Boolean,
    onWordCompleteListener: (Int, Boolean) -> Unit
) {
    val context = LocalContext.current
    val vibrator = remember { context.getSystemService(Vibrator::class.java) }
    BasicTextField(
        modifier = modifier,
        value = inputText,
        onValueChange = {
            if (it.length <= count) {
                onTextChange.invoke(it)
                Log.d("Actual Value", it)
                if (it.length == correctWord.size) {
                    val isCorrectWord = evaluateCorrectWord(
                        inputWord = it,
                        correctWord = correctWord.joinToString("")
                    )
                    onWordCompleteListener(currentWordIndex + 1, isCorrectWord)
                }
            }
        },
        keyboardOptions = keyboardOptions,
        decorationBox = {
            Row(horizontalArrangement = Arrangement.spacedBy(containerSpacing)) {
                repeat(count) { index ->
                    // Check if there is an input character for this position
                    val inputChar = inputText.getOrNull(index)?.toString()
                    val isCorrectChar = inputChar != null && inputText.getOrNull(index)
                        ?.toString() == correctWord.getOrNull((correctWord.size - 1) - index)


                    // Set color based on correctness, default to a neutral color for empty boxes
                    val borderColor = when {
                        inputChar == null -> Color.Gray  // Default color for empty boxes
                        isCorrectChar -> Color.Blue      // Blue if correct
                        else -> {
                            // Trigger vibration if input is incorrect
                            if (isVibrationEnabled) {
                            createVibration(vibrator)
                            }
                            Color.Red  // Red if incorrect
                        }

                    }

                    // Bounce animation only if incorrect input
                    val scale by animateFloatAsState(
                        targetValue = if (inputChar != null && !isCorrectChar) 1.2f else 1f,
                        animationSpec = spring(dampingRatio = Spring.DampingRatioHighBouncy)
                    )
                    CharView(
                        index = index,
                        count = count,
                        text = inputText,
                        charColor = charColor,
                        containerColor = borderColor,
                        highlightColor = selectedContainerColor,
                        scale = scale,
                        charSize = charSize,
                        containerRadius = containerRadius,
                        containerSize = containerSize,
                        charBackground = charBackground,
                        letter = correctWord[(correctWord.size - 1) - index]
                    )
                }
            }
        }
    )
    // Observe inputText changes and trigger onWordCompleteListener only after it appears on screen
    LaunchedEffect(inputText) {
        snapshotFlow { inputText }
            .filter { it.length == correctWord.size - 1 }
            .debounce(5000)
            .collect { completedText ->
//                        val isCorrectWord = evaluateCorrectWord(
//                            inputWord = completedText,
//                            correctWord = correctWord.joinToString("")
//                        )
//                        onWordCompleteListener(currentWordIndex + 1, isCorrectWord)
            }
    }
}


@Composable
private fun CharView(
    index: Int,
    count: Int,
    text: String,
    charColor: Color,
    highlightColor: Color,
    scale: Float = 1f,
    containerColor: Color,
    charSize: TextUnit,
    containerSize: Dp,
    containerRadius: Dp,
    charBackground: Color = Color.Transparent,
    letter: String
) {
    val modifier =
        Modifier
            .size(containerSize)
            .border(
                width = 1.dp,
                color = containerColor,
                shape = RoundedCornerShape(containerRadius)
            )
            .scale(scale)
            .padding(bottom = 4.dp)
            .clip(RoundedCornerShape(containerRadius))
            .background(charBackground)


    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        val char = when {
            index >= text.length -> ""
            else -> text[index].toString()
        }

        Text(
            text = char,
            color = charColor,
            modifier = modifier.wrapContentHeight(),
            style = MaterialTheme.typography.body1,
            fontSize = charSize,
            textAlign = TextAlign.Center,
        )
    }
}

@Composable
fun WordGridWithWoodenTiles(word: List<String>) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(), horizontalArrangement = Arrangement.Center
        ) {
            word.forEach { letter ->
                LazyRow(
                    modifier = Modifier.padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(letter.length) { index ->
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .border(1.dp, Color(0xFF8B4513)) // Border color for wooden effect
                                .background(
                                    Brush.linearGradient(
                                        colors = listOf(
                                            Color(0xFFDEB887),
                                            Color(0xFFA0522D)
                                        ) // Simulating wood
                                    )
                                )
                                .shadow(
                                    4.dp,
                                    shape = RoundedCornerShape(4.dp)
                                ), // Add a shadow for 3D effect
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = letter[index].toString(),
                                style = MaterialTheme.typography.h5,
                                color = Color.Black,
                                modifier = Modifier.padding(4.dp) // Adjust text positioning
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun WordGridWith3DTiles() {
    val words: List<String> = listOf("W", "O", "R", "D")
    Column(modifier = Modifier.fillMaxSize()) {
        words.forEach { word ->
            LazyRow(
                modifier = Modifier.padding(8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                items(word.length) { index ->
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .shadow(8.dp, shape = RoundedCornerShape(8.dp)) // 3D shadow effect
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(
                                        Color(0xFFDEB887),
                                        Color(0xFFA0522D)
                                    ), // Wooden tile colors
                                    start = Offset(0f, 0f),
                                    end = Offset(100f, 100f)
                                )
                            )
                            .border(2.dp, Color(0xFF8B4513)) // Darker border for a 3D frame
                            .clip(RoundedCornerShape(8.dp)) // Rounded edges for smoothness
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = word[index].toString(),
                            style = MaterialTheme.typography.h4.copy(
                                shadow = Shadow(
                                    color = Color.Gray, // Add shadow to the text for 3D effect
                                    offset = Offset(1f, 1f),
                                    blurRadius = 2f
                                )
                            ),
                            color = Color.Black,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
@Composable
fun ArrowButton() {
    IconButton(
        onClick = { /* Handle settings action */ },
    ) {
        Icon(Icons.Default.ArrowDropDown, contentDescription = "Settings", tint = Color.Black)
    }
}


@Composable
fun TimerAndCorrectObjects(
    timeLeft: String, // Timer in string format like "00:59"
    wordsSolved: Int, // Number of correct objects
    totalWords: Int,   // Total number of objects
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.End
    ) {
        // Display the timer
        Text(
            text = "Time: $timeLeft",
            style = MaterialTheme.typography.h6,
            color = Color.Black,
        )

        Spacer(modifier = Modifier.width(16.dp)) // Spacer between Timer and Correct Objects

        // Display the correct objects out of total objects
        Text(
            text = "$wordsSolved/$totalWords",
            style = MaterialTheme.typography.h6,
            color = Color.Black,
        )
    }
}

@Composable
private fun TimerAndCorrectObjectsWithTimerWrapper(homeViewModel: HomeViewModel) {
    val totalTime by homeViewModel.remainingTime.collectAsState()
    val wordsSolved by homeViewModel.wordsSolved.collectAsState()
    val totalWords by homeViewModel.totalWords.collectAsState()
    TimerAndCorrectObjectsWithTimer(totalTime, wordsSolved, totalWords)
}

@Composable
fun TimerAndCorrectObjectsWithTimer(
    totalTime: Int,
    wordsSolved: Int,
    totalWords: Int
) {


    // Format time in MM:SS format
    val minutes = totalTime / 60
    val seconds = totalTime % 60
    val formattedTime = String.format("%02d:%02d", minutes, seconds)

    // Display timer and correct objects
    TimerAndCorrectObjects(
        timeLeft = formattedTime,
        wordsSolved = wordsSolved,
        totalWords = totalWords
    )
}

fun evaluateCorrectWord(correctWord: String, inputWord: String): Boolean {
    for (index in correctWord.indices) {
        if (!correctWord[(correctWord.length - 1) - index].equals(inputWord[index])) {
            return false
        }
    }
    return true
}

fun createVibration(vibrator: Vibrator) {
        vibrator?.let { vib ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vib.vibrate(
                    VibrationEffect.createOneShot(
                        50,
                        VibrationEffect.DEFAULT_AMPLITUDE
                    )
                )
            } else {
                vib.vibrate(50)
            }
        }
}

@Preview(showBackground = true)
@Composable
fun TestWordGridWithWoodenTiles() {
    val word = listOf("hope","story","life")
    // Remember a rotation angle for each letter box
    val rotations = remember { word.map { Animatable(0f) } }

    // Trigger flip animation whenever the word changes
    LaunchedEffect(word) {
        rotations.forEach { rotation ->
            // Reset rotation before starting the flip
            rotation.snapTo(0f)
            rotation.animateTo(
                targetValue = 180f,
                animationSpec = tween(durationMillis = 600)
            )
        }
    }

    LazyRow(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(word.size) { index ->
            val rotation = rotations[index].value

            Box(
                modifier = Modifier
                    .size(50.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 16f * density // Adds depth for 3D effect
                    }
                    .border(1.dp, Color(0xFF8B4513))
                    .background(
                        Brush.linearGradient(
                            colors = listOf(
                                Color(0xFFDEB887),
                                Color(0xFFA0522D)
                            )
                        )
                    )
                    .shadow(4.dp, shape = RoundedCornerShape(4.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (rotation < 90f) {
                    // First half of the page (front view)
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = word[index],
                            style = MaterialTheme.typography.h5,
                            color = Color.Black,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                } else {
                    // Second half of the page (back view)
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFFD3D3D3)), // A different color for the back
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = word[index],
                            style = MaterialTheme.typography.h5,
                            color = Color.Gray,
                            modifier = Modifier.padding(4.dp)
                        )
                    }
                }
            }
        }
    }
}
