package com.javix.wordflipster

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import kotlin.random.Random

@Composable
fun WordFinderGameScreen(wordsToFind: List<String>) {
    val gridLetters = generateRandomGrid(wordsToFind)
    val selectedLetters = remember { mutableStateListOf<Pair<Int, Int>>() }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        // Word Grid
        WordGrid(gridLetters, selectedLetters)

        Spacer(modifier = Modifier.height(20.dp))

        // Found Words List
        Text("Words to Find", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        wordsToFind.forEach { word ->
            Text(word, color = if (isWordFound(word, selectedLetters, gridLetters)) Color.Green else Color.Black)
        }
    }
}

@Composable
fun WordGrid(
    gridLetters: List<List<Char>>,
    selectedLetters: MutableList<Pair<Int, Int>>
) {
    val gridSize = gridLetters.size

    Column {
        for (row in 0 until gridSize) {
            Row(horizontalArrangement = Arrangement.Center) {
                for (col in 0 until gridSize) {
                    LetterBox(
                        letter = gridLetters[row][col],
                        isSelected = selectedLetters.contains(row to col),
                        onSelect = { toggleSelection(row to col, selectedLetters) },
                        onDragSelect = { toggleSelection(row to col, selectedLetters) }
                    )
                }
            }
        }
    }
}

@Composable
fun LetterBox(
    letter: Char,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onDragSelect: () -> Unit
) {
    val color by animateColorAsState(
        targetValue = if (isSelected) Color.Cyan else Color.LightGray
    )

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .padding(4.dp)
            .size(48.dp)
            .background(color, shape = CircleShape)
            .pointerInput(Unit) {
                detectDragGestures(
                    onDrag = { _, _ -> onDragSelect() },
                    onDragStart = {onSelect()}
                )
            }
    ) {
        Text(text = letter.toString(), fontSize = 20.sp, fontWeight = FontWeight.Bold)
    }
}

fun generateRandomGrid(words: List<String>): List<List<Char>> {
    val gridSize = 10
    val grid = MutableList(gridSize) { MutableList(gridSize) { ' ' } }

    words.forEach { word ->
        placeWordInGrid(word, grid)
    }

    // Fill remaining spaces with random letters
    grid.forEach { row ->
        row.replaceAll { if (it == ' ') ('A'..'Z').random() else it }
    }

    return grid
}

fun placeWordInGrid(word: String, grid: MutableList<MutableList<Char>>) {
    val directions = listOf(
        0 to 1, // horizontal
        1 to 0, // vertical
        1 to 1, // diagonal down-right
        -1 to 1 // diagonal up-right
    )
    val gridSize = grid.size
    var placed = false

    while (!placed) {
        val direction = directions.random()
        val row = Random.nextInt(gridSize)
        val col = Random.nextInt(gridSize)

        if (canPlaceWord(word, row, col, direction, grid)) {
            for (i in word.indices) {
                grid[row + i * direction.first][col + i * direction.second] = word[i]
            }
            placed = true
        }
    }
}

fun canPlaceWord(word: String, row: Int, col: Int, direction: Pair<Int, Int>, grid: List<List<Char>>): Boolean {
    val gridSize = grid.size
    for (i in word.indices) {
        val newRow = row + i * direction.first
        val newCol = col + i * direction.second

        if (newRow !in 0 until gridSize || newCol !in 0 until gridSize || (grid[newRow][newCol] != ' ' && grid[newRow][newCol] != word[i])) {
            return false
        }
    }
    return true
}

fun toggleSelection(position: Pair<Int, Int>, selectedLetters: MutableList<Pair<Int, Int>>) {
    if (position in selectedLetters) {
        selectedLetters.remove(position)
    } else {
        selectedLetters.add(position)
    }
}

fun isWordFound(
    word: String,
    selectedLetters: List<Pair<Int, Int>>,
    gridLetters: List<List<Char>>
): Boolean {
    val selectedWord = selectedLetters.map { (row, col) -> gridLetters[row][col] }.joinToString("")
    return selectedWord.contains(word) // Checks if word is found within selection
}

@Preview(showBackground = true)
@Composable
fun WordFinderGameScreenPreview() {
    val wordsToFind = listOf("APPLE", "BANANA", "CHERRY", "DATE", "FIG","STORY")
    WordFinderGameScreen(wordsToFind)
}