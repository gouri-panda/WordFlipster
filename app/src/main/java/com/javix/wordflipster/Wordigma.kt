package com.javix.wordflipster

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.javix.wordflipster.ui.theme.fooCustomKeyboardContentColor
import com.javix.wordflipster.ui.theme.fooCustomKeyboardKeyButtonBackgroundColor
import com.javix.wordflipster.ui.theme.foocustomKeyboardBackgroundColor
import com.javix.wordflipster.ui.theme.wordgimaBackgroundScreen
import com.javix.wordflipster.ui.theme.wordgimaQuoteTextColor
import com.javix.wordflipster.ui.theme.wordgimaTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun WordigmaScreen(navHostController: NavHostController) {
    WordigmaBaseScreen {
        val context = LocalContext.current

        BackHandler {
            navHostController.popBackStack()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            val isCompleteScreen = remember { mutableStateOf(false) }
            val mistakes = remember { mutableStateOf(0) }
            val level = remember { mutableStateOf(0) }

            val mapping = remember { mutableStateOf(getMapping()) }
            val quotes = arrayListOf<String>()
            quotes.add("WHERE THERE IS LOVE THERE IS LIFE")
            quotes.add("DREAM BIG AND DARE TO FAIL.")
            quotes.add("BE THE CHANGE.")
            quotes.add("LIVE AND LET LIVE.")
            quotes.add("THIS TOO SHALL PASS.")
            quotes.add("NEVER GIVE UP.")
            quotes.add("SIMPLE IS BEAUTIFUL.")
            quotes.add("TIME HEALS ALL WOUNDS.")
            quotes.add("LESS IS MORE.")
            quotes.add("STAY POSITIVE.")
            quotes.add("HOPE NEVER DIES.")
            if (isCompleteScreen.value) {
                PhraseEndingScreen(level = level.value, quote = quotes[level.value] , timer = "1:40") {
                    level.value += 1
                    isCompleteScreen.value = false
                }
            } else {
                TopInfoSection(lives = 7, mistakes = mistakes.value, level = level.value)

                QuoteDisplaySection(
                    quote = quotes[level.value],
                    maxRowLength = 13,
                    mapping = mapping.value,
                    onLetterInputSubmit = { correct ->
                        if (!correct) {
                            if (mistakes.value < 3) {
                                mistakes.value += 1
                            } else if (mistakes.value == 3) {
                                mistakes.value = 0
                            }
                        }

                    },
                    levelCompleteListener = {
                        mapping.value = getMapping()
                        mistakes.value = 0
                        if (level.value < quotes.size - 1) {
                            isCompleteScreen.value = true

                        } else if (level.value == quotes.size - 1) {
                            Toast.makeText(
                                context,
                                "Congratulations! You've completed the challenge.",
                                Toast.LENGTH_SHORT
                            ).show()
                            level.value = 0
                        }
                    })
            }
        }
    }


}@Composable
private fun QuoteDisplaySection(
    quote: String,
    commonLetterCount: Int = 2,
    maxRowLength: Int,
    mapping: Map<Int, Char>,
    onLetterInputSubmit: (Boolean) -> Unit,
    levelCompleteListener: () -> Unit
) {
    val words = quote.split(" ")
    val letterFrequency = words.joinToString("").groupingBy { it.lowercaseChar() }.eachCount()
    val commonLetters = letterFrequency
        .filter { it.key.isLetter() }
        .toList()
        .sortedByDescending { it.second }
        .take(commonLetterCount)
        .map { it.first }

    val commonDistinctLetters = commonLetters.distinct()

    var hiddenIndices = words.flatMapIndexed { wordIndex, word ->
        word.mapIndexedNotNull { charIndex, char ->
            if (commonLetters.contains(char.lowercaseChar())) Triple(wordIndex, charIndex, word) else null
        }
    }

    var userInputs = remember(quote) {
        words.map { word ->
            word.map { char -> if (commonLetters.contains(char.lowercaseChar())) "" else char.toString() }.toMutableList()
        }
    }

    // Track the current focus globally
    val currentFocusIndex = remember(quote) { mutableStateOf(0) }

    val currentWrongChar = remember(quote) { mutableStateOf("") }

    var rows = mutableListOf<List<Int>>()
    var currentRow = mutableListOf<Int>()
    var currentLength = 0

    // Group words into rows based on the max row length
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

        // Render rows of words
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
                        currentFocusIndex = currentFocusIndex.value,
                        mapping = mapping,
                        globalHiddenIndices = hiddenIndices,
                        wordIndex = wordIndex,
                        currentWrongChar = currentWrongChar.value,
                        onValueChange = { wordIdx, charIdx, input ->
                            if (input.length == 1) {
                                userInputs[wordIdx][charIdx] = input
                                val nextFocus = hiddenIndices.indexOfFirst {
                                    it.first == wordIdx && it.second == charIdx
                                } + 1
                                if (nextFocus < hiddenIndices.size) {
                                    currentFocusIndex.value = nextFocus
                                }
                            }
                        },
                        onBoxClick = { wordIdx, charIdx ->
                            val newFocusIndex = hiddenIndices.indexOfFirst {
                                it.first == wordIdx && it.second == charIdx
                            }
                            if (newFocusIndex != -1) {
                                currentFocusIndex.value = newFocusIndex
                            }
                        },
                        hideTextAfterAnimation = {
                            currentWrongChar.value = ""
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.weight(1f)) // Push content downward

        // Render the custom keyboard
        CustomKeyboard(onKeyPress = { char ->
            if (currentFocusIndex.value < hiddenIndices.size) {
                val (wordIndex, charIndex, _) = hiddenIndices[currentFocusIndex.value]
                val actualChar = words[wordIndex][charIndex]

                if (userInputs[wordIndex][charIndex].isEmpty()) { // Prevent overwriting existing input
                    userInputs[wordIndex][charIndex] = char.toString()
                    if(userInputs[wordIndex][charIndex] == actualChar.toString()) {
                        currentWrongChar.value = ""
                        val nextFocus = currentFocusIndex.value + 1
                        if (nextFocus <= hiddenIndices.size) {
                            currentFocusIndex.value = nextFocus
                        }
                        if(checkAllTheInputsCompleted(userInputs)) {
                            levelCompleteListener()
                        }
                    } else {
                        currentWrongChar.value = userInputs[wordIndex][charIndex]
                        userInputs[wordIndex][charIndex] = ""
                        onLetterInputSubmit(false)
                    }
                }
            }
        }, onBackspacePress = {
            if (currentFocusIndex.value > 0) {
                val (wordIndex, charIndex, _) = hiddenIndices[currentFocusIndex.value]
                userInputs[wordIndex][charIndex] = ""
                currentFocusIndex.value -= 1
            }
        }, onEnterPress = {
            val nextFocus = currentFocusIndex.value + 1
            if (nextFocus < hiddenIndices.size) {
                currentFocusIndex.value = nextFocus
            }else if(nextFocus == hiddenIndices.size) {
                currentFocusIndex.value = 0 // Todo: fix this not move to 0 position but to the next focus index
            }
        }, keyBackgrounds = mapOf(*keyMappedColors(commonDistinctLetters).toTypedArray()))
    }
}
fun keyMappedColors(keys: List<Char>): List<Pair<String, Color>> {
    return keys.mapNotNull { char ->
        char.toUpperCase().toString() to Color.Green
    }
}

@Composable
private fun WordRow(
    word: String,
    commonLetters: List<Char>,
    userInput: List<String>,
    mapping: Map<Int, Char>,
    hiddenIndices: List<Triple<Int, Int, String>>,
    currentFocusIndex: Int,
    currentWrongChar: String,
    globalHiddenIndices: List<Triple<Int, Int, String>>,
    wordIndex: Int,
    onValueChange: (Int, Int, String) -> Unit,
    onBoxClick: (Int, Int) -> Unit,
    hideTextAfterAnimation:() -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(0.dp), // Adjusted spacing for a more natural look
        verticalAlignment = Alignment.CenterVertically
    ) {
        val coroutineScope = rememberCoroutineScope()
        val shakeOffset = remember { Animatable(0f) }
        var isAnimating = remember { mutableStateOf(false) } // Flag to control animation


        suspend fun triggerShakeAnimation() {
            isAnimating.value = true
            shakeOffset.animateTo(
                targetValue = 1f, // Shake distance
                animationSpec = tween(durationMillis = 10, easing = LinearEasing)
            )
            shakeOffset.animateTo(
                targetValue = -1f,
                animationSpec = tween(durationMillis = 10, easing = LinearEasing)
            )
            shakeOffset.animateTo(
                targetValue = 0f,
                animationSpec = tween(durationMillis = 10, easing = LinearEasing)
            )
            isAnimating.value = false
        }


        word.forEachIndexed { charIndex, char ->
            val shouldHide = commonLetters.contains(char.lowercaseChar())
            val globalIndex = globalHiddenIndices.indexOfFirst { it.first == wordIndex && it.second == charIndex }
            val isFocused = currentFocusIndex == globalIndex

            Box(
                modifier = Modifier
                    .padding(0.dp)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = when {
                            shouldHide && isFocused && currentWrongChar.isNotEmpty() -> Color.Red
                            shouldHide && isFocused -> Color.Green
                            shouldHide -> wordgimaBackgroundScreen
                            else -> Color.Transparent
                        },
                        shape = RoundedCornerShape(4.dp)
                    )
                   ,
                contentAlignment = Alignment.Center
            ) {
                if (shouldHide) {
                    Column(
                        verticalArrangement = Arrangement.spacedBy(0.dp)
                    ) {
                        BasicTextField(
                            value = if(currentWrongChar.isNotEmpty() && isFocused){
                                if(!isAnimating.value) {
                                coroutineScope.launch {
                                    triggerShakeAnimation()
                                    delay(50)
                                    hideTextAfterAnimation()
                                }
                                    }
                                currentWrongChar

                            } else if (charIndex < userInput.size) userInput[charIndex] else "",
                            onValueChange = { input ->
                                if (input.length <= 1) {
                                    val wordIdx =
                                        hiddenIndices.firstOrNull { it.second == charIndex }?.first
                                            ?: return@BasicTextField
                                    onValueChange(wordIdx, charIndex, input)
                                }
                            },
                            singleLine = true,
                            readOnly = true,
                            modifier = Modifier
                                .width(18.dp)
                                .height(33.dp)
                                .offset(x = if (shouldHide && isFocused && currentWrongChar.isNotEmpty()) shakeOffset.value.dp else 0.dp)
                                .fillMaxWidth()
                                .padding(bottom = 0.dp, start = 0.dp, top = 8.dp),
                                    textStyle = TextStyle(
                                fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = wordgimaQuoteTextColor,
                                textAlign = TextAlign.Center
                            )
                        )
                        Divider(
                            modifier = Modifier
                                .width(18.dp)
                                .clickable {
                                    if (shouldHide) onBoxClick(
                                        wordIndex,
                                        charIndex
                                    ) // Todo:: Add this in more appropriate place
                                },
                            color = wordgimaQuoteTextColor
                        )

                        Text(
                            text = encodeWord(
                                char.toString(),
                                mapping
                            )[0].toString(), // Hint (letter position)
                            fontSize = 12.sp,
                            color = wordgimaQuoteTextColor,
                            modifier = Modifier
                                .padding(start = 4.dp)
                                .clickable {
                                    if (shouldHide) onBoxClick(wordIndex, charIndex)
                                }
                        )
                    }

                } else {
                    if(char.toString() == ".") {
                        Text(
                            text = char.toString(),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = wordgimaQuoteTextColor
                        )
                    }else {
                        Column {
                            Text(
                                text = char.toString(),
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = wordgimaQuoteTextColor,
                                modifier = Modifier
                                    .width(18.dp)
                                    .height(33.dp)
                                    .padding(bottom = 0.dp, start = 0.dp, top = 8.dp)
                            )
                            Divider(
                                modifier = Modifier
                                    .width(18.dp), // Makes the line span the entire width
                                color = wordgimaQuoteTextColor
                            )
                            Text(
                                text = encodeWord(
                                    char.toString(),
                                    mapping
                                )[0].toString(), // Hint (letter position)
                                fontSize = 12.sp,
                                color = wordgimaQuoteTextColor,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}



@Composable
fun TopInfoSection(lives: Int, mistakes: Int, level: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 8.dp, end = 8.dp, top = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

//        Text(text = "❤️ $lives", style = MaterialTheme.typography.h6) // Todo: Add this When we'll  implement AD
        Row(
                modifier = Modifier.weight(1f), // Take available space
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
        ) {
        val mistakeSymbols = buildAnnotatedString {
            repeat(mistakes) {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("✗")
                }
            }
            repeat(3 - mistakes) {
                withStyle(style = SpanStyle(color = wordgimaQuoteTextColor)) {
                    append("○")
                }
            }
        }
        Text(
            text = AnnotatedString("Mistakes: ", spanStyles = listOf()) + mistakeSymbols,
            style = MaterialTheme.typography.h6,
            textAlign = TextAlign.Center,
            color = wordgimaQuoteTextColor
        )
    }

        Text(text = "Level $level", style = MaterialTheme.typography.h6, color = wordgimaQuoteTextColor)
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
}

@Composable
fun CustomKeyboard(
    onKeyPress: (String) -> Unit,
    onBackspacePress: () -> Unit,
    onEnterPress: () -> Unit,
    disabledKeys: List<String> = emptyList(), // Keys to disable
    keyBackgrounds: Map<String, Color> = emptyMap() // Custom backgrounds for keys
) {
    val keys = listOf(
        listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"),
        listOf("A", "S", "D", "F", "G", "H", "J", "K", "L"),
        listOf("Back", "Z", "X", "C", "V", "B", "N", "M", "Enter")
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(foocustomKeyboardBackgroundColor)
            .padding(bottom = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        keys.forEachIndexed { rowIndex, row ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        start =
                        if (rowIndex == 2) 4.dp else (rowIndex * 16).dp, // Reduce padding for the 3rd row
                        end = if (rowIndex == 2) 4.dp else (rowIndex * 16).dp, // Reduce padding for the 3rd row

                    ) // Adjust start padding based on row index
                    .padding(top = 2.dp, bottom = 4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                row.forEach { key ->
                    val isDisabled = disabledKeys.contains(key)
                    val backgroundColor = keyBackgrounds[key] ?: fooCustomKeyboardKeyButtonBackgroundColor // Default background  content color

                    when (key) {
                        "Back" -> ActionKeyButton(
                            icon = Icons.AutoMirrored.Filled.ArrowBack,
                            modifier = Modifier
                                .background(if (isDisabled) Color.LightGray else backgroundColor),
                            onClick = {
                                if (!isDisabled) onBackspacePress()
                            },
                            enabled = !isDisabled
                        )
                        "Enter" -> ActionKeyButton(
                            icon = Icons.AutoMirrored.Filled.ArrowForward,
                            modifier = Modifier
                                .background(if (isDisabled) Color.LightGray else backgroundColor)
                                .clip(RoundedCornerShape(8.dp))
                                .shadow(4.dp, RoundedCornerShape(8.dp)),
                            onClick = {
                                if (!isDisabled) onEnterPress()
                            },
                            enabled = !isDisabled
                        )
                        else -> KeyButton(
                            key = key,
                            onKeyPress = {
                                if (!isDisabled) onKeyPress(key)
                            },
                            backgroundContentColor = backgroundColor,
                            contentColor = fooCustomKeyboardContentColor,
                            isDisabled = isDisabled
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun KeyButton(key: String, onKeyPress: () -> Unit, backgroundContentColor: Color, contentColor: Color,isDisabled: Boolean) {
        Button(
            onClick = onKeyPress,
            modifier = Modifier
                .width(30.dp)
                .height(45.dp)
                .padding(0.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = backgroundContentColor,
                contentColor = contentColor
            ),
            shape = RoundedCornerShape(8.dp), // Rounded corners for modern look
            elevation = ButtonDefaults.elevation(4.dp),
            enabled = !isDisabled,
            contentPadding = PaddingValues(0.dp), // Remove extra padding within button
            content = {
                    Box(
                        contentAlignment = Alignment.Center, // Center the text in the box
                        modifier = Modifier.fillMaxSize()
                    ) {
                        Text(
                            text = key,
                            style = TextStyle(
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                lineHeight = 18.sp
                            ),
                            modifier = Modifier.padding(0.dp) // Ensure no extra padding for the text
                        )
                    }
            }
        )

}

@Composable
fun ActionKeyButton(icon: ImageVector,modifier: Modifier = Modifier, onClick: () -> Unit, enabled: Boolean) {
            Button(
                onClick = onClick,
                modifier = modifier
                    .width(45.dp)
                    .height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = fooCustomKeyboardKeyButtonBackgroundColor,
                    contentColor = fooCustomKeyboardContentColor
                ),
                elevation = ButtonDefaults.elevation(4.dp),
                enabled = enabled,
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

fun encodeWord(
    message: String,
    mapping: Map<Int, Char>
): List<Int> {
    // Encode the message
    if (message == "") return listOf(-1)
    val encodedWord = message.map { char ->
        val capChar = char.uppercaseChar()
        mapping.entries.find { it.value == capChar }?.key ?: -1
    }
    println("Encoded Word: $encodedWord")
    return encodedWord
}

fun getMapping(): Map<Int, Char> {
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

fun checkAllTheInputsCompleted(userInputs: List<MutableList<String>>): Boolean {
    for (word in userInputs) {
        if (word.any { it.isEmpty() }) {
            return false
        }
    }
    return true
}


