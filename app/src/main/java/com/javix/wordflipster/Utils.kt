package com.javix.wordflipster

fun calculateAverageTime(totalTimeMillis: Long, correctWords: Int): Double {
    return if (correctWords > 0) {
        (totalTimeMillis.toDouble() / correctWords) / 1000 // Convert to seconds
    } else {
        0.0 // Avoid division by zero
    }
}

fun convertReadableTimeToString(time: Double) : String{
    return String.format("%.2f seconds", time)  // Format to 5 decimal places
}
