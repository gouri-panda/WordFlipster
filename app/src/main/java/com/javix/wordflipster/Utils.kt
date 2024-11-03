package com.javix.wordflipster

fun calculateAverageTime(totalTimeMillis: Long, correctWords: Int): Double {
    return if (correctWords > 0) {
        ((totalTimeMillis/ 1000)  / correctWords).toDouble()   // Convert to seconds
    } else {
        0.0 // Avoid division by zero
    }
}

fun convertReadableTimeToString(time: Double) : String{
    return String.format("%.5f", time)  // Format to 5 decimal places
}
