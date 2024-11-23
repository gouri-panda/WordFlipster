package com.javix.wordflipster

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.javix.wordflipster.Navigation.Screens


@Composable
fun WordCrypticOnboarding(navController: NavController) {

    val context = LocalContext.current
    val dataStoreManager = remember { DataStoreManager(context) }
    val coroutineScope = rememberCoroutineScope()


    // State variables to hold the letter and minute counts


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp)
    ) {


        TopBar(navController) {}

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
//            Button(
//                onClick = { navController.navigate("puzzleCategory") },
//                shape = RoundedCornerShape(
//                    topStart = 16.dp,
//                    topEnd = 16.dp,
//                    bottomEnd = 16.dp,
//                    bottomStart = 16.dp
//                ),  // Rounded corners
//                colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
//                modifier = Modifier.size(200.dp, 50.dp)
//            ) {
//                Text("Category", color = Color.White)
//            } // Todo: Add later in the screen
            EditLettersButton( dataStoreManager, coroutineScope)
            MinutesButton(dataStoreManager, coroutineScope)
            Button(
                onClick = { navController.navigate(Screens.WordCrypticMainScreen.createRoute("")) },
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
