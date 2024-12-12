package com.javix.wordflipster

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextGranularity.Companion.Word
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.javix.wordflipster.ui.theme.wordgimaBackgroundScreen
import com.javix.wordflipster.ui.theme.wordgimaQuoteTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.Locale

@Preview(showBackground = true)
@Composable
fun PhraseDecodeScreen() {
    WordigmaBaseScreen {
        val viewModel: PhraseDecodeScreenViewModel = viewModel()
        val phrasesState by viewModel.phrases.collectAsState()
        val mapping = remember { mutableStateOf(getMapping()) }

        val correctUserInputs =
            remember { mutableStateOf(setOf<String>()) } // Track user-guessed letters

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 16.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            TopInfoSection(lives = 7, mistakes = 1, level = 1)
            Spacer(modifier = Modifier.weight(1f))
            QuoteDisplaySection(
                quote = "DREAM BIG AND DARE TO FAIL CAT",
                maxRowLength = 15,
                mapping = mapping.value,
                correctUserInputs = correctUserInputs,
                onLetterInputSubmit = {}
            ) {

            }
            Spacer(modifier = Modifier.weight(1f))
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center // Center content in the screen
            ) {
                if (phrasesState.isEmpty()) {
                    AnimatedLoadingText()
                } else {
                    // Prepare phrase inputs and track correct user inputs
                    val phraseInputs = remember {
                        phrasesState.map { it.answer.map { "" }.toMutableStateList() }
                    }.toMutableList()

                    // Generate a valid set of phrases where all answer letters are in the quote
                    val validPhrases = viewModel.getValidPhrases(phrasesState)

                    // Generate a quote based on valid phrases
                    val quote = viewModel.generateQuote(validPhrases)
                    PhraseInputSection(
                        phrases = viewModel.phrases.value,
                        phraseInputs = phraseInputs,
                        correctUserInputs = correctUserInputs,
                        onLetterInputSubmit = { phraseIndex, charIndex, input ->
                            val targetWord = viewModel.phrases.value[phraseIndex].answer
                            if (targetWord[charIndex].uppercaseChar().toString() == input) {
                                phraseInputs[phraseIndex][charIndex] = input
                                correctUserInputs.value += input
                                Log.d(
                                    "pDecoder",
                                    "correct word $input and ${correctUserInputs.value.toList()}"
                                )
                            }
                        }
                    )
                }
            }
            Spacer(modifier = Modifier.weight(1f))
        }
    }
}
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun PhraseInputSection(
    phrases: List<Phrase>,
    phraseInputs: List<MutableList<String>>,
    correctUserInputs: MutableState<Set<String>>,
    onLetterInputSubmit: (Int,Int, String) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val focusManager = LocalFocusManager.current
    val focusRequesters = remember {
        phrases.map { phrase ->
            List(phrase.answer.length) { FocusRequester() }
        }
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(phrases)  { phraseIndex, (phrase, targetWord) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = phrase,
                    fontSize = 16.sp,
                    modifier = Modifier
                        .padding(16.dp)
                        .width(100.dp)
                        .wrapContentWidth(),
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Medium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colors.onSurface
                    ),
                )
                Spacer(modifier = Modifier.weight(1f))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(end = 8.dp)
                ) {
                    targetWord.forEachIndexed { charIndex, char ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            val input = if(correctUserInputs.value.contains(char.uppercaseChar().toString())) {
                                phraseInputs[phraseIndex][charIndex] = char.uppercaseChar().toString()
                                char.uppercaseChar().toString()
                            } else phraseInputs[phraseIndex][charIndex]
                            BasicTextField(
                                value = input,
                                onValueChange = {input ->
                                    if (input.length <= 1) {
                                        // Move focus to the next box if correct
                                        if (targetWord[charIndex].toString()
                                                .uppercase(Locale.getDefault()) == input) {
                                            val nextIndex = charIndex + 1
                                            if (nextIndex < targetWord.length) {
                                                // Move to the next box in the same word If it's not empty
                                                var nextFocusBoxIndex = nextIndex
//                                                while(correctUserInputs.value.contains(phraseInputs[phraseIndex][nextFocusBoxIndex]) && nextFocusBoxIndex <= targetWord.length) {
//                                                    nextFocusBoxIndex += 1
//                                                } // Todo fix this
                                                if (nextFocusBoxIndex == targetWord.length){
                                                    coroutineScope.launch {
                                                        focusManager.clearFocus()
                                                    }
                                                }
                                                focusRequesters[phraseIndex][nextFocusBoxIndex].requestFocus() //TODO:// Fix the last word's last letter

                                            } else {
                                                // Move to the first box of the next word
                                                val nextPhraseIndex = phraseIndex + 1
                                                if (nextPhraseIndex < phrases.size) {
                                                    val nextWordInputs = phraseInputs[nextPhraseIndex]
                                                    val nextFocusBoxIndex = nextWordInputs.indexOfFirst { it.isEmpty() }
                                                    if (nextFocusBoxIndex != -1) {
                                                        // Focus the first empty box in the next word
                                                        focusRequesters[nextPhraseIndex][nextFocusBoxIndex].requestFocus()
                                                    }
                                                }
                                            }
                                        }
                                        onLetterInputSubmit(phraseIndex, charIndex, input)

                                    }
                                },
                                modifier = Modifier.focusRequester(focusRequesters[phraseIndex][charIndex]),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    capitalization = KeyboardCapitalization.Characters // Forces all input to be capital letters
                                ),
                                keyboardActions = KeyboardActions.Default,
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun QuoteDisplaySection(
    quote: String,
    commonLetterCount: Int = 2,
    maxRowLength: Int,
    mapping: Map<Int, Char>,
    correctUserInputs: MutableState<Set<String>>,
    onLetterInputSubmit: (Boolean) -> Unit,
    levelCompleteListener: () -> Unit
) {
    val words = quote.split(" ")


    var hiddenIndices = words.flatMapIndexed { wordIndex, word ->
        word.mapIndexedNotNull { charIndex, char ->
            Triple(wordIndex, charIndex, word)
        }
    }

    var userInputs = remember(quote) {
        words.map { word ->
            word.map { char -> "" }.toMutableList()
        }
    }

    // Track the current focus globally
    val currentFocusIndex = remember(quote) { mutableStateOf(0) }

    val currentWrongChar = remember(quote) { mutableStateOf("") }


    var rows = mutableListOf<List<Int>>()
    var currentRow = mutableListOf<Int>()
    var currentLength = 0
    val globalFocusRequesters = remember { List(hiddenIndices.size) { FocusRequester() } }

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
            .fillMaxWidth()
            .padding(top = 32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Render rows of words
        for (row in rows) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(32.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                for (wordIndex in row) {
                    Word(
                        word = words[wordIndex],
                        userInput = userInputs[wordIndex],
                        hiddenIndices = hiddenIndices.filter { it.first == wordIndex },
                        currentFocusIndex = currentFocusIndex.value,
                        mapping = mapping,
                        correctUserInputs = correctUserInputs,
                        globalHiddenIndices = hiddenIndices,
                        focusRequesters = globalFocusRequesters,
                        wordIndex = wordIndex,
                        currentWrongChar = currentWrongChar.value,
                        onValueChange = { wordIdx, charIdx, input ->
                            if (input.length <= 1) {
                                userInputs[wordIdx][charIdx] = input
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
    }
}


@Composable
private fun Word(
    word: String,
    userInput: List<String>,
    mapping: Map<Int, Char>,
    hiddenIndices: List<Triple<Int, Int, String>>,
    currentFocusIndex: Int,
    focusRequesters: List<FocusRequester>,
    currentWrongChar: String,
    correctUserInputs: MutableState<Set<String>>,
    globalHiddenIndices: List<Triple<Int, Int, String>>,
    wordIndex: Int,
    onValueChange: (Int, Int, String) -> Unit,
    onBoxClick: (Int, Int) -> Unit,
    hideTextAfterAnimation: () -> Unit
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
            val globalIndex =
                globalHiddenIndices.indexOfFirst { it.first == wordIndex && it.second == charIndex }
            val isFocused = currentFocusIndex == globalIndex

            Box(
                modifier = Modifier
                    .padding(0.dp)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = when {
                            isFocused && currentWrongChar.isNotEmpty() -> Color.Red
                            isFocused -> Color.Green
                            else -> wordgimaBackgroundScreen
                        },
                        shape = RoundedCornerShape(4.dp)
                    )
                    .focusRequester(focusRequesters[globalIndex]),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    val inputValue = when {
                        currentWrongChar.isNotEmpty() && isFocused && !isAnimating.value -> currentWrongChar
                        correctUserInputs.value.contains(char.toString()) -> char.toString()
                        else -> userInput[charIndex]  // Empty input when neither condition is met
                    }
                    BasicTextField(
                        value = inputValue,
                        onValueChange = { input ->
                            if (input.length <= 1) {

                                val wordIdx =
                                    hiddenIndices.firstOrNull { it.second == charIndex }?.first
                                        ?: return@BasicTextField

                                // Focus transitions
                                if (input.isNotEmpty()) {
                                    if (globalIndex < focusRequesters.size - 1) {
                                        focusRequesters[globalIndex + 1].requestFocus()
                                    }
                                }
                                    onValueChange(wordIdx, charIndex, input)
                            }
                        },
                        singleLine = true,
                        modifier = Modifier
                            .width(18.dp)
                            .height(33.dp)
                            .offset(x = if (isFocused && currentWrongChar.isNotEmpty()) shakeOffset.value.dp else 0.dp)
                            .fillMaxWidth()
                            .padding(bottom = 0.dp, start = 0.dp, top = 8.dp)
                            .focusRequester(focusRequesters[globalIndex])
                            .onFocusChanged {
                                if (it.isFocused) {
                                    onBoxClick(wordIndex, charIndex)
                                }
                            },
                        keyboardOptions = KeyboardOptions.Default.copy(
                            capitalization = KeyboardCapitalization.Characters // Forces all input to be capital letters
                        ),
                        keyboardActions = KeyboardActions.Default,
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
                                onBoxClick(
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
                                onBoxClick(wordIndex, charIndex)
                            }
                    )
                }
            }
        }
    }
}
@Composable
fun AnimatedLoadingText() {
    val loadingTexts = listOf("Loading phrases.", "Loading phrases..", "Loading phrases...")
    var currentIndex by remember { mutableStateOf(0) }

    // Use LaunchedEffect to update the index periodically
    LaunchedEffect(Unit) {
        while (true) {
            delay(100L) // Change text every 500 milliseconds
            currentIndex = (currentIndex + 1) % loadingTexts.size
        }
    }

    Text(
        text = loadingTexts[currentIndex],
        style = MaterialTheme.typography.h6,
        textAlign = TextAlign.Center,
        modifier = Modifier.animateContentSize()
    )
}