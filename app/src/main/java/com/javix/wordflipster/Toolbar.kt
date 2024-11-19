package com.javix.wordflipster

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@Composable
fun TopBar(navController: NavController, onClickListener: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top= 8.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            // Icon for progress graph at the top left corner
            IconButton(
                onClick = {
                    onClickListener()
                    navController.navigate("dashboard")
                          },
                // Padding from the top and left
            ) {
                Image(
                    painter = painterResource(id = R.drawable.progress_graph), // Replace with your drawable name
                    contentDescription = "Progress Graph",
                    modifier = Modifier.size(24.dp) // Set size as needed
                )
            }

            // Icon for settings at the bottom right corner
            IconButton(
                onClick = {
                    onClickListener()
                    navController.navigate("settings")
                          },
            ) {
                androidx.compose.material.Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.Black)
            }
        }
    }
}