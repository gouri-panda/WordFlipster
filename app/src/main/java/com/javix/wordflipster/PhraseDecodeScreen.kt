package com.javix.wordflipster

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextGranularity.Companion.Word
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.javix.wordflipster.ui.theme.wordgimaBackgroundScreen
import com.javix.wordflipster.ui.theme.wordgimaQuoteTextColor
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.text.Typography.quote

@Preview(showBackground = true)
@Composable
fun PhraseDecodeScreen() {
    val mapping = remember { mutableStateOf(getMapping()) }
    val phrases = listOf(
        "Not a dog" to "cat",
        "American autumn" to "fall"
    )
    val phraseInputs = remember {
        phrases.map { it.second.map { "" }.toMutableStateList() }
    }.toMutableList()
////    val phraseInputs = remember {
////        mutableStateListOf(
////            *phrases.map { phrase ->
////                phrase.second.map { "" }.toMutableList()
////            }.toTypedArray()
////        )
////    }
//    val phraseInputs = remember {
//        phrases.map { phrase ->
//            phrase.second.map { mutableStateOf("") }
//        }
//    }
    val correctUserInputs: MutableSet<String> = mutableSetOf() // Track user-guessed letters

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 16.dp, bottom = 0.dp, start = 0.dp, end = 0.dp),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        TopInfoSection(lives = 7, mistakes = 1, level = 1)
        Spacer(modifier = Modifier.weight(1f))
        QuoteDisplaySection(
            quote = "DREAM BIG AND DARE TO FAIL",
            maxRowLength = 15,
            mapping = mapping.value,
            onLetterInputSubmit = {}
        ) {

        }
        Spacer(modifier = Modifier.weight(1f))
        PhraseInputSection(
            phrases = phrases,
            phraseInputs = phraseInputs,
            onLetterInputSubmit = { phraseIndex,charIndex, input ->
                val targetWord = phrases[phraseIndex].second
                if (targetWord[charIndex].toString() == input) {
                    phraseInputs[phraseIndex][charIndex] = input
                    correctUserInputs.add(input)
                     Log.d("pDecoder", "correct word $input")
                }
            }
        )
        Spacer(modifier = Modifier.weight(1f))
    }
}
@Composable
fun PhraseInputSection(
    phrases: List<Pair<String, String>>,
    phraseInputs: List<MutableList<String>>,
    onLetterInputSubmit: (Int,Int, String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        phrases.forEachIndexed { phraseIndex, (phrase, targetWord) ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = phrase, fontSize = 16.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    targetWord.forEachIndexed { charIndex, char ->
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .padding(4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            BasicTextField(
                                value = phraseInputs[phraseIndex][charIndex],
                                onValueChange = { input ->
                                    if (input.length <= 1) {
                                        onLetterInputSubmit(phraseIndex, charIndex, input)
                                    }
                                },
                                textStyle = TextStyle(
                                    fontSize = 16.sp,
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                            )
                        }
                    }
                }
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
                    BasicTextField(
                        value = if (currentWrongChar.isNotEmpty() && isFocused) {
                            if (!isAnimating.value) {
                                coroutineScope.launch {
                                    triggerShakeAnimation()
                                    delay(50)
                                    hideTextAfterAnimation()
                                }
                            }
                            currentWrongChar

                        } else userInput[charIndex],
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