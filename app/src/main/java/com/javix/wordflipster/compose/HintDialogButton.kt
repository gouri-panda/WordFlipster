package com.javix.wordflipster.compose

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun HintDialogButton(hint: String) {
    var showHintDialog by remember { mutableStateOf(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        // Fancy Hint Button
        Button(
            onClick = { showHintDialog = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF4CAF50),
                contentColor = Color.White
            ),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "💡", style = MaterialTheme.typography.bodyMedium)
        }

        // Hint Dialog
        if (showHintDialog) {
            AlertDialog(
                onDismissRequest = { showHintDialog = false },
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Hint Icon",
                            tint = Color(0xFFFFA000),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Need a Hint?",
                            style = MaterialTheme.typography.titleLarge.copy(
                                color = Color(0xFF4CAF50)
                            )
                        )
                    }
                },
                text = {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.DarkGray
                            )
                        )
                        Text(
                            text = "Keep going, you're doing great! 🌟",
                            style = MaterialTheme.typography.bodySmall.copy(
                                color = Color(0xFF9E9E9E)
                            )
                        )
                    }
                },
                confirmButton = {
                    Button(
                        onClick = { showHintDialog = false },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF2196F3),
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Got it!")
                    }
                },
                containerColor = Color(0xFFFFF8E1), // Soft yellow background
                tonalElevation = 6.dp
            )
        }
    }
}
