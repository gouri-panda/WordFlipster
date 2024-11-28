package com.javix.wordflipster

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.focus.onFocusEvent
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

@Composable
fun WordigmaScreen(
    wordsWithHints: List<WordWithHints>, // List of words and hints
    onLetterInput: (String, Int) -> Unit // Callback for user input
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 16.dp, start = 8.dp, end = 8.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        val currentFocusIndex = remember { mutableStateOf(0) }
        val inputLetter = remember { mutableStateOf("") }

        val mapping = remember {mutableStateOf(getMapping()) }
        TopInfoSection(lives = 7, mistakes = 1, level = 1)

        QuoteDisplaySection(quote = "WHERE THERE IS LOVE THERE IS LIFE.", maxRowLength = 13, mapping = mapping.value) {

        }


    }

}@Composable
fun QuoteDisplaySection(
    quote: String,
    commonLetterCount: Int = 2,
    maxRowLength: Int,
    mapping: Map<Int,Char>,
    onLetterInput: (Char) -> Unit
) {
    val words = quote.split(" ")
    val letterFrequency = words.joinToString("").groupingBy { it.lowercaseChar() }.eachCount()
    val commonLetters = letterFrequency
        .filter { it.key.isLetter() }
        .toList()
        .sortedByDescending { it.second }
        .take(commonLetterCount)
        .map { it.first }

    val hiddenIndices = words.flatMapIndexed { wordIndex, word ->
        word.mapIndexedNotNull { charIndex, char ->
            if (commonLetters.contains(char.lowercaseChar())) Triple(wordIndex, charIndex, word) else null
        }
    }

    val userInputs = remember {
        words.map { word ->
            word.map { char -> if (commonLetters.contains(char.lowercaseChar())) "" else char.toString() }.toMutableList()
        }
    }

    val currentHiddenIndex = remember { mutableStateOf(0) }
    val manuallySelectedIndex = remember { mutableStateOf<Int?>(null) }

    val rows = mutableListOf<List<Int>>()
    var currentRow = mutableListOf<Int>()
    var currentLength = 0

    for ((index, word) in words.withIndex()) {
        if (currentLength + word.length + currentRow.size > maxRowLength) {
            rows.add(currentRow)
            currentRow = mutableListOf()
            currentLength = 0
        }
        currentRow.add(index)
        currentLength += word.length
    }
    if (currentRow.isNotEmpty()) {
        rows.add(currentRow)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f)) // Push content downward

        for (row in rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (wordIndex in row) {
                    WordRow(
                        word = words[wordIndex],
                        commonLetters = commonLetters,
                        userInput = userInputs[wordIndex],
                        hiddenIndices = hiddenIndices.filter { it.first == wordIndex },
                        mapping = mapping,
                        currentHiddenIndex = hiddenIndices.getOrNull(
                            manuallySelectedIndex.value ?: currentHiddenIndex.value
                        )?.let { (index, _, _) ->
                            if (index == wordIndex) manuallySelectedIndex.value ?: currentHiddenIndex.value else -1
                        } ?: -1,
                        onValueChange = { wordIdx, charIdx, input ->
                            if (input.length == 1) {
                                userInputs[wordIdx][charIdx] = input
                                manuallySelectedIndex.value = null
                                val nextHiddenIndex = currentHiddenIndex.value + 1
                                if (nextHiddenIndex < hiddenIndices.size) {
                                    currentHiddenIndex.value = nextHiddenIndex
                                }
                            }
                        },
                        onLetterSelected = { selectedIndex ->
                            manuallySelectedIndex.value = selectedIndex
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
        Spacer(modifier = Modifier.weight(1f)) // Push content downward


        CustomKeyboard(onKeyPress = { char ->
            if (currentHiddenIndex.value < hiddenIndices.size) {
                val (wordIndex, charIndex, _) = hiddenIndices[currentHiddenIndex.value]
                if (userInputs[wordIndex][charIndex].isEmpty()) { // Prevent overwriting existing input
                    userInputs[wordIndex][charIndex] = char.toString()
                    val nextHiddenIndex = currentHiddenIndex.value + 1
                    if (nextHiddenIndex <= hiddenIndices.size) {
                        currentHiddenIndex.value = nextHiddenIndex
                    }
                }
            }
        }, onBackspacePress = {
            if (currentHiddenIndex.value > 0) {
                currentHiddenIndex.value -= 1
                val (wordIndex, charIndex, _) = hiddenIndices[currentHiddenIndex.value]
                userInputs[wordIndex][charIndex] = ""
            }
        }, {})
    }

}


@Composable
fun WordRow(
    word: String,
    commonLetters: List<Char>,
    userInput: List<String>,
    mapping: Map<Int, Char>,
    hiddenIndices: List<Triple<Int, Int, String>>,
    currentHiddenIndex: Int,
    onValueChange: (Int, Int, String) -> Unit,
    onLetterSelected: (Int) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        word.forEachIndexed { charIndex, char ->
            val shouldHide = commonLetters.contains(char.lowercaseChar())
            val isFocused = currentHiddenIndex != -1 && hiddenIndices.indexOfFirst { it.second == charIndex } == currentHiddenIndex

            Box(
                modifier = Modifier
                    .padding(1.dp)
                    .border(
                        width = if (shouldHide && isFocused) 1.dp else 0.dp,
                        color = if (shouldHide && isFocused) Color.Green
                        else if (shouldHide) Color.White
                        else Color.Transparent,
                        shape = RoundedCornerShape(4.dp)
                    )
                    .clickable(enabled = shouldHide) {
                        val index = hiddenIndices.indexOfFirst { it.second == charIndex }
                        if (index != -1) onLetterSelected(index)
                    },
                contentAlignment = Alignment.Center
            ) {
                if (shouldHide) {
                    Column(modifier = Modifier.padding(top = 0.dp)) {
                        BasicTextField(
                            value = userInput[charIndex],
                            onValueChange = { input ->
                                if (input.length <= 1) {
                                    val wordIdx =
                                        hiddenIndices.firstOrNull { it.second == charIndex }?.first
                                            ?: return@BasicTextField
                                    onValueChange(wordIdx, charIndex, input)
                                }
                            },
                            singleLine = true,
                            modifier = Modifier
                                .width(25.dp)
                                .background(
                                     Color.White,
                                    RoundedCornerShape(4.dp)
                                ),
                            textStyle = TextStyle(fontSize = 16.sp, textAlign = TextAlign.Center)
                        )
                        Text(
                            text = "_____",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )

                        Text(
                            text = encodeWord(char.toString(), mapping)[0].toString(), // Hint (letter position)
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                    }
                } else {
                    Text(
                        text = char.toString(),
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                }
            }
        }
    }
}

@Composable
fun TopInfoSection(lives: Int, mistakes: Int, level: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "❤️ $lives", style = MaterialTheme.typography.h6)
        Text(text = "Mistakes: ${"✗".repeat(mistakes)}${"○".repeat(3 - mistakes)}", style = MaterialTheme.typography.h6)
        Text(text = "Level $level", style = MaterialTheme.typography.h6)
    }
}




@Preview(showBackground = true)
@Composable
fun previewWordgimaMainScreen() {
    val sampleWords = listOf(
        WordWithHints(
            letters = listOf(
                LetterWithHint(null, 19), // E
                LetterWithHint('A', 1),
                LetterWithHint(null, 20), // T
            )
        ),
        WordWithHints(
            letters = listOf(
                LetterWithHint(null, 15), // O
                LetterWithHint(null, 22), // V
                LetterWithHint(null, 5)  // E
            )
        )
    )
    WordigmaScreen(wordsWithHints =sampleWords ) { _, _ ->

    }
}

@Composable
fun CustomKeyboard(
    onKeyPress: (String) -> Unit, // Callback for key presses
    onBackspacePress: () -> Unit,
    onEnterPress: () -> Unit
) {
    val keys = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Back", "Z", "X", "C", "V", "B", "N", "M", "Enter")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keys.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start =
                        if (rowIndex == 2) 4.dp else (rowIndex * 16).dp, // Reduce padding for the 3rd row
                        end = if (rowIndex == 2) 0.dp else (rowIndex * 16).dp // Reduce padding for the 3rd row

                    ) // Adjust start padding based on row index
                    .padding(top = 2.dp, bottom = 2.dp),
                horizontalArrangement = Arrangement.spacedBy(2.dp) // Adjust space between buttons
            ) {
                row.forEach { key ->
                    when (key) {
                        "Back" -> ActionKeyButton(
                            icon = Icons.Default.ArrowBack, // Backspace icon
                            modifier = Modifier.weight(1f),
                            onClick = { onBackspacePress() }
                        )
                        "Enter" -> ActionKeyButton(
                            icon = Icons.Default.ArrowForward, // Enter icon
                            modifier = Modifier.weight(1f),
                            onClick = { onEnterPress() }
                        )
                        else -> KeyButton(key = key, onKeyPress = { onKeyPress(key) })
                    }
                }
            }
        }
    }
}

@Composable
fun KeyButton(key: String, onKeyPress: () -> Unit) {
        Button(
            onClick = onKeyPress,
            modifier = Modifier
                .width(32.dp)
                .height(45.dp)
                .padding(0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.White,
                contentColor = Color.Black
            ),
            contentPadding = PaddingValues(0.dp) // Remove extra padding within button
        ) {
            Text(text = key, style = TextStyle(fontSize = 18.sp, fontWeight = FontWeight.Bold))
        }

}

@Composable
fun ActionKeyButton(icon: ImageVector,modifier: Modifier = Modifier, onClick: () -> Unit) {
            Button(
                onClick = onClick,
                modifier = modifier
                    .width(43.dp)
                    .height(50.dp)
                    .padding(top = 4.dp, bottom = 4.dp, start = 2.dp, end = 2.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Gray,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(0.dp) // Remove extra padding within button
            ) {
                Icon(icon, contentDescription = null)
            }
}

@Composable
fun WordRow(word: LetterWithHint, isCorrect: Boolean, onLetterInput: (String, Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .padding(4.dp)
                .background(if (isCorrect) Color.Green else Color.White),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(bottom = 4.dp, top =4.dp)) {
                Text(
                    text = word.char.toString(),
                    style = MaterialTheme.typography.h6
                )
                Text(text = "_____")
                Text(
                    text = word.hint?.toString() ?: "",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )

            }

        }
    }

}
@Composable
fun Keyboard(onKeyClick: (Char) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .background(Color.Magenta)
            .padding(8.dp)
    ) {
        val keyboardRows = listOf(
            "QWERTYUIOP",
            "ASDFGHJKL",
            "ZXCVBNM"
        )
        keyboardRows.forEach { row ->
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { letter ->
                    Button(
                        onClick = { onKeyClick(letter) },
                        modifier = Modifier
                            .width(42.dp)
                            .height(42.dp)
                            .padding(2.dp),
                        shape  = RoundedCornerShape(
                            topStart = 4.dp,
                            topEnd = 4.dp,
                            bottomEnd = 4.dp,
                            bottomStart = 4.dp
                        )
                    ) {
                        Text(
                            text = letter.toString(),
                            style = MaterialTheme.typography.body1
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

// Data Model
data class WordWithHints(
    val letters: List<LetterWithHint>
)

data class LetterWithHint(
    val char: Char?, // Correct letter or null if empty
    val hint: Int?,  // Hint number (e.g., 19 for E)
    val isCorrect: Boolean = false // If the letter input is correct
)

private fun decodeWord(
    encodedWord: List<Int>,
    mapping: Map<Int, Char>
) {
    // Reverse the mapping for decoding
    val reversedMapping = mapping.entries.associate { (key, value) -> value to key }
    println("Decoding Mapping: $reversedMapping")

    // Decode the encoded word
    val decodedWord = encodedWord.map { number ->
        reversedMapping.entries.find { it.value == number }?.key ?: error("Number not found")
    }
    println("Decoded Word: ${decodedWord.joinToString("")}")
}

private fun encodeWord(
    message: String,
    mapping: Map<Int, Char>
): List<Int> {
    // Encode the message
    val encodedWord = message.map { char ->
        mapping.entries.find { it.value == char }?.key ?: error("Letter not found")
    }
    println("Encoded Word: $encodedWord")
    return encodedWord
}

private fun getMapping(): Map<Int, Char> {
    // Use a randomized base letter
    val random = Random(System.currentTimeMillis())
    val baseChar = 'A' + random.nextInt(0, 26) // Random base letter
    println("Base Character: $baseChar")

    // Generate the mapping
    val mapping = (1..26).shuffled(random).associateWith {
        ((it - 1 + baseChar.code) % 26 + 'A'.code).toChar()
    }
    return mapping
}
