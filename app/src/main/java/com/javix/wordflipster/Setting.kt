package com.javix.wordflipster

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.Role.Companion.Switch
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val settingViewModel: SettingViewModel = viewModel()
    val musicEnabled by settingViewModel.musicEnabled.collectAsState()
    val vibrationEnabled by settingViewModel.vibrationEnabled.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Settings", style = MaterialTheme.typography.h5)

        Spacer(modifier = Modifier.height(24.dp))

        // Music Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Music Audio")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = musicEnabled,
                onCheckedChange = {
                    settingViewModel.saveMusicPreference(it) // Save the preference
                },
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Vibration Toggle
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Vibration on Wrong Letter")
            Spacer(modifier = Modifier.weight(1f))
            Switch(
                checked = vibrationEnabled,
                onCheckedChange = {
                    settingViewModel.saveVibrationPreference(it) // Save the preference
                },
                modifier = Modifier.padding(start = 16.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Help Section
        Text(text = "Help", style = MaterialTheme.typography.h6)
        Text(
            text = "Toggle the music audio to play background music while you play the game.\n" +
                    "Enable vibration to receive feedback when you input the wrong letter.",
            style = MaterialTheme.typography.body2
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Share App Button
        Button(onClick = { onShareApp() }) {
            Text(text = "Share the App")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // User Feedback Button
        Button(onClick = { sendFeedback(context) }) {
            Text(text = "Send Feedback")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // App Version
        Text(
            text = "Version: 1.0",
            style = MaterialTheme.typography.body2
        ) //Todo: change the app version
    }
}

fun sendFeedback(context: Context) {
    val emailIntent = Intent(Intent.ACTION_SENDTO).apply {
        data = Uri.parse("mailto:") // Only email apps should handle this
        putExtra(Intent.EXTRA_EMAIL, arrayOf("gouripanda4@gmail.com"))
        putExtra(Intent.EXTRA_SUBJECT, "Feedback for WordFlipster")
        putExtra(Intent.EXTRA_TEXT, "Enter your feedback here...")
    }
    context.startActivity(Intent.createChooser(emailIntent, "Send Feedback"))
}

fun onShareApp() {

}