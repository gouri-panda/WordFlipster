package com.javix.wordflipster

import android.content.Context
import java.io.IOException
import java.nio.charset.Charset

fun calculateAverageTime(totalTimeMillis: Long, correctWords: Int): Double {
    return if (correctWords > 0) {
        (totalTimeMillis.toDouble() / correctWords) / 1000 // Convert to seconds
    } else {
        0.0 // Avoid division by zero
    }
}

fun convertReadableTimeToString(time: Double) : String {
    return String.format("%.2f", time)  // Format to 5 decimal places
}

fun loadJSONFromAsset(context: Context, fileName: String): String? {
    return try {
        val inputStream = context.assets.open(fileName)
        val size = inputStream.available()
        val buffer = ByteArray(size)
        inputStream.read(buffer)
        inputStream.close()
        String(buffer, Charset.forName("UTF-8"))
    } catch (e: IOException) {
        e.printStackTrace()
        null
    }
}
