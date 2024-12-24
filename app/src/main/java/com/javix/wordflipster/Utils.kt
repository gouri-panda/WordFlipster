package com.javix.wordflipster

import android.content.Context
import android.media.browse.MediaBrowser
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
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

@Composable
fun BackgroundMusic(mediaResource: Int, play: Boolean) {
    val context = LocalContext.current
    val player = remember {
        ExoPlayer.Builder(context).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.packageName}/$mediaResource")
            setMediaItem(mediaItem)
            prepare()
            repeatMode = Player.REPEAT_MODE_ONE
        }
    }

    LaunchedEffect(play) {
        if (play) player.play() else player.pause()
    }

    DisposableEffect(Unit) {
        onDispose { player.release() }
    }
}

