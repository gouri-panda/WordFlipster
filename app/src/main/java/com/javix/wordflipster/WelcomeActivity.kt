package com.javix.wordflipster

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class WelcomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 32.dp)
    ) {


        TopBarWelcomScreen()

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            EditLettersButton()
            MinutesButton()
            Button(
                onClick = { /*TODO*/ },
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

@Composable
fun EditLettersButton() {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {},
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("-", color = Color.Black) }
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),  // Rounded corners
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Blue background
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text("4 Letters", color = Color.White)  // White text
        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("+", color = Color.Black) }
    }

}

@Composable
fun MinutesButton() {

    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(
            onClick = {},
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("-", color = Color.Black) }
        Button(
            onClick = { /*TODO*/ },
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),  // Rounded corners
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Black),  // Blue background
            modifier = Modifier.size(150.dp, 50.dp)
        ) {
            Text("2 Minutes", color = Color.White)  // White text
        }
        Button(
            onClick = {},
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomEnd = 16.dp,
                bottomStart = 16.dp
            ),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.White),
            modifier = Modifier.size(50.dp, 50.dp)
        ) { Text("+", color = Color.Black) }
    }

}

@Composable
fun TopBarWelcomScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            // Icon for progress graph at the top left corner
            IconButton(
                onClick = { /* Handle progress graph action */ },
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
                onClick = { /* Handle settings action */ },
            ) {
                Icon(Icons.Default.Settings, contentDescription = "Settings", tint = Color.Black)
            }
        }
    }
}
