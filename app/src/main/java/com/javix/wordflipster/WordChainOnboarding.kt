package com.javix.wordflipster

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.javix.wordflipster.Navigation.Screens
import com.javix.wordflipster.ui.theme.WordChainLevel


@Composable
fun WordChainOnboardingWrapper(navController: NavHostController) {
    Box(
        modifier = Modifier
            .padding(8.dp)
            .padding(WindowInsets.ime.asPaddingValues())
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .fillMaxSize()
        ) {
            TopBar(navController) {}
            val context = LocalContext.current
            val dataStoreManager = remember { DataStoreManager(context) }
            val coroutineScope = rememberCoroutineScope()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 32.dp)
            ) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    ThreeSwitchButton()
                    MinutesButton(
                        dataStoreManager = dataStoreManager,
                        coroutineScope = coroutineScope
                    )
                    Button(
                        onClick = { navController.navigate(Screens.WordChainMainScreen.createRoute("Easy")) },
                        shape = RoundedCornerShape(
                            topStart = 16.dp,
                            topEnd = 16.dp,
                            bottomEnd = 16.dp,
                            bottomStart = 16.dp
                        ),  // Rounded corners
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        modifier = Modifier.size(200.dp, 50.dp)
                    ) {
                        Text("Play", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun ThreeSwitchButton() {
    var selectedOption by remember { mutableStateOf(1) } // Default selection

    Row(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Switch 1
        Button(
            onClick = { selectedOption = 1 },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedOption == 1) Color.Blue else Color.Gray
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = WordChainLevel.Easy.name, color = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp)) // Space between switches

        // Switch 2
        Button(
            onClick = { selectedOption = 2 },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedOption == 2) Color.Blue else Color.Gray
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = WordChainLevel.Medium.name, color = Color.White)
        }

        Spacer(modifier = Modifier.width(8.dp)) // Space between switches

        // Switch 3
        Button(
            onClick = { selectedOption = 3 },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = if (selectedOption == 3) Color.Blue else Color.Gray
            ),
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            modifier = Modifier.weight(1f)
        ) {
            Text(text = WordChainLevel.Hard.name, color = Color.White)
        }
    }
}
